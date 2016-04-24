package samplemodel;

import java.util.List;

import org.eclipse.core.expressions.ElementHandler;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import samplemodel.failurelog.CustomElementHandler;

/**
 * An {@link OperationsLibraryBuilder} implementation is responsible for representing the
 * {@link OperationLibrary} in a visual way, such as a tree view or a menu.<br/>
 * Clients should use {@link #buildLibrary(IStructuredSelection, OperationLibrary)} method to create
 * the visual representation.<br/>
 * Subclasses need to implement {@link #buildGroupNode(String, String)} and
 * {@link #buildOperationNode(IConfigurationElement, IStructuredSelection)}
 * 
 */
public abstract class OperationsLibraryBuilder
{

  /**
   * @param selection
   *          selected items in the user interface, that is context for operations, whether they are
   *          applicable or not. Operations might have different representation in the UI (or none
   *          at all) based on the context.
   * @param library
   *          normally clients will call this method with the value of
   *          {@link SampleModelOperationRegistry#INSTANCE#getOperationLibraryRoot()}. However they
   *          might choose to build the library representation from a different starting point in
   *          the operations hierarchy.
   */
  public void buildLibrary(IStructuredSelection selection,
      OperationLibrary library)
  {

    for (IConfigurationElement operation : library.getOperations())
    {
      buildOperationNode(operation, selection);
    }

    // build sub libraries
    for (

    OperationLibrary c : library.getLibraries())

    {
      OperationsLibraryBuilder sublibraryBuilder = buildGroupNode(c.getName(), c
          .getDocumentation());
      sublibraryBuilder.buildLibrary(selection, c);
    }

  }

  /**
   * A group node holds other operation or group nodes
   * 
   * @param name
   * @return
   */
  protected abstract OperationsLibraryBuilder buildGroupNode(String name,
      String details);

  /**
   * Builds an operation node in the visual representation of the library
   * 
   * @param operation
   * @param selection
   */
  protected abstract void buildOperationNode(IConfigurationElement operation,
      IStructuredSelection selection);

  /**
   * A helper method for subclasses
   * 
   * @param objects
   * @param operation
   * @return
   */
  protected List<Object[]> getInputPermutations(final Object[] objects,
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

  /**
   * A helper method for subclasses
   * 
   * @param configElement
   *          the operation as it is described in its extension point
   * @param selection
   *          the context
   * @return <code>true</code> if the supplied operation is applicable in the given context
   */
  protected boolean isApplicable(IConfigurationElement configElement,
      ISelection selection)
  {
    boolean applicable = true;

    EvaluationContext evaluationContext = new EvaluationContext(null,
        selection);
    IConfigurationElement[] children = configElement.getChildren("applicable");

    final ElementHandler elementHandler = CustomElementHandler.getDefault();
    final ExpressionConverter converter = new ExpressionConverter(
        new ElementHandler[]
    {elementHandler});

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
