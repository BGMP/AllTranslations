package cl.bgm;

import cl.bgm.exception.MissingTemplateFileException;
import cl.bgm.util.PropertiesUtils;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * The main class for AllTranslations.
 *
 * <p>This class represents the translations core, and with it you can translate strings from one
 * language to another. Declaring or extending this class means you will have to implement {@link
 * this#getLocale(O)} and {@link this#setLocale(O, String)}, {@link O} being your end user object.
 *
 * <p>{@link AllTranslations} relies on several {@link Properties} files to represent each supported
 * locale. These files will be loaded from <code>resources/i18n</code> by default. The default file
 * for all template strings is <code>i18n/string.properties</code>.
 *
 * <p>New locales can be added by creating {@link Properties} files with a language code for name.
 * All language codes must follow Java {@link Locale} Language Tags conventions, which consist of a
 * <code>ISO 639-1</code> language code and an <code>ISO 3166-2</code> country code, separated by an
 * underscore or hyphen. (i.e: en_UK, en-US, es_cl, etc.).
 */
public abstract class AllTranslations<O> {
  private static final String I18N_DIRECTORY = "i18n";
  private static final String TEMPLATE_FILE = "strings.properties";
  private static final Locale TEMPLATE_LOCALE = new Locale("en", "US");

  private Properties templateFile;
  private Map<Locale, Properties> translationFilesMap = new HashMap<>();
  private Map<Locale, Map<String, String>> translationsMap = new HashMap<>();

  public AllTranslations() {
    // Set the context class loader for the thread in action. This way we get the right resource files.
    Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

    try {
      this.loadTemplateFile();
    } catch (MissingTemplateFileException e) {
      e.printStackTrace();
      System.out.println("Could not initialize AllTranslations! Missing template strings file.");
      System.out.println(
          "Make sure " + TEMPLATE_FILE + " is present in resources at " + I18N_DIRECTORY + "/.");
      return;
    }

    this.loadTranslationFiles();
    this.loadLocales();
  }

  private void loadTemplateFile() throws MissingTemplateFileException {
    Properties templateFile =
        PropertiesUtils.getFromResources(I18N_DIRECTORY + "/" + TEMPLATE_FILE);
    if (templateFile == null) throw new MissingTemplateFileException();

    this.templateFile = templateFile;
  }

  private void loadTranslationFiles() {
    for (String propertiesResourcePath :
        PropertiesUtils.getResourcePropertiesPaths(I18N_DIRECTORY)) {
      if (propertiesResourcePath.equals(TEMPLATE_FILE)) continue;

      Properties properties = PropertiesUtils.getFromResources(propertiesResourcePath);
      if (properties == null) continue;

      String languageTag =
          propertiesResourcePath.replaceAll(I18N_DIRECTORY + "/", "").replaceAll(".properties", "");
      Locale locale = Locale.forLanguageTag(languageTag.replaceAll("_", "-"));

      this.translationFilesMap.put(locale, properties);
    }
  }

  private void loadLocales() {
    for (Locale locale : this.translationFilesMap.keySet()) {
      Properties localeFile = this.translationFilesMap.get(locale);
      this.loadLocale(locale, localeFile);
    }

    // Always load our template
    this.loadLocale(TEMPLATE_LOCALE, templateFile);
  }

  private void loadLocale(Locale locale, Properties translationsFile) {
    Enumeration<?> keys = translationsFile.propertyNames();
    Map<String, String> translations = new HashMap<>();

    while (keys.hasMoreElements()) {
      String key = keys.nextElement().toString();
      translations.put(key, translationsFile.getProperty(key));
    }

    this.translationsMap.put(locale, translations);
  }

  public String get(String key, O object, Object... args) {
    return this.get(key, this.getLocale(object), args);
  }

  public String get(Translatable translatable, O object) {
    return this.get(translatable, this.getLocale(object));
  }

  public String get(Translatable translatable, Locale locale) {
    return this.get(translatable.getKey(), locale, translatable.getArgs());
  }

  public String get(String key, String locale, Object... args) {
    return this.get(key, Locale.forLanguageTag(locale.replaceAll("_", "-")), args);
  }

  public String get(String key, Locale locale, Object... args) {
    String translated = null;
    Map<String, String> translations = this.translationsMap.get(locale);

    if (translations != null) {
      String translation = translations.get(key);
      if (translation != null) {
        translated = translation;
      }
    }

    // If no translation is found, we try to return from the template strings
    if (translated == null && locale != TEMPLATE_LOCALE) {
      translated = this.get(key, TEMPLATE_LOCALE, args);
    }

    if (translated != null && args != null) {
      translated = this.replaceArgs(translated, locale, args);
    }

    return translated;
  }

  private String replaceArgs(String translated, Locale locale, Object... args) {
    String replacement;
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Translatable) {
        replacement = this.get((Translatable) args[i], locale);
      } else {
        replacement = String.valueOf(args[i]);
      }

      translated = translated.replace("{" + i + "}", replacement);
    }

    return translated;
  }

  public void setLocale(O object, String locale) {
    this.setLocale(object, Locale.forLanguageTag(locale.replaceAll("_", "-")));
  }

  /** Get the locale string of your end user. */
  public abstract Locale getLocale(O object);

  /** Set the locale string of your end user. */
  public abstract void setLocale(O object, Locale locale);
}
