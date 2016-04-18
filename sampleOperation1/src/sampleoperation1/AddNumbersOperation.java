package sampleoperation1;

import java.util.Arrays;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

public class AddNumbersOperation implements SampleModelOperation
{

  public Object[] execute(Object[] input)
  {
    Number sums[] = new Number[0];
    for (Object nw : input)
    {
      SampleModel sm = (SampleModel) nw;
      int i = 0;      
      for (Object data : sm.getData())
      {
        if (sums.length == 0) {
          sums = new Number[sm.getData().length];
          Arrays.fill(sums, 0d);
        }                
        sums[i] = ((Number)sums[i]).doubleValue() + ((Number) data).doubleValue();
        i++;
      }
    }
    return new Object[]
    {new SampleModel(sums)};
  }

}
