package sampleview.views;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import samplemodel.NoInputPermutations;
import samplemodel.OperationInputPermutator;
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
        SampleView.this.fillContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void fillContextMenu(final IMenuManager manager)
  {

    final IConfigurationElement[] operations = getContributedOperations();

    final IStructuredSelection selection = (IStructuredSelection) viewer
        .getSelection();
    EvaluationContext context = new EvaluationContext(null, selection);

    for (IConfigurationElement operation : operations)
    {
      boolean applicable = isApplicable(operation, context);

      if (applicable)
      {
        final List<Object[]> inputPermutations = getInputPermutations(selection,
            operation);
        final String name = operation.getAttribute("name");
        for (Object[] inputPermutation : inputPermutations)
        {
          String operationName = MessageFormat.format(name, inputPermutation);
          Action action = createOperationAction(inputPermutation, operationName,
              operation);
          manager.add(action);
        }

      }
    }

  }

  private List<Object[]> getInputPermutations(
      final IStructuredSelection selection, IConfigurationElement operation)
  {
    // by default assume commutative operation
    OperationInputPermutator inputPermutator = new NoInputPermutations();
    if (operation.getAttribute("inputPermutator") != null)
    {
      // a non-commutative operation, i.e. a+b=b+a
      try
      {
        inputPermutator = (OperationInputPermutator) operation
            .createExecutableExtension("inputPermutator");
      }
      catch (CoreException e)
      {
        e.printStackTrace();
      }
    }
    return inputPermutator.getOperationInputPermutations(selection.toArray());
  }

  private Action createOperationAction(final Object[] selection,
      final String operationName,
      final IConfigurationElement operationDescriptor)
  {
    return new Action(operationName)
    {
      @Override
      public void run()
      {
        try
        {
          SampleModelOperation operation =
              (SampleModelOperation) operationDescriptor
                  .createExecutableExtension("class");
          Object[] result = operation.execute(selection);
          MessageDialog.openInformation(getSite().getShell(), operationName,
              operationName + " operation result is " + Arrays.toString(
                  result));
        }
        catch (CoreException e)
        {
          e.printStackTrace();
        }
      }
    };
  }

  private boolean isApplicable(IConfigurationElement configElement,
      IEvaluationContext evaluationContext)
  {
    boolean applicable = true;

    IConfigurationElement[] children = configElement.getChildren("applicable");

    final ElementHandler elementHandler = ElementHandler.getDefault();
    final ExpressionConverter converter = ExpressionConverter.getDefault();

    if (children.length > 0)
    {
      IConfigurationElement applicableElement = children[0];

      final IConfigurationElement[] expressionElements = applicableElement
          .getChildren();
      if (expressionElements.length > 0)
      {

        final IConfigurationElement expressionElement = expressionElements[0];

        try
        {
          Expression applicableExpression = elementHandler.create(converter,
              expressionElement);
          applicable = applicableExpression.evaluate(evaluationContext).equals(
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
      configurationElements = Platform.getExtensionRegistry()
          .getConfigurationElementsFor("sampleModel.SampleModelOperation");
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
