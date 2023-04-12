package cl.bgm.exception;

/** Missing locale exception. */
public class MissingTemplateFileException extends Exception {

  public MissingTemplateFileException() {
    super("Missing template strings file!");
  }
}
