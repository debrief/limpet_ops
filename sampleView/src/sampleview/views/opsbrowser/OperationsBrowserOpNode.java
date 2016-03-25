package sampleview.views.opsbrowser;

public class OperationsBrowserOpNode extends OperationsBrowserTreeNode
{

  private final String failMessage;
  private final boolean applicable;

  public OperationsBrowserOpNode(String name, String documentation,
      boolean applicable, String failMessage)
  {
    super(name, documentation);
    this.applicable = applicable;
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

  public boolean isApplicable()
  {
    return applicable;
  }

}
