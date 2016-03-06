package sampletest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;

import samplemodel.IOperationLibraryBuilder;

/**
 * Test helper. Implementation of {@link IOperationLibraryBuilder}, which collects the operations in
 * a {@link Map}.
 */
public class MockOperationLibraryBuilder implements IOperationLibraryBuilder
{

  static final String SEPARATOR = "//";

  // A map of operation id -> menu path
  private Map<String, String> operations = new HashMap<>();

  private String path = SEPARATOR;

  @Override
  public void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor)
  {
    String implementationClass = operationDescriptor.getAttribute("class");
    operations.put(implementationClass, path);
  }

  @Override
  public IOperationLibraryBuilder buildGroupNode(String name)
  {
    path = path + name + SEPARATOR;
    return this;
  }

  /**
   * 
   * @param implementationClass
   * @return <code>null</code> if the operation was not applicable for the provided selection,
   *         otherwise return the menu path for the operation
   */
  public String getOperationPath(String implementationClass)
  {
    return operations.get(implementationClass);
  }
}