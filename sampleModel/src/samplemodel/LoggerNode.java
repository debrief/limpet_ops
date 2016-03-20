package samplemodel;

import java.util.ArrayList;
import java.util.List;

public class LoggerNode
{
  private String message;
  private LoggerNode parent;
  private List<LoggerNode> children = new ArrayList<LoggerNode>();

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public LoggerNode getParent()
  {
    return parent;
  }

  public void setParent(LoggerNode parent)
  {
    this.parent = parent;
  }

  public List<LoggerNode> getChildren()
  {
    return children;
  }

  public void setChildren(List<LoggerNode> children)
  {
    this.children = children;
  }

  public boolean hasMessage()
  {
    if (message != null) {
      return true;
    }
    
    for (LoggerNode child : children)
    {
      if (child.hasMessage())
      {
        return true;
      }
    }
    return false;
  }

  public String log(int indentLevel)
  {
    StringBuilder sb = new StringBuilder();
    if (message != null) {
      for (int i = 0; i < indentLevel; i++)
      {
        sb.append("  ");
      }
      sb.append(message).append(System.lineSeparator());
      indentLevel ++;
    }

    for (LoggerNode node : children)
    {
      sb.append(node.log(indentLevel));
    }
    return sb.toString();
  }
}
