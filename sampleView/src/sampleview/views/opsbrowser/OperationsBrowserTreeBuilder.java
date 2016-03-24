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
  public IOperationLibraryBuilder buildGroupNode(String name)
  {
    OperationsBrowserTreeNode child = new OperationsBrowserTreeNode(name);
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
    OperationsBrowserOpNode operationNode = new OperationsBrowserOpNode(
        operationName, log);
    node.addChild(operationNode);
  }

}
