package samplemodel;

import java.util.Arrays;

public class SampleModel
{

  private final Object[] data;

  public SampleModel(Object... data)
  {
    if (data.length == 0)
    {
      throw new IllegalArgumentException("Data cannot be empty");
    }
    this.data = data;
  }

  public boolean isNumeric()
  {
    return data[0] instanceof Number;
  }

  public boolean isArray()
  {
    return data.length > 1;
  }

  public Number getNumber()
  {
    return (Number) data[0];
  }

  public String getString()
  {
    return (String) data[0];
  }

  public Object[] getData()
  {
    return data;
  }

  @Override
  public String toString()
  {
    return isArray() ? Arrays.toString(data) : data[0].toString();
  }

}