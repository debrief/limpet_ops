package sampleview.views.opsbrowser;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

class OperationsContentProvider implements
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