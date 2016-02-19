package sampletest;

import junit.framework.Assert;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;

public class SampleTest
{
  @Test
  public void testModelOperations()
  {
    IConfigurationElement[] configurationElements =
        Platform.getExtensionRegistry().getConfigurationElementsFor(
            "sampleModel.SampleModelOperation");
    Assert.assertEquals(6, configurationElements.length);

    // TODO: test here different set of selections
  }
}