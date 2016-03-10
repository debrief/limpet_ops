package samplemodel;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

public class CustomExpression extends Expression
{

  private final Expression wrapped;
  private final String failMessage;
  private CustomLogger logger;

  public CustomExpression(Expression wrapped, String failMessage,
      CustomLogger logger)
  {
    this.wrapped = wrapped;
    this.failMessage = failMessage;
    this.logger = logger;
  }

  @Override
  public EvaluationResult evaluate(IEvaluationContext context)
      throws CoreException
  {
    EvaluationResult result = wrapped.evaluate(context);
    if (result == EvaluationResult.FALSE && failMessage != null)
    {
      logger.log(failMessage);
    }
    return result;
  }

  @Override
  public void collectExpressionInfo(ExpressionInfo info)
  {
    info.addMisBehavingExpressionType(wrapped.getClass());
  }

  @Override
  public int hashCode()
  {
    return wrapped.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomExpression other = (CustomExpression) obj;
    if (wrapped == null)
    {
      if (other.wrapped != null)
        return false;
    }
    else if (!wrapped.equals(other.wrapped))
      return false;
    return true;
  }

}
