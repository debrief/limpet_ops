package samplemodel.failurelog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.expressions.Expression;

/**
 * Represents a node in a hierarchical tree structure of logger nodes. Each node has a message and
 * optionally parent/children nodes. A {@link LoggerNode} is meant to map to an {@link Expression}
 * in a parsed expression tree. For example, given the expression (simplified, not-actual syntax):
 * 
 * <pre>
 * {@code
 * <and failMessage="A">
 *  <test1 failMessage="B"/>
 *  <test2 failMessage="C"/>
 * </and>
 * }
 * </pre>
 * 
 * ,when <code>test1</code> fails, the logger node tree will look like this:
 * 
 * <pre>
 * {@code
 * LoggerNode(message:A)
 *  |
 *  +- LoggerNode(message:B)
 * }
 * 
 * <pre>
 */
class LoggerNode
{
  private String message;
  private LoggerNode parent;
  private List<LoggerNode> children = new ArrayList<LoggerNode>();

  String getMessage()
  {
    return message;
  }

  void setMessage(String message)
  {
    this.message = message;
  }

  LoggerNode getParent()
  {
    return parent;
  }

  void setParent(LoggerNode parent)
  {
    this.parent = parent;
  }

  List<LoggerNode> getChildren()
  {
    return children;
  }

  void setChildren(List<LoggerNode> children)
  {
    this.children = children;
  }

  boolean hasMessage()
  {
    return message != null || hasChildMessage();
  }

  private boolean hasChildMessage()
  {
    for (LoggerNode child : children)
    {
      if (child.hasMessage())
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Recursively logs the node - its message and all children with increased indentation level
   * 
   * @param indentLevel
   * @return
   */
  String log(int indentLevel)
  {
    StringBuilder sb = new StringBuilder();
    if (message != null)
    {
      for (int i = 0; i <= indentLevel; i++)
      {
        sb.append("  ");
      }
      sb.append("[").append(message).append("]");
      if (hasChildMessage())
      {
        sb.append(" failed because of:");
      }
      sb.append(System.lineSeparator());
      indentLevel++;
    }

    for (LoggerNode node : children)
    {
      sb.append(node.log(indentLevel));
    }
    return sb.toString();
  }
}
