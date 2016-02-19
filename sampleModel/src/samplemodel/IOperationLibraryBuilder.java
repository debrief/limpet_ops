package samplemodel;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * The library builder knows the specific context/framework in which the library is provided. It
 * separates traversing the operations from building the library (menu).
 */
public interface IOperationLibraryBuilder
{
  /**
   * A group node holds other operation or group nodes
   * 
   * @param name
   * @return
   */
  IOperationLibraryBuilder buildGroupNode(String name);

  /**
   * @param selection
   * @param operationName
   * @param operationDescriptor
   */
  void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor);
}
