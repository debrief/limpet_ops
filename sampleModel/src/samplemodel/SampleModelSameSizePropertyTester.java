package samplemodel;

import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Tests if all {@link SampleModel} objects within a {@link StructuredSelection} are vectors, that
 * is data.length > 1;
 * 
 */
public class SampleModelSameSizePropertyTester extends PropertyTester
{

  public SampleModelSameSizePropertyTester()
  {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args,
      Object expectedValue)
  {
    int size = -1;
    StructuredSelection selection = (StructuredSelection) receiver;
    for (Iterator selectionIterator = selection.iterator(); selectionIterator
        .hasNext();)
    {
      SampleModelCollection next =
          (SampleModelCollection) selectionIterator.next();
      int length = next.getData().length;

      if (size == -1)
      {
        size = length;
      }
      else if (size != length)
      {
        return false;
      }
    }
    return true;
  }

}
