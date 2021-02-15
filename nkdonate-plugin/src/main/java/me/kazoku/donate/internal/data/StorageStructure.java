package me.kazoku.donate.internal.data;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.util.ThrowableFunction;
import me.kazoku.donate.internal.util.file.FileUtils;
import me.kazoku.donate.internal.util.logging.Level;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

public final class StorageStructure {
  public static final File ROOT_DIRECTORY = NKDonatePlugin.getInstance().getDataFolder();
  public static final File LOGS_DIRECTORY = new File(ROOT_DIRECTORY, "logs");
  public static final File SETTINGS_DIRECTORY = new File(ROOT_DIRECTORY, "settings");
  public static final File LANGUAGES_DIRECTORY = new File(ROOT_DIRECTORY, "languages");
  // flexible
  public static final Supplier<File> LOCALE_DIRECTORY = () -> new File(StorageStructure.LANGUAGES_DIRECTORY, GeneralSettings.LOCALE.getValue());

  public static final File DATA_DIRECTORY = new File(ROOT_DIRECTORY, "data");

  private static final String OK = "§aOK";
  private static final String ERROR = "§cERROR";

  static {
    Arrays.stream(StorageStructure.class.getDeclaredFields())
        .filter(field -> field.getType() == File.class)
        .map((ThrowableFunction<Field, File>) field -> (File) field.get(null))
        .filter(Objects::nonNull)
        .forEach(StorageStructure::printStatus);
  }

  private StorageStructure() {
  }

  private static void printStatus(File directory) {
    printStatus(directory, NKDonatePlugin.getInstance().getLogger(), Level.DEBUG);
  }

  public static void printStatus(File directory, Logger logger, java.util.logging.Level level) {
    logger.log(level, () -> String.format("%s: %s", directory.getPath(), FileUtils.mkdirs(directory) ? OK : ERROR));
  }

}
