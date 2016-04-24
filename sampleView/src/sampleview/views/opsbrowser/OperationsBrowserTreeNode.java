package sampleview.views.opsbrowser;

import java.util.ArrayList;
import java.util.List;

/**
 * A base model object for the tree representation of the operations library. Created by the
 * {@link OperationsBrowserTreeBuilder} and provided as an input (model) to the UI tree control.
 * <br/>
 * Represents a group/library of operations
 * 
 * @see OperationsBrowserOpNode
 */
public class OperationsBrowserTreeNode
{

  private List<OperationsBrowserTreeNode> children = new ArrayList<>();
  private final String name;
  private final String documentation;

  OperationsBrowserTreeNode(String name, String documentation)
  {
    this.name = name;
    this.documentation = documentation;
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

  public String getDocumentation()
  {
    return documentation;
  }

}
