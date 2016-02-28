package samplemodel;

import java.text.MessageFormat;
import java.util.List;

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

  private IConfigurationElement[] configurationElements;

  private SampleModelOperationRegistry()
  {

  }

  public IConfigurationElement[] getOperations()
  {
    if (configurationElements == null)
    {
      // lazy initialization
      configurationElements = Platform.getExtensionRegistry()
          .getConfigurationElementsFor("sampleModel.SampleModelOperation");
    }
    return configurationElements;
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

    final IConfigurationElement[] operations = getOperations();

    EvaluationContext context = new EvaluationContext(null, selection);

    for (IConfigurationElement operation : operations)
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
