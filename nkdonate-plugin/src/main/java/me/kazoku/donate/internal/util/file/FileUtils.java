package me.kazoku.donate.internal.util.file;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {

  private FileUtils() {
  }

  public static void toFile(final InputStream is, File out, final Logger logger) {
    try {
      toFile(is, out);
    } catch (IOException e) {
      logger.log(Level.SEVERE, String.format("Could not save %s to %s", out.getName(), out), e);
    }
  }

  public static void toFile(final InputStream is, File out) throws IOException {
    try (OutputStream os = new FileOutputStream(out)) {
      byte[] buf = new byte[1024];
      int len;
      while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
    }
  }

  public static boolean create(File file) {
    try {
      return file.exists() || file.createNewFile();
    } catch (IOException e) {
      return false;
    }
  }

  public static boolean mkdirs(File directory) {
    return directory.exists() || directory.mkdirs();
  }

}
