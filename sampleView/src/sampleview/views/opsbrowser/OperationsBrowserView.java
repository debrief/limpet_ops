package sampleview.views.opsbrowser;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import samplemodel.SampleModelOperationRegistry;
import sampleview.views.SampleView;

/**
 * Things that might be improved:
 * <ul>
 * <li>Use horizontal splitters to separate the viewer/browser/text control and allow re-adjusting
 * the size
 * <li>Present the Applicability Test Output as a tree
 * <li>Avoid expand all, try to preserve the selection (only keep the current node expanded)
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
      return hasApplicableChild(node)
          || node instanceof OperationsBrowserOpNode
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

    operationsViewer = new TreeViewer(parent, SWT.SINGLE);
    operationsViewer.setContentProvider(new OperationsContentProvider());
    operationsViewer.setLabelProvider(new OperationsLabelProvider());
    operationsViewer
        .addSelectionChangedListener(new ISelectionChangedListener()
        {

          @Override
          public void selectionChanged(SelectionChangedEvent event)
          {
            updateDetails((IStructuredSelection) event.getSelection());
          }
        });

    GridDataFactory.fillDefaults().grab(true, true).applyTo(
        operationsViewer.getControl());

    new Label(parent, SWT.NONE).setText("Documentation:");

    documentationBrowser = new Browser(parent, SWT.NONE);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(
        documentationBrowser);

    new Label(parent, SWT.NONE).setText("Applicability Test Output:");
    applicabilityTestOutputText = new Text(parent, SWT.V_SCROLL | SWT.H_SCROLL);
    applicabilityTestOutputText.setEditable(false);
    applicabilityTestOutputText.setBackground(parent.getShell().getDisplay()
        .getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    GridDataFactory.fillDefaults().grab(true, true).applyTo(
        applicabilityTestOutputText);

    updateViewer(new StructuredSelection());
    filterSelector.select(0);
  }

  protected void updateDetails(IStructuredSelection selection)
  {
    if (!selection.isEmpty())
    {
      OperationsBrowserTreeNode node =
          (OperationsBrowserTreeNode) selection.getFirstElement();
      documentationBrowser.setText(node.getDocumentation());

      if (node instanceof OperationsBrowserOpNode)
      {
        String failMessage = ((OperationsBrowserOpNode) node).getFailMessage();

        if (failMessage == null || failMessage.isEmpty())
        {
          applicabilityTestOutputText.setText("Not available");
        }
        else
        {
          applicabilityTestOutputText.setText("Operation '" + node.getName()
              + "' failed because of:\n" + failMessage);
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
    OperationsBrowserTreeNode root =
        new OperationsBrowserTreeNode("_ROOT_", null);
    OperationsBrowserTreeBuilder builder =
        new OperationsBrowserTreeBuilder(root);
    SampleModelOperationRegistry.INSTANCE.buildLibrary(
        ((IStructuredSelection) selection), builder);
    operationsViewer.setInput(root);

    operationsViewer.expandAll();
  }

  @Override
  public void setFocus()
  {
    operationsViewer.getControl().setFocus();
  }

}
