package samplemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * A singleton providing convenience methods to obtain contributed model operations.
 */
public class SampleModelOperationRegistry
{
  public static final SampleModelOperationRegistry INSTANCE =
      new SampleModelOperationRegistry();

  private OperationLibrary operationLibraryRoot;

  private SampleModelOperationRegistry()
  {

  }

  public OperationLibrary getOperationLibraryRoot()
  {

    // lazy initialization
    if (operationLibraryRoot == null)
    {

      operationLibraryRoot = new OperationLibrary();

      // read all configuration elements and split them into libraries/operations
      IConfigurationElement[] configurationElements = Platform
          .getExtensionRegistry().getConfigurationElementsFor(
              "sampleModel.SampleModelOperation");

      Map<String, OperationLibrary> libraries = new HashMap<>();
      List<IConfigurationElement> operations =
          new ArrayList<IConfigurationElement>();

      for (IConfigurationElement element : configurationElements)
      {
        if ("library".equals(element.getName()))
        {
          String id = element.getAttribute("id");
          if (!libraries.containsKey(id))
          {
            String parentId = element.getAttribute("parentLibrary");
            String name = element.getAttribute("name");
            String documentation = element.getChildren("documentation")[0]
                .getValue();
            libraries.put(id, new OperationLibrary(name, id, parentId,
                documentation));
          }
        }
        else
        {
          operations.add(element);
        }
      }

      // build a tree of libraries
      for (OperationLibrary library : libraries.values())
      {
        if (library.getParentId() == null)
        {
          // no parent, add in the root
          operationLibraryRoot.addLibrary(library);
        }
        else
        {
          OperationLibrary parentCategory = libraries.get(library
              .getParentId());
          parentCategory.addLibrary(library);
        }
      }

      // put operations into corresponding libraries
      for (IConfigurationElement operation : operations)
      {
        String categoryId = operation.getAttribute("library");
        if (categoryId == null)
        {
          // no library, add in the root
          operationLibraryRoot.addOperation(operation);
        }
        else
        {
          OperationLibrary library = libraries.get(categoryId);
          library.addOperation(operation);
        }
      }
    }

    return operationLibraryRoot;
  }

}
