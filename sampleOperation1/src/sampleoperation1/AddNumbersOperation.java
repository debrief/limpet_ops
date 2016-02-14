package sampleoperation1;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

public class AddNumbersOperation implements SampleModelOperation
{

  public Object[] execute(Object[] input)
  {
    double sum = 0;
    for (Object nw : input)
    {
      sum += ((SampleModel) nw).getNumber().doubleValue();
    }
    return new Object[]
    {new SampleModel(sum)};
  }

}
