package sampleview.views;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import samplemodel.OperationsLibraryBuilder;
import samplemodel.SampleModelOperation;

/**
 * This {@link OperationsLibraryBuilder} builds a JFace menu of the applicable operations.
 * Individual operations are wrapped in {@link Action}s and loaded+executed when the action is run.
 */
public class OperationsLibraryMenuBuilder extends OperationsLibraryBuilder
{

  private final IMenuManager menuManager;
  private final Shell shell;

  public OperationsLibraryMenuBuilder(IMenuManager menuManager, Shell shell)
  {
    this.menuManager = menuManager;
    this.shell = shell;
  }

  protected OperationsLibraryBuilder buildGroupNode(String name, String details)
  {
    MenuManager submenu = new MenuManager(name);
    menuManager.add(submenu);
    return new OperationsLibraryMenuBuilder(submenu, shell);
  }

  private void buildOperationNode(final Object[] selection,
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

  @Override
  protected void buildOperationNode(IConfigurationElement operation,
      IStructuredSelection selection)
  {
    boolean applicable = isApplicable(operation, selection);
    String label = operation.getAttribute("label");
    String permutationLabel = operation.getAttribute("permutationLabel");
    List<Object[]> inputPermutations = getInputPermutations(selection.toArray(),
        operation);

    OperationsLibraryMenuBuilder builder = this;
    if (inputPermutations.size() > 1)
    {

      builder = (OperationsLibraryMenuBuilder) builder.buildGroupNode(label,
          null);
      if (permutationLabel != null)
      {
        label = label + " " + permutationLabel;
      }
    }

    if (applicable)
    {
      for (Object[] inputPermutation : inputPermutations)
      {
        String operationName = MessageFormat.format(label, inputPermutation);
        builder.buildOperationNode(inputPermutation, operationName, operation);
      }
    }
  }
}
