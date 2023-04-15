package cl.bgm.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.io.InputStream;
import java.util.List;

/** File utils. */
public interface FileUtils {

  /**
   * Retrieves a file as an {@link InputStream} from resources.
   *
   * @param path Path to the resource.
   * @return The requested file as an {@link InputStream} from the resources directory.
   */
  static InputStream getResourceAsStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }

  /**
   * Retrieves all file paths from resources within a given path.
   *
   * @param path The given path.
   * @return A list with all the file paths found in the given path.
   */
  static List<String> getResourceFilePaths(String path) {
    List<String> resourceNames;
    try (ScanResult scanResult = new ClassGraph().acceptPaths(path).scan()) {
      resourceNames = scanResult.getAllResources().getPaths();
    }

    return resourceNames;
  }
}
