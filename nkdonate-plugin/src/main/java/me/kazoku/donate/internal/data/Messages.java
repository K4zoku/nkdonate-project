package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.prototype.StringConfigPath;

import java.io.File;
import java.util.function.Supplier;

public final class Messages extends MemoryPathBundle {

  public static final StringConfigPath UNKNOWN_COMMAND = new StringConfigPath("unknown-command", "§cUnknown command");
  public static final StringConfigPath UNKNOWN_ERROR = new StringConfigPath("unknown-error", "§cAn error occurred!");
  public static final StringConfigPath NO_PERMISSION = new StringConfigPath("no-permission", "§cNo permission");
  public static final StringConfigPath TOO_MANY_ARGS = new StringConfigPath("too-many-args", "§cToo many arguments");
  public static final StringConfigPath TOO_FEW_ARGS = new StringConfigPath("too-few-args", "§cToo few arguments");
  public static final StringConfigPath ONLY_PLAYER = new StringConfigPath("only-player", "§cOnly player");
  public static final StringConfigPath RELOADED = new StringConfigPath("reloaded", "§aReloaded {type} successfully");
  public static final StringConfigPath NAN = new StringConfigPath("not-a-number", "§f'{0}' §cis not a number!");

  // flexible file
  private static final Supplier<File> MESSAGES_FILE = () -> new File(StorageStructure.LOCALE_DIRECTORY.get(), "messages.yml");
  private static Messages instance;

  private Messages(File file) {
    super(file);
  }

  public static Messages getInstance() {
    return instance == null ? newInstance() : instance;
  }

  private static Messages newInstance() {
    File file = MESSAGES_FILE.get();
    String resourcePath = String.format("languages/%s/%s", GeneralSettings.LOCALE.getValue(), file.getName());
    boolean save = false;
    try {
      saveDefault(file, resourcePath);
    } catch (NullPointerException e) {
      save = true;
    }
    instance = new Messages(file);
    if (save) instance.save();
    return instance;
  }

  public static void hardReload() {
    newInstance();
  }

  public static void softReload() {
    if (MESSAGES_FILE.get().exists()) getInstance().getConfig().reloadConfig();
    else hardReload();
  }
}
