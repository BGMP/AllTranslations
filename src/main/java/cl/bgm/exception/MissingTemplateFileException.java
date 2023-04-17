package cl.bgm.exception;

/** Missing templates exception. */
public class MissingTemplateFileException extends Exception {

  public MissingTemplateFileException() {
    super("Missing template strings file!");
  }
}
