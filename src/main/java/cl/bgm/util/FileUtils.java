package cl.bgm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/** File utils. */
public interface FileUtils {

  /**
   * Retrieves a file as an {@link InputStream} from the resources/ directory.
   *
   * @param path Path to the resource.
   * @return The requested file as an {@link InputStream} from the resources directory.
   */
  static InputStream getResourceAsStream(String path) {
    return getContextClassLoader().getResourceAsStream(path);
  }

  static List<String> getResourceFiles(String path) {
    List<String> filenames = new ArrayList<>();

    try (InputStream in = FileUtils.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
      String resource;

      while ((resource = br.readLine()) != null) {
        filenames.add(resource);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return filenames;
  }

  static ClassLoader getContextClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }
}
