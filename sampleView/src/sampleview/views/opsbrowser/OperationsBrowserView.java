package sampleview.views.opsbrowser;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import samplemodel.SampleModelOperationRegistry;

public class OperationsBrowserView extends ViewPart
{

  private static class OperationsContentProvider implements ITreeContentProvider
  {

    @Override
    public void dispose()
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public Object[] getElements(Object inputElement)
    {
      return new String[]
      {"A"};
    }

    @Override
    public Object[] getChildren(Object parentElement)
    {
      return null;
    }

    @Override
    public Object getParent(Object element)
    {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
      // TODO Auto-generated method stub
      return false;
    }

  }

  private TreeViewer operationsViewer;

  public OperationsBrowserView()
  {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void createPartControl(Composite parent)
  {
    operationsViewer = new TreeViewer(parent);
    operationsViewer.setContentProvider(new OperationsContentProvider());
    operationsViewer.setLabelProvider(new LabelProvider());
//    operationsViewer.setInput(SampleModelOperationRegistry.INSTANCE.);
  }

  @Override
  public void setFocus()
  {
    operationsViewer.getControl().setFocus();
  }

}
