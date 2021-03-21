package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.prototype.Paths;
import me.kazoku.artxe.configuration.path.prototype.StringConfigPath;
import me.kazoku.donate.internal.util.bukkit.ChatColorUtils;

import java.io.File;
import java.util.function.Supplier;

public final class Messages extends MemoryPathBundle {

  public static final StringConfigPath UNKNOWN_COMMAND = Paths.stringPath("unknown-command", "§cUnknown command");
  public static final StringConfigPath UNKNOWN_ERROR = Paths.stringPath("unknown-error", "§cAn error occurred!");
  public static final StringConfigPath NO_TOPUP_MODULE = Paths.stringPath("no-topup-module", "§cThere is no module to handle this!");
  public static final StringConfigPath NO_PERMISSION = Paths.stringPath("no-permission", "§cNo permission");
  public static final StringConfigPath TOO_MANY_ARGS = Paths.stringPath("too-many-args", "§cToo many arguments");
  public static final StringConfigPath TOO_FEW_ARGS = Paths.stringPath("too-few-args", "§cToo few arguments");
  public static final StringConfigPath ONLY_PLAYER = Paths.stringPath("only-player", "§cOnly player");
  public static final StringConfigPath RELOADED = Paths.stringPath("reloaded", "§aReloaded {type} successfully");
  public static final StringConfigPath NVT = Paths.stringPath("not-valid-type", "§f'{0}' §cis not valid card type!");
  public static final StringConfigPath ENTER_CARD_SERIAL = Paths.stringPath("enter-card-serial", "§aEnter card serial");
  public static final StringConfigPath ENTER_CARD_PIN = Paths.stringPath("enter-card-pin", "§aEnter card pin");
  public static final StringConfigPath CANCELLED = Paths.stringPath("cancelled", "§eCancelled");
  public static final StringConfigPath SEND_SUCCESS = Paths.stringPath("send-success", "§aSuccess, waiting for approval...");
  public static final StringConfigPath SEND_FAILED = Paths.stringPath("send-failed", "§cFailed");
  public static final StringConfigPath DONATE_SUCCESS = Paths.stringPath("donate-success", "§aSuccess");
  public static final StringConfigPath DONATE_FAILED = Paths.stringPath("donate-failed", "§cFailed");
  public static final StringConfigPath SERIAL_ENTERED = Paths.stringPath("serial-entered", "§cYou entered serial: {serial}");
  public static final StringConfigPath PIN_ENTERED = Paths.stringPath("pin-entered", "§cYou entered pin: {pin}");
  public static final StringConfigPath CHECKED = Paths.stringPath("checked", "§aChecked {0} cards in queue");


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
    instance.load();
    if (save) instance.save();
    return instance;
  }

  public static void hardReload() {
    newInstance();
  }

  public static void softReload() {
    if (MESSAGES_FILE.get().exists()) {
      final Config config = getInstance().getConfig();
      config.reloadConfig();
      ChatColorUtils.colorize(config.getConfig());
    } else hardReload();
  }
}
