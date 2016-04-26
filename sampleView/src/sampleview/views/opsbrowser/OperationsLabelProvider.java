package sampleview.views.opsbrowser;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class OperationsLabelProvider extends LabelProvider
{

  private Image disabledOperationImage;
  private Image disabledOperationGroupImage;

  @Override
  public String getText(Object element)
  {
    return ((OperationsBrowserTreeNode) element).getName();
  }

  @SuppressWarnings("deprecation")
  @Override
  public Image getImage(Object element)
  {

    OperationsBrowserTreeNode node = (OperationsBrowserTreeNode) element;

    if (node.isOperationGroup())
    {
      return node.isApplicable() ? PlatformUI.getWorkbench().getSharedImages()
          .getImage(ISharedImages.IMG_OBJ_PROJECT)
          : getDisabledOperationGroupImage();
    }
    else if (node instanceof OperationsBrowserOpNode)
    {
      OperationsBrowserOpNode opNode = (OperationsBrowserOpNode) node;
      return opNode.isApplicable() ? PlatformUI.getWorkbench()
          .getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT)
          : getDisabledOperationImage();

    }
    return PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_OBJ_FOLDER);
  }

  private Image getDisabledOperationImage()
  {
    if (disabledOperationImage == null)
    {
      disabledOperationImage =
          new Image(Display.getDefault(), PlatformUI.getWorkbench()
              .getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT),
              SWT.IMAGE_DISABLE);
    }
    return disabledOperationImage;
  }

  @SuppressWarnings("deprecation")
  private Image getDisabledOperationGroupImage()
  {
    if (disabledOperationGroupImage == null)
    {
      disabledOperationGroupImage =
          new Image(Display.getDefault(), PlatformUI.getWorkbench()
              .getSharedImages().getImage(ISharedImages.IMG_OBJ_PROJECT),
              SWT.IMAGE_DISABLE);
    }
    return disabledOperationGroupImage;
  }
}