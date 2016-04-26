package sampleview.views.opsbrowser;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import samplemodel.SampleModelOperation;
import sampleview.views.SampleView;

/**
 * Things that might be improved:
 * <ul>
 * <li>Avoid expand all, try to preserve the selection (only keep the current node expanded)
 * <li>Present the Applicability Test Output as a tree
 * <li>Show "No applicable operations" text instead of an empty tree viewer
 * </ul>
 * 
 */
public class OperationsBrowserView extends ViewPart
{

  private TreeViewer operationsViewer;
  private Browser documentationBrowser;
  private Text applicabilityTestOutputText;

  private ISelectionListener viewerInputUpdater = new ISelectionListener()
  {
    public void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
      updateViewer(selection);
    }
  };

  private ViewerFilter onlyApplicableOperationsFilter = new ViewerFilter()
  {

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
      OperationsBrowserTreeNode node = (OperationsBrowserTreeNode) element;

      // accept only libraries or applicable operations
      return hasApplicableChild(node) || node instanceof OperationsBrowserOpNode
          && ((OperationsBrowserOpNode) element).isApplicable();
    }

    private boolean hasApplicableChild(OperationsBrowserTreeNode node)
    {
      for (OperationsBrowserTreeNode child : node.getChildren())
      {
        if (child instanceof OperationsBrowserOpNode)
        {
          if (((OperationsBrowserOpNode) child).isApplicable())
          {
            return true;
          }
        }
        else if (hasApplicableChild(child))
        {
          return true;
        }
      }
      return false;
    }
  };
  private Button applyBtn;

  @Override
  public void createPartControl(Composite parent)
  {
    parent.setLayout(new GridLayout(1, false));

    final Combo filterSelector = new Combo(parent, SWT.READ_ONLY);
    filterSelector.add("All operations");
    filterSelector.add("Operations applicable to active selection");
    filterSelector.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        int selectionIndex = filterSelector.getSelectionIndex();
        if (selectionIndex == 1)
        {
          operationsViewer.setFilters(new ViewerFilter[]
          {onlyApplicableOperationsFilter});
        }
        else
        {
          operationsViewer.setFilters(new ViewerFilter[0]);
        }
      }
    });

    operationsViewer = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER);
    operationsViewer.setContentProvider(new OperationsContentProvider());
    operationsViewer.setLabelProvider(new OperationsLabelProvider());
    operationsViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {

      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        updateDetails((IStructuredSelection) event.getSelection());
      }
    });

    GridDataFactory.fillDefaults().grab(true, true).applyTo(operationsViewer
        .getControl());

    applyBtn = new Button(parent, SWT.PUSH);
    applyBtn.setText("Apply Operation");
    applyBtn.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        handleApplyOperation();
      }
    });
    GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(
        applyBtn);

    new Label(parent, SWT.NONE).setText("Documentation:");

    documentationBrowser = new Browser(parent, SWT.BORDER);
    GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 80)
        .applyTo(documentationBrowser);

    new Label(parent, SWT.NONE).setText("Applicability Test Output:");
    applicabilityTestOutputText = new Text(parent, SWT.V_SCROLL | SWT.H_SCROLL
        | SWT.BORDER);
    applicabilityTestOutputText.setEditable(false);
    applicabilityTestOutputText.setBackground(parent.getShell().getDisplay()
        .getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 80)
        .applyTo(applicabilityTestOutputText);

    updateViewer(new StructuredSelection());
    filterSelector.select(0);
  }

  protected void handleApplyOperation()
  {
    try
    {
      IStructuredSelection selectedOperation =
          (IStructuredSelection) operationsViewer.getSelection();
      OperationsBrowserOpNode node = (OperationsBrowserOpNode) selectedOperation
          .getFirstElement();
      IConfigurationElement operationDescriptor = node.getOperationDescriptor();
      SampleModelOperation operation =
          (SampleModelOperation) operationDescriptor.createExecutableExtension(
              "class");
      Object[] result = operation.execute(node.getSelection());
      MessageDialog.openInformation(getSite().getShell(), node.getName(), node
          .getName() + " operation result is " + Arrays.toString(result));
    }
    catch (CoreException e)
    {
      e.printStackTrace();
    }
  }

  protected void updateDetails(IStructuredSelection selection)
  {

    applicabilityTestOutputText.setText("");
    applyBtn.setEnabled(false);

    if (!selection.isEmpty())
    {
      OperationsBrowserTreeNode node = (OperationsBrowserTreeNode) selection
          .getFirstElement();
      if (node.getDocumentation() != null)
      {
        documentationBrowser.setText(node.getDocumentation());
      }
      else
      {
        documentationBrowser.setText("Grouping node");
      }

      if (node instanceof OperationsBrowserOpNode)
      {
        OperationsBrowserOpNode opNode = (OperationsBrowserOpNode) node;
        if (!opNode.isApplicable())
        {
          String failMessage = opNode.getFailMessage();

          if (failMessage == null || failMessage.isEmpty())
          {
            applicabilityTestOutputText.setText("Not available");
          }
          else
          {
            applicabilityTestOutputText.setText("Operation '" + node.getName()
                + "' is not applicable because:\n" + failMessage);
          }
        }
        else
        {
          applyBtn.setEnabled(true);
        }
      }
    }
    else
    {
      documentationBrowser.setText("");
    }
  }

  @Override
  public void init(IViewSite site) throws PartInitException
  {
    super.init(site);
    site.getPage().addSelectionListener(SampleView.ID, viewerInputUpdater);
  }

  @Override
  public void dispose()
  {
    getSite().getPage().removeSelectionListener(SampleView.ID,
        viewerInputUpdater);
    super.dispose();
  }

  protected void updateViewer(ISelection selection)
  {
    OperationsBrowserTreeNode root = new OperationsBrowserTreeNode();
    OperationsBrowserTreeBuilder builder = new OperationsBrowserTreeBuilder(
        root);
    builder.buildLibrary((IStructuredSelection) selection);
    operationsViewer.setInput(root);

    operationsViewer.expandAll();
  }

  @Override
  public void setFocus()
  {
    operationsViewer.getControl().setFocus();
  }

}
