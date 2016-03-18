package samplemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.ExpressionTagNames;
import org.eclipse.core.internal.expressions.DefinitionRegistry;
import org.eclipse.core.internal.expressions.StandardElementHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.w3c.dom.Element;

public class CustomElementHandler extends StandardElementHandler implements
    CustomLogger
{

  // consider making this a more general extension manager
  // for now it's just part of the reference expression
  private static DefinitionRegistry fgDefinitionRegistry = null;

  private static DefinitionRegistry getDefinitionRegistry()
  {
    if (fgDefinitionRegistry == null)
    {
      fgDefinitionRegistry = new CustomDefinitionRegistry();
    }
    return fgDefinitionRegistry;
  }

  private static final CustomElementHandler INSTANCE =
      new CustomElementHandler();

  public static CustomElementHandler getDefault()
  {
    return INSTANCE;
  }

  @Override
  public Expression create(ExpressionConverter converter,
      IConfigurationElement config) throws CoreException
  {
    Expression expression = null;
    String name = config.getName();
    String failMessage = config.getAttribute("failMessage");

    if (ExpressionTagNames.REFERENCE.equals(name))
    {
      expression = new CustomReferenceExpression(config, failMessage);
    }
    else
    {
      expression = super.create(converter, config);
      expression = new CustomExpression(expression, failMessage, this);
    }
    return expression;
  }

  @Override
  public Expression create(ExpressionConverter converter, Element element)
      throws CoreException
  {
    Expression expression = null;
    String name = element.getNodeName();
    String failMessage = element.getAttribute("failMessage");

    if (ExpressionTagNames.REFERENCE.equals(name))
    {
      expression = new CustomReferenceExpression(element, failMessage);
    }
    else
    {
      expression = super.create(converter, element);
      expression = new CustomExpression(expression, failMessage, this);
    }

    return expression;
  }

  private List<String> log = new ArrayList<String>();
  private int logIndent = 0;
  private int mark = -1;

  @Override
  public void log(String message)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < logIndent; i++)
    {
      sb.append("  ");
    }
    sb.append(message);

    log.add(sb.toString());

  }

  public String getLog()
  {
    StringBuilder sb = new StringBuilder();
    for (String s : log)
    {
      sb.append(s).append("\n");
    }
    return sb.toString();
  }

  public void resetLog()
  {
    log.clear();
  }

  @Override
  public void pushIndent()
  {
    logIndent++;
    mark = log.size();
  }

  @Override
  public void popIndent()
  {
    logIndent--;
    if (log.size() > mark)
    {
      List<String> sublist = log.subList(mark, log.size());
      Collections.reverse(sublist);
    }
  }
}
