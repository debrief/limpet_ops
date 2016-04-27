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
import samplemodel.SampleModelCollection;

/**
 * Ref: https://www.eclipse.org/forums/index.php/t/107919/
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

    getSite().setSelectionProvider(viewer);
  }

  private List<Object> createSampleData()
  {
    List<Object> res = new ArrayList<Object>();

    // singletons
    res.add(SampleModelCollection.wrap(12));
    res.add(SampleModelCollection.wrap("A String Object"));
    res.add(SampleModelCollection.wrap(133.4));
    res.add(SampleModelCollection.wrap("... another one"));
    res.add(new Date());
    res.add(SampleModelCollection.wrap(332.3));
    res.add(SampleModelCollection.wrap("Another string"));
    res.add(SampleModelCollection.wrap("smiles"));
    res.add(SampleModelCollection.wrap(1));
    res.add(SampleModelCollection.wrap(5));

    // collections
    res.add(SampleModelCollection.wrap(1, 2, 3, 4, 5));
    res.add(SampleModelCollection.wrap(1, 2, 3, 4, 6));
    res.add(SampleModelCollection.wrap(1, 2, 3));
    res.add(SampleModelCollection.wrap("a", "b", "c", "d", "e"));

    // timestamped
    res.add(SampleModelCollection.wrap(1.1, 2.2, 4.5, 3.0, 6.0).timestamp(0L,
        1000L, 2000L, 3000L, 4000L));
    res.add(SampleModelCollection.wrap(2.1, 3.2, 1.5, 1.0, 2.0).timestamp(0L,
        1000L, 2000L, 3000L, 4000L));
    res.add(SampleModelCollection.wrap(3.1, 2.0, 1.0).timestamp(0L, 3000L,
        4000L));
    res.add(SampleModelCollection.wrap(0.1, 4.2, 2.5, 2.0, 1.0).timestamp(0L,
        1000L, 2000L, 3000L, 5000L));
    res.add(SampleModelCollection.wrap(4.1, 1.2, 3.5, 4.0, 3.0, 6.0).timestamp(
        0L, 1000L, 2000L, 3000L, 4000L, 5000L));
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
    IStructuredSelection selection = (IStructuredSelection) viewer
        .getSelection();
    Shell shell = getSite().getShell();
    OperationsLibraryMenuBuilder menuBuilder = new OperationsLibraryMenuBuilder(
        manager, shell);
    menuBuilder.buildLibrary((IStructuredSelection) selection);
  }

}
