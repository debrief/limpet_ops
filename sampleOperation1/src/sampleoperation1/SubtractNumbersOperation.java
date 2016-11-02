package sampleoperation1;

import samplemodel.SampleModel;
import samplemodel.SampleModelCollection;
import samplemodel.SampleModelOperation;

public class SubtractNumbersOperation implements SampleModelOperation
{

  public Object[] execute(Object[] input)
  {
    SampleModelCollection first = (SampleModelCollection) input[0];
    SampleModelCollection second = (SampleModelCollection) input[1];
    int length = first.getData().length;

    Number results[] = new Number[length];

    for (int i = 0; i < length; i++)
    {
      results[i] =
          ((SampleModel) first.getData()[i]).getNumber().doubleValue()
              - ((SampleModel) second.getData()[i]).getNumber().doubleValue();
    }

    return new Object[]
    {SampleModelCollection.wrap(results)};
  }

}