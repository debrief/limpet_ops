package samplemodel.failurelog;

/**
 * An expression logger is used during {@link CustomExpression} evaluation to hold the failure
 * messages in a hierarchical structure. The logger usage happens in the following steps:
 * <ol>
 * <li>{@link #resetLog()}
 * <li>{@link #pushNode()}
 * <li>{@link #log(String)}
 * <li>{@link #popNode()}
 * <li>{@link #getLog()}
 * </ol>
 */
public interface ExpressionLogger
{
  /**
   * Reset the state of the log, removing all previous messages.
   */
  void resetLog();

  /**
   * Create a child node and attaches it to the hierarchy. It becomes the "current node"
   * 
   * @param message
   */
  void pushNode();

  /**
   * Log a message in the current node
   */
  void log(String message);

  /**
   * Goes one level up in the hierarchy. Current node pointer is moved to the parent.
   */
  void popNode();

  /**
   * In the end, a String representation of the log is obtained
   * 
   * @return
   */
  String getLog();
}
