package sampleview.views.opsbrowser;

import org.eclipse.core.runtime.IConfigurationElement;

import samplemodel.IOperationLibraryBuilder;
import samplemodel.IOperationsBrowserLibraryBuilder;

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
    OperationsBrowserTreeNode child = new OperationsBrowserTreeNode(name,
        details);
    node.addChild(child);
    return new OperationsBrowserTreeBuilder(child);
  }

  @Override
  public void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor)
  {
    buildOperationNode(selection, operationName, operationDescriptor, true,
        null);
  }

  @Override
  public void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor, boolean applicable, String log)
  {
    OperationsBrowserOpNode operationNode = new OperationsBrowserOpNode(
        operationName, operationDescriptor, selection, applicable, log);
    node.addChild(operationNode);
  }

}
