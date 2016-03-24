package sampleview.views.opsbrowser;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import samplemodel.SampleModelOperationRegistry;
import sampleview.views.SampleView;

public class OperationsBrowserView extends ViewPart
{

  private static class OperationsLabelProvider extends LabelProvider
  {
    @Override
    public String getText(Object element)
    {
      return ((OperationsBrowserTreeNode) element).getName();
    }

    @Override
    public Image getImage(Object element)
    {
      if (element instanceof OperationsBrowserOpNode)
      {
        return PlatformUI.getWorkbench().getSharedImages().getImage(
            ISharedImages.IMG_OBJ_ELEMENT);
      }
      return PlatformUI.getWorkbench().getSharedImages().getImage(
          ISharedImages.IMG_OBJ_FOLDER);
    }
  }

  private static class OperationsContentProvider implements
      ITreeContentProvider
  {

    @Override
    public void dispose()
    {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public Object[] getElements(Object inputElement)
    {
      return ((OperationsBrowserTreeNode) inputElement).getChildren();
    }

    @Override
    public Object[] getChildren(Object parentElement)
    {
      return ((OperationsBrowserTreeNode) parentElement).getChildren();
    }

    @Override
    public Object getParent(Object element)
    {
      return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
      return getChildren(element).length > 0;
    }

  }

  private TreeViewer operationsViewer;

  private ISelectionListener viewerInputUpdater = new ISelectionListener()
  {

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
      updateViewer(selection);
    }
  };

  public OperationsBrowserView()
  {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void createPartControl(Composite parent)
  {
    operationsViewer = new TreeViewer(parent);
    operationsViewer.setContentProvider(new OperationsContentProvider());
    operationsViewer.setLabelProvider(new OperationsLabelProvider());
    
    updateViewer(new StructuredSelection());
  }

  @Override
  public void init(IViewSite site) throws PartInitException
  {
    super.init(site);
    site.getPage().addSelectionListener(SampleView.ID,
        viewerInputUpdater);
  }

  @Override
  public void dispose()
  {
    getSite().getPage()
        .removeSelectionListener(SampleView.ID, viewerInputUpdater);
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
  }

  @Override
  public void setFocus()
  {
    operationsViewer.getControl().setFocus();
  }

}
