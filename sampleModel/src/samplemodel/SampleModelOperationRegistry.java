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

/**
 * A singleton providing convenience methods to obtain contributed model operations and to build a
 * library (i.e. menu) of operations
 */
public class SampleModelOperationRegistry
{
  public static final SampleModelOperationRegistry INSTANCE =
      new SampleModelOperationRegistry();

  private OperationCategory operationCategoryRoot;

  private SampleModelOperationRegistry()
  {

  }

  OperationCategory getOperationCategoryRoot()
  {

    // lazy initialization
    if (operationCategoryRoot == null)
    {

      operationCategoryRoot = new OperationCategory();

      // read all configuration elements and split them into categories/operations
      IConfigurationElement[] configurationElements = Platform
          .getExtensionRegistry().getConfigurationElementsFor(
              "sampleModel.SampleModelOperation");

      Map<String, OperationCategory> categories = new HashMap<>();
      List<IConfigurationElement> operations =
          new ArrayList<IConfigurationElement>();

      for (IConfigurationElement element : configurationElements)
      {
        if ("category".equals(element.getName()))
        {
          String id = element.getAttribute("id");
          if (!categories.containsKey(id))
          {
            String parentId = element.getAttribute("parentCategory");
            String name = element.getAttribute("name");
            categories.put(id, new OperationCategory(name, id, parentId));
          }
        }
        else
        {
          operations.add(element);
        }
      }

      // build a tree of categories
      for (OperationCategory category : categories.values())
      {
        if (category.getParentId() == null)
        {
          // no parent, add in the root
          operationCategoryRoot.addSubcategory(category);
        }
        else
        {
          OperationCategory parentCategory = categories.get(category
              .getParentId());
          parentCategory.addSubcategory(category);
        }
      }

      // put operations into corresponding categories
      for (IConfigurationElement operation : operations)
      {
        String categoryId = operation.getAttribute("category");
        if (categoryId == null)
        {
          // no category, add in the root
          operationCategoryRoot.addOperation(operation);
        }
        else
        {
          OperationCategory category = categories.get(categoryId);
          category.addOperation(operation);
        }
      }
    }

    return operationCategoryRoot;
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

    OperationCategory operationCategoryRoot = getOperationCategoryRoot();
    buildLibrary(selection, builder, operationCategoryRoot);
  }

  private void buildLibrary(IStructuredSelection selection,
      IOperationLibraryBuilder builder, OperationCategory category)
  {

    EvaluationContext context = new EvaluationContext(null, selection);

    for (IConfigurationElement operation : category.getOperations())
    {
      boolean applicable = isApplicable(operation, context);

      if (applicable)
      {
        final List<Object[]> inputPermutations = getInputPermutations(selection
            .toArray(), operation);
        String label = operation.getAttribute("label");

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
    }

    // build subcategories
    for (OperationCategory c : category.getSubcategories())
    {
      IOperationLibraryBuilder subcategoryBuilder = builder.buildGroupNode(c
          .getName());
      buildLibrary(selection, subcategoryBuilder, c);
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
        inputPermutator = (OperationInputPermutator) operation
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

    final ElementHandler elementHandler = ElementHandler.getDefault();
    final ExpressionConverter converter = ExpressionConverter.getDefault();

    if (children.length > 0)
    {
      IConfigurationElement applicableElement = children[0];

      final IConfigurationElement[] expressionElements = applicableElement
          .getChildren();
      if (expressionElements.length > 0)
      {

        final IConfigurationElement expressionElement = expressionElements[0];

        try
        {
          Expression applicableExpression = elementHandler.create(converter,
              expressionElement);
          applicable = applicableExpression.evaluate(evaluationContext).equals(
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
