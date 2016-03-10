package samplemodel;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.ExpressionTagNames;
import org.eclipse.core.internal.expressions.DefinitionRegistry;
import org.eclipse.core.internal.expressions.ExpressionMessages;
import org.eclipse.core.internal.expressions.ExpressionStatus;
import org.eclipse.core.internal.expressions.Messages;
import org.eclipse.core.internal.expressions.StandardElementHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
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

  private StringBuilder log = new StringBuilder();

  @Override
  public void log(String message)
  {
    log.append(message);
    log.append("\n");
  }

  public String getLog()
  {
    return log.toString();
  }

  public void resetLog()
  {
    log = new StringBuilder();
  }
}
