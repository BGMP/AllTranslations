package cl.bgm.exception;

/** Missing template file exception. */
public class MissingTemplateFileException extends Exception {

  public MissingTemplateFileException() {
    super("Missing template strings file!");
  }
}
