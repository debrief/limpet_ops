package sampleview.views.opsbrowser;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IStructuredSelection;

import samplemodel.OperationsLibraryBuilder;
import samplemodel.failurelog.CustomElementHandler;

/**
 * Builds a hierarchical tree model to represent the operation library in a UI tree control
 * 
 */
public class OperationsBrowserTreeBuilder extends OperationsLibraryBuilder
{

  private final OperationsBrowserTreeNode node;

  public OperationsBrowserTreeBuilder(OperationsBrowserTreeNode node)
  {
    this.node = node;
  }

  protected OperationsLibraryBuilder
      buildGroupNode(String name, String details)
  {
    return buildGroupNodeInternal(name, details, true, false);
  }

  protected OperationsLibraryBuilder buildGroupNodeInternal(String name,
      String details, boolean applicable, boolean operationGroup)
  {
    OperationsBrowserTreeNode child =
        new OperationsBrowserTreeNode(name, details, applicable, operationGroup);
    node.addChild(child);
    return new OperationsBrowserTreeBuilder(child);
  }

  private void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor, boolean applicable,
      String log, boolean operationGroup)
  {
    OperationsBrowserOpNode operationNode =
        new OperationsBrowserOpNode(operationName, operationDescriptor,
            selection, applicable, log, operationGroup);
    node.addChild(operationNode);
  }

  @Override
  protected void buildOperationNode(IConfigurationElement operation,
      IStructuredSelection selection)
  {

    boolean applicable = isApplicable(operation, selection);
    String label = operation.getAttribute("label");
    String permutationLabel = operation.getAttribute("permutationLabel");
    List<Object[]> inputPermutations =
        getInputPermutations(selection.toArray(), operation);

    OperationsBrowserTreeBuilder builder = this;

    String failMessage = CustomElementHandler.getDefault().getLog();
    CustomElementHandler.getDefault().resetLog();

    if (applicable)
    {
      if (inputPermutations.size() > 1)
      {
        builder =
            (OperationsBrowserTreeBuilder) builder.buildGroupNodeInternal(
                label, null, true, true);

        if (permutationLabel != null)
        {
          label = label + " " + permutationLabel;
        }
      }

      for (Object[] inputPermutation : inputPermutations)
      {
        String operationName = MessageFormat.format(label, inputPermutation);
        builder.buildOperationNode(inputPermutation, operationName, operation,
            applicable, failMessage, false);
      }
    }
    else
    {
      boolean operationGroup =
          operation.getAttribute("inputPermutator") != null;

      builder.buildOperationNode(inputPermutations.get(0), label, operation,
          applicable, failMessage, operationGroup);
    }

  }
}
