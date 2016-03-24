package samplemodel;

import org.eclipse.core.runtime.IConfigurationElement;

public interface IOperationsBrowserLibraryBuilder extends
    IOperationLibraryBuilder
{
      void buildOperationNode(Object[] selection, String operationName,
          IConfigurationElement operationDescriptor, String failMessage);
}
