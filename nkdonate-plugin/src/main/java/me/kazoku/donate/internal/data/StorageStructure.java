package me.kazoku.donate.internal.data;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.util.ThrowableFunction;
import me.kazoku.donate.internal.util.file.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public final class StorageStructure {
  public static final File ROOT_DIRECTORY = NKDonatePlugin.getInstance().getDataFolder();
  public static final File LOGS_DIRECTORY = new File(ROOT_DIRECTORY, "logs");
  public static final File SETTINGS_DIRECTORY = new File(ROOT_DIRECTORY, "settings");
  public static final File LANGUAGES_DIRECTORY = new File(ROOT_DIRECTORY, "languages" + File.separator + GeneralSettings.LOCALE.getValue());
  public static final File UI_DIRECTORY = new File(LANGUAGES_DIRECTORY, "ui");
  public static final File DATA_DIRECTORY = new File(ROOT_DIRECTORY, "data");

  private static final String OK = "§aOK";
  private static final String ERROR = "§cERROR";

  static {
    Arrays.stream(StorageStructure.class.getDeclaredFields())
            .filter(field -> field.getType() == File.class)
            .map((ThrowableFunction<Field, File>) field -> (File) field.get(null))
            .filter(Objects::nonNull)
            .forEach(StorageStructure::checkDir);
  }

  private static void checkDir(File directory) {
    NKDonatePlugin.getInstance().getCustomLogger().debug(String.format("%s: %s", directory.getPath(), FileUtils.mkDirsIfNotExists(directory) ? OK : ERROR));
  }

  private StorageStructure() {}

}
