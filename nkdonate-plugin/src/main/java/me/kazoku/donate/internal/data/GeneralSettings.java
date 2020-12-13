package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.CommentablePath;
import me.kazoku.artxe.configuration.path.prototype.Paths;
import me.kazoku.artxe.configuration.yaml.YamlConfig;
import me.kazoku.donate.internal.data.prototype.TimeConfigPath;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class GeneralSettings extends MemoryData {

  public static final CommentablePath<Boolean> DEBUG = Paths.commented(
          Paths.booleanPath("debug", false),
          "/!\\ Do not enable if you're not developer"
  );
  public static final CommentablePath<String> LOCALE = Paths.commented(
          Paths.stringPath("locale", "en-US"),
          "[?] Change language here"
  );
  public static final CommentablePath<Long> CHECKING_PERIOD = Paths.commented(
          new TimeConfigPath("period", "1200tick"),
          "[?] Card checking period (default 1200tick = 60s)",
          "[+] Time units support! Can use unit such as tick (no need to add 'tick'), nanosecond (ns), ",
          "    millisecond (ms), second (s), minute (m), hour (h), day (d). Recommend using tick!");
  public static final CommentablePath<String> ENABLED_TOPUP_MODULE = Paths.commented(
          Paths.stringPath("modules.topup.enabled", ""),
          "[?] Enable topup module"
  );

  public GeneralSettings(@NotNull File file) {
    super(file);
  }

  private static void loadChild(boolean languages, boolean ui) {
    if (languages) Messages.loadLocale(new File(StorageStructure.LANGUAGES_DIRECTORY, "messages.yml"));
    if (ui) UISettings.loadUi(new File(StorageStructure.UI_DIRECTORY, "choose-card.yml"));
  }

  private static GeneralSettings instance;

  public static void loadSettings(File file) {
    instance = new GeneralSettings(file);
    loadChild(true, true);
  }

  public static void reload(boolean settings, boolean languages, boolean ui) {
    if (settings) instance.reloadConfig();
    loadChild(languages, ui);
  }
}
