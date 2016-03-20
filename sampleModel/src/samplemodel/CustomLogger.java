package samplemodel;

public interface CustomLogger
{
      void log(String message);

  void pushNode();

  void popNode();

  LoggerNode getRootNode();
}
