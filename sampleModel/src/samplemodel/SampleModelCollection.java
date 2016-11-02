package samplemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleModelCollection
{
  /**
   * The array is assumed to be homogeneous, i.e. is one array element is numeric and timestamped
   * (timestamp != null), then all elements are.
   */
  private final SampleModel[] data;

  public SampleModelCollection(SampleModel... data)
  {
    if (data.length == 0)
    {
      throw new IllegalArgumentException("Data cannot be empty");
    }
    this.data = data;
  }

  public SampleModel[] getData()
  {
    return data;
  }

  public boolean isSingleton()
  {
    return data.length == 1;
  }

  public boolean isNumeric()
  {
    return data[0].isNumeric();
  }

  public boolean isTimestamped()
  {
    return data[0].getTimestamp() != null;
  }

  public Number getSingletonNumber()
  {
    if (!isSingleton())
    {
      throw new IllegalStateException("Collection is not a singleton");
    }
    return data[0].getNumber();
  }

  public String getSingletonString()
  {
    if (!isSingleton())
    {
      throw new IllegalStateException("Collection is not a singleton");
    }
    return data[0].getString();
  }

  @SafeVarargs
  public static <T> SampleModelCollection wrap(T... values)
  {
    List<SampleModel> sm = new ArrayList<>();
    for (T v : values)
    {
      sm.add(new SampleModel(v));
    }
    return new SampleModelCollection((SampleModel[]) sm
        .toArray(new SampleModel[values.length]));
  }

  public SampleModelCollection timestamp(Long... timestamps)
  {
    if (data.length != timestamps.length)
    {
      throw new IllegalArgumentException(
          "The number of supplied values do not match the data to be timestamped");
    }
    int i = 0;
    for (SampleModel s : data)
    {
      s.setTimestamp(timestamps[i++]);
    }

    return this;
  }

  public Long[] getTimestamps()
  {
    Long[] timestamps = new Long[data.length];
    int i = 0;
    for (SampleModel sm : data)
    {
      timestamps[i++] = sm.getTimestamp();
    }
    return timestamps;
  }

  @Override
  public String toString()
  {
    return Arrays.toString(data);
  }
}
