package cl.bgm;

/** Represents a translatable key with all its arguments */
public class Translatable {
  private final String key;
  private final Object[] args;

  private Translatable(String key, Object... args) {
    this.key = key;
    this.args = args;
  }

  public static Translatable of(String key, Object... args) {
    return new Translatable(key, args);
  }

  public String getKey() {
    return key;
  }

  public Object[] getArgs() {
    return args;
  }
}
