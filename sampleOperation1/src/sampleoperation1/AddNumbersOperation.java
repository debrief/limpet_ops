package sampleoperation1;

import java.util.Arrays;

import samplemodel.SampleModel;
import samplemodel.SampleModelCollection;
import samplemodel.SampleModelOperation;

public class AddNumbersOperation implements SampleModelOperation
{

  public Object[] execute(Object[] input)
  {
    Number sums[] = new Number[0];
    SampleModelCollection sm = null;
    for (Object nw : input)
    {
      sm = (SampleModelCollection) nw;
      int i = 0;
      for (Object data : sm.getData())
      {
        if (sums.length == 0)
        {
          sums = new Number[sm.getData().length];
          Arrays.fill(sums, 0d);
        }
        sums[i] =
            ((Number) sums[i]).doubleValue()
                + ((SampleModel) data).getNumber().doubleValue();
        i++;
      }
    }
    return new Object[]
    {SampleModelCollection.wrap(sums).timestamp(sm.getTimestamps())};
  }

}
