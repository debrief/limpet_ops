package sampleview.views;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import samplemodel.IOperationLibraryBuilder;
import samplemodel.SampleModelOperation;

/**
 * This {@link IOperationLibraryBuilder} builds a JFace menu of the applicable operations.
 * Individual operations are wrapped in {@link Action}s and loaded+executed when the action is run.
 */
public class OperationLibraryMenuBuilder implements IOperationLibraryBuilder
{

  private final IMenuManager menuManager;
  private final Shell shell;

  public OperationLibraryMenuBuilder(IMenuManager menuManager, Shell shell)
  {
    this.menuManager = menuManager;
    this.shell = shell;
  }

  @Override
  public IOperationLibraryBuilder buildGroupNode(String name)
  {
    MenuManager submenu = new MenuManager(name);
    menuManager.add(submenu);
    return new OperationLibraryMenuBuilder(submenu, shell);
  }

  @Override
  public void buildOperationNode(final Object[] selection,
      final String operationName,
      final IConfigurationElement operationDescriptor)
  {
    Action action = new Action(operationName)
    {
      @Override
      public void run()
      {
        try
        {
          SampleModelOperation operation =
              (SampleModelOperation) operationDescriptor
                  .createExecutableExtension("class");
          Object[] result = operation.execute(selection);
          MessageDialog.openInformation(shell, operationName, operationName
              + " operation result is " + Arrays.toString(result));
        }
        catch (CoreException e)
        {
          e.printStackTrace();
        }
      }
    };

    menuManager.add(action);
  }

}
