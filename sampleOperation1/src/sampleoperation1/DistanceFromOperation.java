package sampleoperation1;

import samplemodel.SampleModel;
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

    SampleModel first = (SampleModel) input[0];
    for (int i = 0; i < result.length; i++)
    {
      SampleModel other = (SampleModel) input[i+1];
      Number[] distances = new Number[first.getData().length];
      for (int j = 0; j < first.getData().length; j++)
      {
        distances[j] =
            ((Number) other.getData()[j]).doubleValue()
                - ((Number) first.getData()[j]).doubleValue();
      }
      result[i] = new SampleModel(distances);
    }
    return result;
  }

}