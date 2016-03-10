package samplemodel;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.internal.expressions.DefinitionRegistry;
import org.eclipse.core.internal.expressions.Expressions;
import org.eclipse.core.internal.expressions.ReferenceExpression;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.w3c.dom.Element;

public class CustomReferenceExpression extends Expression
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

  private static final String ATT_DEFINITION_ID = "definitionId"; //$NON-NLS-1$

  /**
   * The seed for the hash code for all equals expressions.
   */
  private static final int HASH_INITIAL = ReferenceExpression.class.getName()
      .hashCode();

  private String fDefinitionId;

  private String failMessage;

  public CustomReferenceExpression(String definitionId)
  {
    Assert.isNotNull(definitionId);
    fDefinitionId = definitionId;
  }

  public CustomReferenceExpression(IConfigurationElement element,
      String failMessage) throws CoreException
  {
    this.failMessage = failMessage;
    fDefinitionId = element.getAttribute(ATT_DEFINITION_ID);
    Expressions.checkAttribute(ATT_DEFINITION_ID, fDefinitionId);
  }

  public CustomReferenceExpression(Element element, String failMessage)
      throws CoreException
  {
    this.failMessage = failMessage;
    fDefinitionId = element.getAttribute(ATT_DEFINITION_ID);
    Expressions.checkAttribute(ATT_DEFINITION_ID, fDefinitionId.length() > 0
        ? fDefinitionId : null);
  }

  public EvaluationResult evaluate(IEvaluationContext context)
      throws CoreException
  {
    Expression expr = getDefinitionRegistry().getExpression(fDefinitionId);
    return expr.evaluate(context);
  }

  public void collectExpressionInfo(ExpressionInfo info)
  {
    Expression expr;
    try
    {
      expr = getDefinitionRegistry().getExpression(fDefinitionId);
    }
    catch (CoreException e)
    {
      // We didn't find the expression definition. So no
      // expression info can be collected.
      return;
    }
    expr.collectExpressionInfo(info);
  }

  public boolean equals(final Object object)
  {
    if (!(object instanceof CustomReferenceExpression))
      return false;

    final CustomReferenceExpression that = (CustomReferenceExpression) object;
    return this.fDefinitionId.equals(that.fDefinitionId);
  }

  protected int computeHashCode()
  {
    return HASH_INITIAL * HASH_FACTOR + fDefinitionId.hashCode();
  }
}
