package sampleview.views.opsbrowser;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class OperationsLabelProvider extends LabelProvider
{

  private Image disabledImage;

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
      OperationsBrowserOpNode opNode = (OperationsBrowserOpNode) element;
      return opNode.isApplicable() ? PlatformUI.getWorkbench()
          .getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT)
          : getDisabledImage();

    }
    return PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_OBJ_FOLDER);
  }

  private Image getDisabledImage()
  {
    if (disabledImage == null)
    {
      disabledImage =
          new Image(Display.getDefault(), PlatformUI.getWorkbench()
              .getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT),
              SWT.IMAGE_DISABLE);
    }
    return disabledImage;
  }
}