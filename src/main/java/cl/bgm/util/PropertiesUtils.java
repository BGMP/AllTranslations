package cl.bgm.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
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
    Properties properties = null;

    try (ScanResult scanResult = new ClassGraph().scan()) {
      ResourceList resourcesWithPath = scanResult.getResourcesWithPath(path);
      Optional<Resource> match = resourcesWithPath.stream().findFirst();
      if (!match.isPresent()) return null;

      String content = match.get().getContentAsString();
      InputStream stream = new ByteArrayInputStream(content.getBytes(charset));

      properties = new Properties();
      properties.load(new InputStreamReader(stream));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return properties;
  }

  static List<String> getResourcePropertiesPaths(String path) {
    List<String> resourcePaths;
    try (ScanResult scanResult = new ClassGraph().acceptPaths(path).scan()) {
      resourcePaths = scanResult.getResourcesWithExtension("properties").getPaths();
    }

    return resourcePaths;
  }
}
