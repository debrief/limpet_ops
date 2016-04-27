package sampleoperation1;

import samplemodel.SampleModel;
import samplemodel.SampleModelCollection;
import samplemodel.SampleModelOperation;

public class DistanceFromOperation implements SampleModelOperation
{

  /**
   * Calculates the difference between the first object in the input and the rest of the input
   * objects. Produces input.length-1 number of output objects.
   */
  public Object[] execute(Object[] input)
  {
    Object[] result = new Object[input.length - 1];

    SampleModelCollection first = (SampleModelCollection) input[0];
    for (int i = 0; i < result.length; i++)
    {
      SampleModelCollection other = (SampleModelCollection) input[i + 1];
      Number[] distances = new Number[first.getData().length];
      for (int j = 0; j < first.getData().length; j++)
      {
        distances[j] =
            ((SampleModel) other.getData()[j]).getNumber().doubleValue()
                - ((SampleModel) first.getData()[j]).getNumber().doubleValue();
      }
      result[i] = SampleModelCollection.wrap(distances);
    }
    return result;
  }

}