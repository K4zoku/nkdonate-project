package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.CommentablePath;
import me.kazoku.artxe.configuration.path.prototype.Paths;
import me.kazoku.artxe.converter.time.object.Time;
import me.kazoku.donate.internal.data.prototype.TimeConfigPath;

import java.io.File;

public final class GeneralSettings extends MemoryPathBundle {

  // Paths ============================================
  public static final CommentablePath<Boolean> DEBUG = Paths.commented(
      Paths.booleanPath("debug", false),
      "/!\\ Do not enable if you're not developer"
  );
  public static final CommentablePath<String> LOCALE = Paths.commented(
      Paths.stringPath("locale", "en-US"),
      "[?] Change language here"
  );
  public static final CommentablePath<Time> CHECKING_PERIOD = Paths.commented(
      new TimeConfigPath("period", "1200tick"),
      "[?] Card checking period (default 1200tick = 60s)",
      "[+] Time units support! Can use unit such as tick (no need to add 'tick'), nanosecond (ns), ",
      "    millisecond (ms), second (s), minute (m), hour (h), day (d). Recommend using tick!"
  );
  public static final CommentablePath<String> ENABLED_TOPUP_MODULE = Paths.commented(
      Paths.stringPath("modules.topup.enabled", ""),
      "[?] Enable topup module"
  );
  // End Path =========================================

  // static file
  public static final File GENERAL_SETTINGS_FILE = new File(StorageStructure.SETTINGS_DIRECTORY, "general.yml");

  private GeneralSettings() {
    super(GENERAL_SETTINGS_FILE);
  }

  private static GeneralSettings instance;

  public static GeneralSettings getInstance() {
    return instance == null ? newInstance() : instance;
  }

  private static GeneralSettings newInstance() {
    String resourcePath = String.format("settings/%s", GENERAL_SETTINGS_FILE.getName());
    boolean save = false;
    try {
      saveDefault(GENERAL_SETTINGS_FILE, resourcePath);
    } catch (NullPointerException e) {
      save = true;
    }
    instance = new GeneralSettings();
    if (save) instance.save();
    return instance;
  }

  public static void hardReload() {
    newInstance();
  }

  public static void softReload() {
    if (GENERAL_SETTINGS_FILE.exists()) getInstance().getConfig().reloadConfig();
    else hardReload();
  }

}
