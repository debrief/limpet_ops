package sampleview.views;

import java.util.Date;

import org.eclipse.core.expressions.ElementHandler;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

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

  private IConfigurationElement[] configurationElements;

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
    viewer.setInput(createSampleData());

    hookContextMenu();
  }

  private Object[] createSampleData()
  {
    return new Object[]
    {new SampleModel(12), new SampleModel("A String Object"),
        new SampleModel(133.4), new SampleModel("... another one"), new Date(),
        new SampleModel(332.3), new SampleModel("Another string"),
        new SampleModel("smiles"), new SampleModel(1), new SampleModel(5)};
  }

  private void hookContextMenu()
  {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        SampleView.this.fillContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void fillContextMenu(IMenuManager manager)
  {

    IConfigurationElement[] configurationElements = getContributedOperations();

    final IStructuredSelection selection =
        (IStructuredSelection) viewer.getSelection();
    EvaluationContext context = new EvaluationContext(null, selection);
    
    final Object[] items = selection.toArray();

    for (IConfigurationElement configElement : configurationElements)
    {
      boolean applicable = readApplicable(configElement, context);

      if (applicable)
      {
        final String name = configElement.getAttribute("name");
        final IConfigurationElement cfg = configElement;
        manager.add(new Action(name)
        {
          @Override
          public void run()
          {
            try
            {
              SampleModelOperation operation =
                  (SampleModelOperation) cfg.createExecutableExtension("class");
			Object result = operation.execute(items);
              MessageDialog.openInformation(getSite().getShell(), name, name
                  + " operation result is " + result.toString());

            }
            catch (CoreException e)
            {
              e.printStackTrace();
            }
          }
        });
      }
    }

  }

  private boolean readApplicable(IConfigurationElement configElement,
      IEvaluationContext evaluationContext)
  {
    boolean applicable = true;

    IConfigurationElement[] children = configElement.getChildren("applicable");

    final ElementHandler elementHandler = ElementHandler.getDefault();
    final ExpressionConverter converter = ExpressionConverter.getDefault();

    if (children.length > 0)
    {
      IConfigurationElement applicableElement = children[0];

      final IConfigurationElement[] expressionElements =
          applicableElement.getChildren();
      if (expressionElements.length > 0)
      {

        final IConfigurationElement expressionElement = expressionElements[0];

        try
        {
          Expression applicableExpression =
              elementHandler.create(converter, expressionElement);
          applicable =
              applicableExpression.evaluate(evaluationContext).equals(
                  EvaluationResult.TRUE);
        }
        catch (CoreException e)
        {
          e.printStackTrace();
        }
      }

    }
    return applicable;
  }

  private IConfigurationElement[] getContributedOperations()
  {
    if (configurationElements == null)
    {
      // lazy initialization
      configurationElements =
          Platform.getExtensionRegistry().getConfigurationElementsFor(
              "sampleModel.SampleModelOperation");
    }
    return configurationElements;
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus()
  {
    viewer.getControl().setFocus();
  }

}
