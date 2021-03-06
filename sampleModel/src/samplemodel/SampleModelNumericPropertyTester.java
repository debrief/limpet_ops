package samplemodel;

import org.eclipse.core.expressions.PropertyTester;

public class SampleModelNumericPropertyTester extends PropertyTester
{

  public SampleModelNumericPropertyTester()
  {
  }

  @Override
  public boolean test(Object receiver, String property, Object[] args,
      Object expectedValue)
  {
    SampleModelCollection model = (SampleModelCollection) receiver;
    return model.isNumeric() == (Boolean) expectedValue;
  }

}
