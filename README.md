# AllTranslations [![build](https://github.com/BGMP/AllTranslations/actions/workflows/build.yml/badge.svg)](https://github.com/BGMP/AllTranslations/actions/workflows/build.yml) [![test](https://github.com/BGMP/AllTranslations/actions/workflows/test.yml/badge.svg)](https://github.com/BGMP/AllTranslations/actions/workflows/test.yml)

<img align="right" width="170" height="170" src="https://user-images.githubusercontent.com/26081543/232375880-516c6c17-bf49-4463-92ed-43dd1b4f7647.png" alt="">

**AllTranslations** is a very simple localisation framework for your Java projects!

Ever wanted to add support for multiple languages to your Java applications without having to introduce a ton of
complexity to your codebase? Well, AllTranslations might just be what you were looking for!

This framework offers a very simplistic approach to handling localisation via [.properties](https://en.wikipedia.org/wiki/.properties)
files, and supports all [ISO 639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes#Table_of_all_possible_two_letter_codes) and
[ISO 3166-2](https://en.wikipedia.org/wiki/ISO_3166-2) recognised combinations.

Table of Contents
===
* [Installation](#installation)
  * [Maven](#maven)
* [Structure](#structure)
* [Usage](#usage)
* [Translation Arguments](#translation-arguments)
* [Nested Translations](#nested-translations)
* [Missing Translations](#missing-translations)

## Installation
### Maven

```xml
<repository>
  <id>github</id>
  <url>https://maven.pkg.github.com/BGMP/AllTranslations</url>
</repository>
```

```xml
<dependency>
  <groupId>cl.bgm</groupId>
  <artifactId>AllTranslations</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Structure
Once you have installed AllTranslations, create a new directory named `i18n/` within your `resources/` folder. Here
is where all the locale files will live in, along with the template strings file.

Here is how the structure should look like in your project:
```
resources/
└── i18n/
    ├── en_uk.properties (locale)
    ├── es_es.properties (locale)
    ├── strings.properties (templates)
    └── ge.properties (locale)
```

The `strings.properties` file will contain all your base strings (English, US), and the rest of properties files
represent all the available languages for your project. The name of these files consists of a language code, and a
country code (if required). Both of these codes follow ISO conventions:
  * Language Codes: [ISO 3166-2](https://en.wikipedia.org/wiki/ISO_3166-2)
  * Country Codes: [ISO 639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)

## Usage
AllTranslations will require you to have a basic object designed to represent your end user or client you wish to
offer translations for. In this example, a simple `User` object has been created as follows:

```java
public class User {
  private String name;
  private String locale;

  public User(String name, String locale) {
    this.name = name;
    this.locale = locale;
  }

  public String getName() {
    return name;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) { 
    this.locale = locale;
  }
}
```

Then, create a class which will serve all of your translations, extending AllTranslations. The generic object you
provide to AllTranslations will be that of your end user as explained above. Following the `User` example, it would
look as follows:
```java
import java.util.Locale;

public class Translations extends AllTranslations<User> {
  @Override
  public Locale getLocale(User user) {
    return Locale.forLanguageTag(user.getLocale());
  }

  @Override
  public void setLocale(User user, Locale locale) {
    user.setLocale(locale.toLanguageTag());
  }
}
```

Create your locale files...
```properties
# resources/i18n/strings.properties
main.welcome = Welcome, {0}!
```

```properties
# resources/i18n/es_es.properties
main.welcome = Bienvenido, {0}!
```

Finally, use the framework! Here's a very basic example of how to use AllTranslations:

```java
import java.util.Locale;

public class AllTranslationsExample {
  public static void main(String[] args) {
    Translations translations = new Translations();

    User user1 = new User("Steve", "en_us");
    User user2 = new User("José", "es_es");

    System.out.println("To user1: " + translations.get("main.welcome", user1, user1.getName()));
    System.out.println("To user2: " + translations.get("main.welcome", user2, user2.getName()));
  }
}
```

Output:
```
To user1: Welcome, Steve!
To user2: Benvenido, José!
```

## Translation Arguments
As you may have noticed in the example presented above, AllTranslations supports translation arguments by default.
At your locale file, simply name them `{0}, {1}, {2}`, etc. respectively, and then you may trail everything you want to
replace them with using the `AllTranslations#get()` method.

For example, given the string `main.arguments = Their names are {0} and {1}.`, you may replace the arguments `{0}` and
`{1}` as follows:
```java
public class AllTranslationsExample {
  public static void main(String[] args) {
    Translations translations = new Translations();
    String translation = translations.get("main.arguments", "en_us", "Steve", "José");
    System.out.println(translation);
  }
}
```

Output:
```
Their names are Steve and José.
```

## Nested Translations
Translations may also be nested by using the `Translatable` object. Simply pass it as any other argument to the
`Translations#get()` method:

```properties
nested.string = You have {0} {1}.
nested.liter = liter
nested.liters = liters
```

```java
import cl.bgm.Translatable;

public class AllTranslationsExample {
  public static void main(String[] args) {
    Translations translations = new Translations();

    int liters = Math.random() * 10;
    String translation = translations.get("nested.string", "en_us", liters, liters == 1 ? Translatable.of("nested.liter") : Translatable.of("nested.liters"));

    System.out.println(translation);
  }
}
```

`Translatable#of()` also allows trailing arguments to be passed in, so you can infinitely nest translations! 

## Missing Translations
As in any other translations project, there will be instances where a translation will not be available just yet in one
language or another. In these cases, AllTranslations will default to your template strings in case it doesn't find a
suitable translation in a specific locale file.

On the other hand, if you request a translation from a locale which doesn't have a corresponding file within the `i18n/`
directory, AllTranslations will also default the string key to your template strings.

In case the string key you request isn't available in any locale file, or the template strings, the return value for
`Translations#get()` will be `null`.
