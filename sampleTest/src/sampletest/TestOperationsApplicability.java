package sampletest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Test;

import junit.framework.Assert;
import samplemodel.SampleModel;
import samplemodel.SampleModelOperationRegistry;

/**
 * Makes sure the proper operations are contributed for a specific selection of object.
 */
public class TestOperationsApplicability
{

  @Test
  public void testSubtractNumbersOperation()
  {

    List<SampleModel> objects = new ArrayList<>();
    objects.add(new SampleModel(2));
    objects.add(new SampleModel(1));

    MockOperationLibraryBuilder builder = new MockOperationLibraryBuilder();
    StructuredSelection selection = new StructuredSelection(objects);
    builder.buildLibrary(selection, SampleModelOperationRegistry.INSTANCE
        .getOperationLibraryRoot());

    Assert.assertEquals("//Arithmetic operations//", builder
        .getOperationPath("sampleoperation1.SubtractNumbersOperation"));

    // negative test
    objects = new ArrayList<>();
    objects.add(new SampleModel(2));
    objects.add(new SampleModel(1));
    objects.add(new SampleModel("Test 123"));

    builder = new MockOperationLibraryBuilder();
    selection = new StructuredSelection(objects);
    builder.buildLibrary(selection, SampleModelOperationRegistry.INSTANCE
        .getOperationLibraryRoot());

    Assert.assertNull(builder.getOperationPath(
        "sampleoperation1.SubtractNumbersOperation"));

    // TODO: more negative tests here + can also assert the failure reason once it's in place

  }

  // TODO: rest of the operations

}