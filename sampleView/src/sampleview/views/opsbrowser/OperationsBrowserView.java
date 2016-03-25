package sampleview.views.opsbrowser;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridLayout;
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

public class OperationsBrowserView extends ViewPart
{

  private TreeViewer operationsViewer;
  private Browser documentationBrowser;

  private ISelectionListener viewerInputUpdater = new ISelectionListener()
  {
    public void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
      updateViewer(selection);
    }
  };

  public OperationsBrowserView()
  {
  }

  @Override
  public void createPartControl(Composite parent)
  {
    parent.setLayout(new GridLayout(1, false));

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

    updateViewer(new StructuredSelection());
  }

  protected void updateDetails(IStructuredSelection selection)
  {
    if (!selection.isEmpty())
    {
      OperationsBrowserTreeNode node =
          (OperationsBrowserTreeNode) selection.getFirstElement();
      documentationBrowser.setText(node.getDocumentation());
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
