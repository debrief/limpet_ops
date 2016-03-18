package samplemodel;

public interface CustomLogger
{
  void log(String message);
  void pushIndent();
  void popIndent();
}
