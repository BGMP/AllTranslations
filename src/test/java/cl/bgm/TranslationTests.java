package cl.bgm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TranslationTests {
  private AllTranslations<Object> translations;

  @BeforeEach
  public void setUp() {
    this.translations =
        new AllTranslations<Object>() {
          @Override
          public Locale getLocale(Object o) {
            return null;
          }

          @Override
          public void setLocale(Object o, Locale locale) {}
        };
  }

  @Test
  @DisplayName("Load translation files using language tags.")
  public void testLanguageTagsNaming() {
    String translation = translations.get("test.hello", "es-es");
    assertEquals("Hola", translation);

    translation = translations.get("test.hello", "es_es");
    assertEquals("Hola", translation);
  }

  @Test
  @DisplayName("Get a translation by its key and translate it.")
  public void testNormalTranslations() {
    String translation = translations.get("test.hello", Locale.forLanguageTag("es-es"));
    assertEquals("Hola", translation);
  }

  @Test
  @DisplayName("Pass and replace arguments into a translation.")
  public void testTranslationArguments() {
    String translation = translations.get("test.arguments", Locale.forLanguageTag("es-es"), 2);
    assertEquals("Hay 2 manzanas", translation);
  }

  @Test
  @DisplayName("Pass and replace a nested string into a translation.")
  public void testNestedTranslations() {
    String translation =
        translations.get(
            "test.nested.translations",
            Locale.forLanguageTag("es-es"),
            2,
            Translatable.of("test.nested.translations.liters"));
    assertEquals("El volumen es 2 litros", translation);
  }

  @Test
  @DisplayName("Get translation from a missing key.")
  public void testMissingTranslations() {
    String translation =
        translations.get("test.missing.translation", Locale.forLanguageTag("es-es"));
    assertEquals("I am missing in es_es.properties", translation);
  }

  @Test
  @DisplayName("Get translation from a missing locale file.")
  public void testMissingTranslationLocale() {
    String translation = translations.get("test.hello", "invalid locale code");
    assertEquals("Hello", translation);
  }

  @Test
  @DisplayName("Get translation from an invalid key.")
  public void testInvalidTranslationKey() {
    String translation = translations.get("efknreiwhfbedo.dbfi", Locale.forLanguageTag("es-es"));
    assertNull(translation);
  }

  @Test
  @DisplayName("Get UTF-8 character from a translation.")
  public void testTranslationsUTF8() {
    String translation = translations.get("test.utf8.translation", Locale.forLanguageTag("es-es"));
    assertEquals("canción", translation);
  }
}
