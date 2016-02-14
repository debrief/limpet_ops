package sampleoperation1;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

public class SubtractNumbersOperation implements SampleModelOperation
{

  public Object[] execute(Object[] input)
  {
    SampleModel first = (SampleModel) input[0];
    SampleModel second = (SampleModel) input[1];
    SampleModel result = new SampleModel(first.getNumber().doubleValue()
        - second.getNumber().doubleValue());
    return new Object[]
    {result};
  }

}