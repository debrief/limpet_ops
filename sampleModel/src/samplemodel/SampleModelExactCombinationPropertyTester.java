package samplemodel;

import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.StructuredSelection;

public class SampleModelExactCombinationPropertyTester extends PropertyTester
{

  public SampleModelExactCombinationPropertyTester()
  {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args,
      Object expectedValue)
  {

    boolean testOK = false;

    StructuredSelection selection = (StructuredSelection) receiver;

    // this logic can be made generic for X numbers and Y strings
    if ("2numbers1string".equals(property) && selection.size() == 3)
    {
      int nCount = 0;
      int sCount = 0;
      for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();)
      {
        Object o = iterator.next();
        if (o instanceof SampleModel)
        {
          SampleModel m = (SampleModel) o;
          
          if (!m.isArray()) {
            if (m.isNumeric())
            {
              nCount++;
            }
            else
            {
              sCount++;
            }
          } else {
            break;
          }
          
          if (nCount > 2 || sCount > 1)
          {
            break;
          }
        }
      }
      testOK = nCount == 2 && sCount == 1;
    }

    return testOK;
  }

}
