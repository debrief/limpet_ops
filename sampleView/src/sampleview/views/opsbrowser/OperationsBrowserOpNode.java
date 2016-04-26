package sampleview.views.opsbrowser;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Represents an operation (leaf node) in the hierarchical tree model of the operation library.
 * 
 */
public class OperationsBrowserOpNode extends OperationsBrowserTreeNode
{

  private final String failMessage;
  private final IConfigurationElement operationDescriptor;
  private final Object[] selection;

  public OperationsBrowserOpNode(String name,
      IConfigurationElement operationDescriptor, Object[] selection,
      boolean applicable, String failMessage, boolean operationGroup)
  {
    super(name, operationDescriptor.getChildren("documentation")[0].getValue(),
        applicable, operationGroup);
    this.operationDescriptor = operationDescriptor;
    this.selection = selection;
    this.failMessage = failMessage;
  }

  @Override
  public void addChild(OperationsBrowserTreeNode child)
  {
    throw new UnsupportedOperationException();
  }

  public String getFailMessage()
  {
    return failMessage;
  }

  public IConfigurationElement getOperationDescriptor()
  {
    return operationDescriptor;
  }

  public Object[] getSelection()
  {
    return selection;
  }

}
