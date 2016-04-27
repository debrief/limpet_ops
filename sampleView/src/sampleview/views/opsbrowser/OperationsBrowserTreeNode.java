package sampleview.views.opsbrowser;

import java.util.ArrayList;
import java.util.List;

/**
 * A base model object for the tree representation of the operations library. Created by the
 * {@link OperationsBrowserTreeBuilder} and provided as an input (model) to the UI tree control. <br/>
 * Represents a group/library of operations
 * 
 * @see OperationsBrowserOpNode
 */
public class OperationsBrowserTreeNode
{

  private List<OperationsBrowserTreeNode> children = new ArrayList<>();
  private final String name;
  private final String documentation;
  private final boolean applicable;
  // true for non-commutative operations
  private final boolean operationGroup;

  OperationsBrowserTreeNode()
  {
    // the name for the root is just for debug purposes, anything would be OK
    this("_ROOT_", null, true, false);
  }

  OperationsBrowserTreeNode(String name, String documentation,
      boolean applicable, boolean operationGroup)
  {
    this.name = name;
    this.documentation = documentation;
    this.applicable = applicable;
    this.operationGroup = operationGroup;
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

  public boolean isOperationGroup()
  {
    return operationGroup;
  }

  public boolean isApplicable()
  {
    return applicable;
  }

}
