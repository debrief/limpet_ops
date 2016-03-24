package sampleview.views.opsbrowser;

import java.util.ArrayList;
import java.util.List;

public class OperationsBrowserTreeNode
{

  private List<OperationsBrowserTreeNode> children = new ArrayList<>();
  private final String name;

  public OperationsBrowserTreeNode(String name)
  {
    this.name = name;
  }

  public void addChild(OperationsBrowserTreeNode child)
  {
    children.add(child);
  }

  public OperationsBrowserTreeNode[] getChildren()
  {
    OperationsBrowserTreeNode[] childrenArray =
        new OperationsBrowserTreeNode[children.size()];
    return (OperationsBrowserTreeNode[]) children.toArray(childrenArray);
  }

  public String getName()
  {
    return name;
  }

}
