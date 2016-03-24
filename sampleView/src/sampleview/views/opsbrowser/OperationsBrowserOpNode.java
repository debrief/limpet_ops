package sampleview.views.opsbrowser;

public class OperationsBrowserOpNode extends OperationsBrowserTreeNode
{

  private final String failMessage;

  public OperationsBrowserOpNode(String name, String failMessage)
  {
    super(name);
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

}
