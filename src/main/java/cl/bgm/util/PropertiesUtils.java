package cl.bgm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** Specific utils for handling {@link Properties} files. */
public interface PropertiesUtils {

  /**
   * Retrieves a {@link Properties} file from the resources/ directory.
   *
   * <p>The retrieved {@link Properties} file comes in the {@link StandardCharsets#UTF_8} encoding
   * by default, but you can directly request it in a determined charset using {@link
   * this#getFromResources(String, Charset)}
   *
   * @param path The path to the properties file relative to the resources/ directory.
   * @return The {@link Properties} loaded from resources, in its standard encoding, or <code>null
   *     </code> if not found.
   */
  static Properties getFromResources(String path) {
    return getFromResources(path, StandardCharsets.UTF_8);
  }

  static Properties getFromResources(String path, Charset charset) {
    final Properties properties = new Properties();
    final InputStream propertiesStream =
        FileUtils.getContextClassLoader().getResourceAsStream(path);
    if (propertiesStream == null) return null;

    try {
      properties.load(new InputStreamReader(propertiesStream, charset));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return properties;
  }

  static List<String> getResourceProperties(String path) {
    List<String> resourceFiles = FileUtils.getResourceFiles(path);
    List<String> propertiesFiles = new ArrayList<>();

    for (String resource : resourceFiles) {
      Properties properties = getFromResources(String.format("%s/%s", path, resource));
      if (properties == null) continue;

      propertiesFiles.add(resource);
    }

    return propertiesFiles;
  }
}
