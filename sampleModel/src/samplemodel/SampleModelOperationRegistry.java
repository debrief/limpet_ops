package samplemodel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.expressions.ElementHandler;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;

import samplemodel.failurelog.CustomElementHandler;

/**
 * A singleton providing convenience methods to obtain contributed model operations and to build a
 * library (i.e. menu) of operations
 */
public class SampleModelOperationRegistry
{
  public static final SampleModelOperationRegistry INSTANCE =
      new SampleModelOperationRegistry();

  private OperationLibrary operationLibraryRoot;

  private SampleModelOperationRegistry()
  {

  }

  OperationLibrary getOperationLibraryRoot()
  {

    // lazy initialization
    if (operationLibraryRoot == null)
    {

      operationLibraryRoot = new OperationLibrary();

      // read all configuration elements and split them into libraries/operations
      IConfigurationElement[] configurationElements =
          Platform.getExtensionRegistry().getConfigurationElementsFor(
              "sampleModel.SampleModelOperation");

      Map<String, OperationLibrary> libraries = new HashMap<>();
      List<IConfigurationElement> operations =
          new ArrayList<IConfigurationElement>();

      for (IConfigurationElement element : configurationElements)
      {
        if ("library".equals(element.getName()))
        {
          String id = element.getAttribute("id");
          if (!libraries.containsKey(id))
          {
            String parentId = element.getAttribute("parentLibrary");
            String name = element.getAttribute("name");
            libraries.put(id, new OperationLibrary(name, id, parentId));
          }
        }
        else
        {
          operations.add(element);
        }
      }

      // build a tree of libraries
      for (OperationLibrary library : libraries.values())
      {
        if (library.getParentId() == null)
        {
          // no parent, add in the root
          operationLibraryRoot.addLibrary(library);
        }
        else
        {
          OperationLibrary parentCategory =
              libraries.get(library.getParentId());
          parentCategory.addLibrary(library);
        }
      }

      // put operations into corresponding libraries
      for (IConfigurationElement operation : operations)
      {
        String categoryId = operation.getAttribute("library");
        if (categoryId == null)
        {
          // no library, add in the root
          operationLibraryRoot.addOperation(operation);
        }
        else
        {
          OperationLibrary library = libraries.get(categoryId);
          library.addOperation(operation);
        }
      }
    }

    return operationLibraryRoot;
  }

  /**
   * 
   * @param selection
   *          the selection for which the applicable operations will be provided
   * @param builder
   *          the builder separates operation traversal from building the library
   */
  public void buildLibrary(IStructuredSelection selection,
      IOperationLibraryBuilder builder)
  {

    OperationLibrary operationLibraryRoot = getOperationLibraryRoot();
    buildLibrary(selection, builder, operationLibraryRoot);
  }

  private void buildLibrary(IStructuredSelection selection,
      IOperationLibraryBuilder builder, OperationLibrary library)
  {

    EvaluationContext context = new EvaluationContext(null, selection);

    for (IConfigurationElement operation : library.getOperations())
    {
      boolean applicable = isApplicable(operation, context);

      String label = operation.getAttribute("label");

      if (applicable)
      {
        final List<Object[]> inputPermutations =
            getInputPermutations(selection.toArray(), operation);

        IOperationLibraryBuilder target = builder;
        if (inputPermutations.size() > 1)
        {
          target = builder.buildGroupNode(label);
        }

        String permutationLabel = operation.getAttribute("permutationLabel");
        if (permutationLabel != null)
        {
          label = permutationLabel;
        }

        for (Object[] inputPermutation : inputPermutations)
        {
          String operationName = MessageFormat.format(label, inputPermutation);
          target.buildOperationNode(inputPermutation, operationName, operation);
        }

      }
      else
      {
        String log = CustomElementHandler.getDefault().getLog();
        if (!log.isEmpty())
        {
          System.out.println("[" + label + "] failed because of:");
          System.out.println(log);
        }
        CustomElementHandler.getDefault().resetLog();
      }
    }

    // build sub libraries
    for (OperationLibrary c : library.getLibraries())
    {
      IOperationLibraryBuilder sublibraryBuilder =
          builder.buildGroupNode(c.getName());
      buildLibrary(selection, sublibraryBuilder, c);
    }
  }

  private List<Object[]> getInputPermutations(final Object[] objects,
      IConfigurationElement operation)
  {
    // by default assume commutative operation
    OperationInputPermutator inputPermutator = new NoInputPermutations();
    if (operation.getAttribute("inputPermutator") != null)
    {
      // a non-commutative operation, i.e. a+b=b+a
      try
      {
        inputPermutator =
            (OperationInputPermutator) operation
                .createExecutableExtension("inputPermutator");
      }
      catch (CoreException e)
      {
        e.printStackTrace();
      }
    }
    return inputPermutator.getOperationInputPermutations(objects);
  }

  private boolean isApplicable(IConfigurationElement configElement,
      IEvaluationContext evaluationContext)
  {
    boolean applicable = true;

    IConfigurationElement[] children = configElement.getChildren("applicable");

    final ElementHandler elementHandler = CustomElementHandler.getDefault();
    final ExpressionConverter converter =
        new ExpressionConverter(new ElementHandler[]
        {elementHandler});

    if (children.length > 0)
    {
      IConfigurationElement applicableElement = children[0];

      final IConfigurationElement[] expressionElements =
          applicableElement.getChildren();
      if (expressionElements.length > 0)
      {

        final IConfigurationElement expressionElement = expressionElements[0];

        try
        {
          Expression applicableExpression =
              elementHandler.create(converter, expressionElement);
          applicable =
              applicableExpression.evaluate(evaluationContext).equals(
                  EvaluationResult.TRUE);
        }
        catch (CoreException e)
        {
          e.printStackTrace();
        }
      }

    }
    return applicable;
  }

}
