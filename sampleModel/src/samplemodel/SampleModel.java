package samplemodel;

public class SampleModel
{
  private final Number number;
  private final String string;

  private SampleModel(Number number, String string)
  {
    this.number = number;
    this.string = string;
  }

  public SampleModel(Number number)
  {
    this(number, null);
  }

  public SampleModel(String string)
  {
    this(null, string);
  }

  public boolean isNumeric()
  {
    return number != null;
  }

  public Number getNumber()
  {
    return number;
  }

  public String getString()
  {
    return string;
  }

  @Override
  public String toString()
  {
    return isNumeric() ? number.toString() : getString();
  }

}
