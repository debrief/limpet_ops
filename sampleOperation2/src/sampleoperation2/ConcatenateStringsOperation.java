package sampleoperation2;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

public class ConcatenateStringsOperation implements SampleModelOperation
{

  public Object execute(Object[] input)
  {
    StringBuilder sb = new StringBuilder();
    for (Object nw : input)
    {
      sb.append(((SampleModel) nw).getString());
    }
    return new SampleModel(sb.toString());
  }

}
