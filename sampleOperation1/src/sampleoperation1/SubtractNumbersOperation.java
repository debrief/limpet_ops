package sampleoperation1;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

public class SubtractNumbersOperation implements SampleModelOperation
{

  public Object[] execute(Object[] input)
  {
    SampleModel first = (SampleModel) input[0];
    SampleModel second = (SampleModel) input[1];
    int length = first.getData().length;

    Number results[] = new Number[length];

    for (int i = 0; i < length; i++)
    {
      results[i] =
          ((Number) first.getData()[i]).doubleValue()
              - ((Number) second.getData()[i]).doubleValue();
    }

    return new Object[]
    {new SampleModel(results)};
  }

}