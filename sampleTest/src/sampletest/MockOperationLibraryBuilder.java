package sampletest;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IStructuredSelection;

import samplemodel.OperationsLibraryBuilder;

/**
 * Test helper. Implementation of {@link IOperationLibraryBuilder}, which collects the operations in
 * a {@link Map}.
 */
public class MockOperationLibraryBuilder extends OperationsLibraryBuilder
{

  static final String SEPARATOR = "//";

  // A map of operation id -> menu path
  private Map<String, String> operations = new HashMap<>();

  private String path = SEPARATOR;

  protected void buildOperationNode(IConfigurationElement operation,
      IStructuredSelection selection)
  {
    if (isApplicable(operation, selection))
    {
      String implementationClass = operation.getAttribute("class");
      operations.put(implementationClass, path);
    }
  }

  @Override
  public OperationsLibraryBuilder buildGroupNode(String name, String details)
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