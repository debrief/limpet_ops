package sampleview.views.opsbrowser;

import org.eclipse.core.runtime.IConfigurationElement;

import samplemodel.IOperationsBrowserLibraryBuilder;
import samplemodel.IOperationLibraryBuilder;

public class OperationsBrowserTreeBuilder implements
    IOperationsBrowserLibraryBuilder
{

  private final OperationsBrowserTreeNode node;

  public OperationsBrowserTreeBuilder(OperationsBrowserTreeNode node)
  {
    this.node = node;
  }

  @Override
  public IOperationLibraryBuilder buildGroupNode(String name, String details)
  {
    OperationsBrowserTreeNode child =
        new OperationsBrowserTreeNode(name, details);
    node.addChild(child);
    return new OperationsBrowserTreeBuilder(child);
  }

  @Override
  public void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor)
  {
    buildOperationNode(selection, operationName, operationDescriptor, null);
  }

  @Override
  public void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor, String log)
  {
    String documentation =
        operationDescriptor.getChildren("documentation")[0].getValue();
    OperationsBrowserOpNode operationNode =
        new OperationsBrowserOpNode(operationName, documentation, log);
    node.addChild(operationNode);
  }

}
