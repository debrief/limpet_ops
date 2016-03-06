package samplemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Helper representation of operation categories (besides IConfigurationElement) to allow building a
 * tree structure to represent in a menu
 */
class OperationCategory
{

  /**
   * The name of the category
   */
  private final String name;

  /**
   * The id of the category
   */
  private final String id;

  /**
   * The id of the parent category, can be <code>null</code>
   */
  private final String parentId;

  private final List<OperationCategory> subcategories =
      new ArrayList<OperationCategory>();
  private final List<IConfigurationElement> operations =
      new ArrayList<IConfigurationElement>();

  /**
   * Construct a root category
   */
  OperationCategory()
  {
    this(null, null, null);
  }

  OperationCategory(String name, String id, String parentId)
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

  public void addSubcategory(OperationCategory category)
  {
    subcategories.add(category);
  }

  public void addOperation(IConfigurationElement operation)
  {
    operations.add(operation);
  }

  public String getParentId()
  {
    return parentId;
  }

  public List<OperationCategory> getSubcategories()
  {
    return Collections.unmodifiableList(subcategories);
  }

  public List<IConfigurationElement> getOperations()
  {
    return Collections.unmodifiableList(operations);
  }
}
