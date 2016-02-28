package sampletest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

import samplemodel.IOperationLibraryBuilder;

/**
 * Test helper. Implementation of {@link IOperationLibraryBuilder}, which collects the operations in
 * a flat {@link List}. No nesting/grouping.
 */
public class MockOperationLibraryBuilder implements IOperationLibraryBuilder
{

  private List<String> operations = new ArrayList<String>();

  @Override
  public void buildOperationNode(Object[] selection, String operationName,
      IConfigurationElement operationDescriptor)
  {
    String implementationClass = operationDescriptor.getAttribute("class");
    operations.add(implementationClass);
  }

  @Override
  public IOperationLibraryBuilder buildGroupNode(String name)
  {
    return this;
  }

  public boolean containsOperation(String implementationClass)
  {
    return operations.contains(implementationClass);
  }
}