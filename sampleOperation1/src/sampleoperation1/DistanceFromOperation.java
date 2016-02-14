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

    double first = ((SampleModel) input[0]).getNumber().doubleValue();
    for (int i = 0; i < result.length; i++)
    {
      double val = ((SampleModel) input[i + 1]).getNumber().doubleValue();
      result[i] = new SampleModel(val - first);
    }
    return result;
  }

}
