package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.CommentablePath;
import me.kazoku.artxe.configuration.path.prototype.Paths;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
  public static final CommentablePath<String> ENABLED_TOPUP_MODULE = Paths.commented(
      Paths.stringPath("modules.topup.enabled", ""),
      "[?] Enable topup module"
  );
  public static final CommentablePath<List<String>> CANCEL_WORDS = Paths.commented(
      Paths.simplePath("input.cancel", Arrays.asList("cancel", "exit")),
      "[?] Cancel"
  );
  // End Path =========================================

  // static file
  public static final File GENERAL_SETTINGS_FILE = new File(StorageStructure.SETTINGS_DIRECTORY, "general.yml");
  private static GeneralSettings instance;

  private GeneralSettings() {
    super(GENERAL_SETTINGS_FILE);
  }

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
