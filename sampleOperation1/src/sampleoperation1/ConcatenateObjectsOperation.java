package sampleoperation1;

import samplemodel.SampleModel;
import samplemodel.SampleModelOperation;

public class ConcatenateObjectsOperation implements SampleModelOperation
{

  public ConcatenateObjectsOperation()
  {
  }

  @Override
  public Object execute(Object[] input)
  {

    StringBuilder sb = new StringBuilder();
    for (Object o : input)
    {
      sb.append(o.toString());
    }
    return new SampleModel(sb.toString());
  }

}
