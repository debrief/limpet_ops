package samplemodel;

public class SampleModel
{

  private final Object data;
  /**
   * <code>null</code> means no timestamp
   */
  private Long timestamp;

  public SampleModel(Object data)
  {
    this.data = data;
  }

  public boolean isNumeric()
  {
    return data instanceof Number;
  }

  public Number getNumber()
  {
    return (Number) data;
  }

  public String getString()
  {
    return (String) data;
  }

  public Long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Long timestamp)
  {
    this.timestamp = timestamp;
  }

  @Override
  public String toString()
  {
    return getTimestamp() != null ? "[" + data.toString() + ", " + timestamp
        + "]" : data.toString();
  }

}