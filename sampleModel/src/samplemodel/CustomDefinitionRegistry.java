package samplemodel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.expressions.ElementHandler;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.internal.expressions.DefinitionRegistry;
import org.eclipse.core.internal.expressions.ExpressionMessages;
import org.eclipse.core.internal.expressions.ExpressionStatus;
import org.eclipse.core.internal.expressions.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

public class CustomDefinitionRegistry extends DefinitionRegistry
{
  private Map cache = null;

  private Map getCache()
  {
    if (cache == null)
    {
      cache = new HashMap();
    }
    return cache;
  }

  public CustomDefinitionRegistry()
  {
    Platform.getExtensionRegistry().addRegistryChangeListener(this,
        "org.eclipse.core.expressions"); //$NON-NLS-1$
  }

  /**
   * Get the expression with the id defined by an extension. This class will cache the expressions
   * when appropriate, so it's OK to always ask the registry.
   * 
   * @param id
   *          The unique ID of the expression definition
   * @return the expression
   * @throws CoreException
   *           If the expression cannot be found.
   */
  public Expression getExpression(String id) throws CoreException
  {
    Expression cachedExpression = (Expression) getCache().get(id);
    if (cachedExpression != null)
    {
      return cachedExpression;
    }

    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IConfigurationElement[] ces =
        registry.getConfigurationElementsFor(
            "org.eclipse.core.expressions", "definitions"); //$NON-NLS-1$ //$NON-NLS-2$

    Expression foundExpression = null;
    for (int i = 0; i < ces.length; i++)
    {
      String cid = ces[i].getAttribute("id"); //$NON-NLS-1$
      if (cid != null && cid.equals(id))
      {
        try
        {
          foundExpression = getExpression(id, ces[i]);
          break;
        }
        catch (InvalidRegistryObjectException e)
        {
          throw new CoreException(new ExpressionStatus(
              ExpressionStatus.MISSING_EXPRESSION, Messages.format(
                  ExpressionMessages.Missing_Expression, id)));
        }
      }
    }
    if (foundExpression == null)
    {
      throw new CoreException(new ExpressionStatus(
          ExpressionStatus.MISSING_EXPRESSION, Messages.format(
              ExpressionMessages.Missing_Expression, id)));
    }
    return foundExpression;
  }

  private Expression getExpression(String id, IConfigurationElement element)
      throws InvalidRegistryObjectException, CoreException
  {
    Expression expr = new ExpressionConverter(new ElementHandler[]
    {CustomElementHandler.getDefault()}).perform(element.getChildren()[0]);
    if (expr != null)
    {
      getCache().put(id, expr);
    }
    return expr;
  }

  public void registryChanged(IRegistryChangeEvent event)
  {
    IExtensionDelta[] extensionDeltas =
        event.getExtensionDeltas("org.eclipse.core.expressions", "definitions"); //$NON-NLS-1$//$NON-NLS-2$
    for (int i = 0; i < extensionDeltas.length; i++)
    {
      if (extensionDeltas[i].getKind() == IExtensionDelta.REMOVED)
      {
        IConfigurationElement[] ces =
            extensionDeltas[i].getExtension().getConfigurationElements();
        for (int j = 0; j < ces.length; j++)
        {
          String id = ces[j].getAttribute("id"); //$NON-NLS-1$
          if (id != null)
          {
            getCache().remove(id);
          }
        }
      }
    }
  }
}
