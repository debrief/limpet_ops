package sampleview.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperationRegistry;

/**
 * Ref: https://www.eclipse.org/forums/index.php/t/107919/
 * 
 * @author dinko.ivanov@gepardsoft.com
 * 
 */
public class SampleView extends ViewPart
{

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "sampleview.views.SampleView";

  private ListViewer viewer;

  private List<Object> myData;

  /**
   * The constructor.
   */
  public SampleView()
  {
  }

  /**
   * This is a callback that will allow us to create the viewer and initialize it.
   */
  public void createPartControl(Composite parent)
  {
    viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(new ArrayContentProvider());

    myData = createSampleData();
    viewer.setInput(myData);

    hookContextMenu();
  }

  private List<Object> createSampleData()
  {
    List<Object> res = new ArrayList<Object>();

    res.add(new SampleModel(12));
    res.add(new SampleModel("A String Object"));
    res.add(new SampleModel(133.4));
    res.add(new SampleModel("... another one"));
    res.add(new Date());
    res.add(new SampleModel(332.3));
    res.add(new SampleModel("Another string"));
    res.add(new SampleModel("smiles"));
    res.add(new SampleModel(1));
    res.add(new SampleModel(5));

    return res;
  }

  private void hookContextMenu()
  {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        buildContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus()
  {
    viewer.getControl().setFocus();
  }

  private void buildContextMenu(IMenuManager manager)
  {
    IStructuredSelection selection =
        (IStructuredSelection) viewer.getSelection();
    Shell shell = getSite().getShell();

    OperationLibraryMenuBuilder menuBuilder =
        new OperationLibraryMenuBuilder(manager, shell);

    SampleModelOperationRegistry.INSTANCE.buildLibrary(selection, menuBuilder);
  }

}
