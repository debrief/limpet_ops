package samplemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Helper representation of operation categories (besides IConfigurationElement) to allow building a
 * tree structure to represent in a menu
 */
class OperationLibrary
{

  /**
   * The name of the library
   */
  private final String name;

  /**
   * The id of the library
   */
  private final String id;

  /**
   * The id of the parent library, can be <code>null</code>
   */
  private final String parentId;

  private final List<OperationLibrary> libraries =
      new ArrayList<OperationLibrary>();
  private final List<IConfigurationElement> operations =
      new ArrayList<IConfigurationElement>();

  /**
   * Construct a root library, the root library does not have a representation in the UI
   */
  OperationLibrary()
  {
    this(null, null, null);
  }

  OperationLibrary(String name, String id, String parentId)
  {
    this.name = name;
    this.id = id;
    this.parentId = parentId;
  }

  public String getName()
  {
    return name;
  }

  public String getId()
  {
    return id;
  }

  public void addLibrary(OperationLibrary library)
  {
    libraries.add(library);
  }

  public void addOperation(IConfigurationElement operation)
  {
    operations.add(operation);
  }

  public String getParentId()
  {
    return parentId;
  }

  public List<OperationLibrary> getLibraries()
  {
    return Collections.unmodifiableList(libraries);
  }

  public List<IConfigurationElement> getOperations()
  {
    return Collections.unmodifiableList(operations);
  }
}
