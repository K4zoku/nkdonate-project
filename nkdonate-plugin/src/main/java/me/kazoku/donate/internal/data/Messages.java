package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.prototype.StringConfigPath;
import me.kazoku.artxe.configuration.yaml.YamlConfig;
import me.kazoku.artxe.utils.JarUtils;
import me.kazoku.donate.NKDonatePlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.jar.JarFile;

public final class Messages extends MemoryData {

  public static final StringConfigPath UNKNOWN_COMMAND = new StringConfigPath("unknown-command", "§cUnknown command");
  public static final StringConfigPath UNKNOWN_ERROR = new StringConfigPath("unknown-error", "§cAn error occurred!");
  public static final StringConfigPath NO_PERMISSION = new StringConfigPath("no-permission", "§cNo permission");
  public static final StringConfigPath TOO_MANY_ARGS = new StringConfigPath("too-many-args", "§cToo many arguments");
  public static final StringConfigPath TOO_FEW_ARGS = new StringConfigPath("too-few-args", "§cToo few arguments");
  public static final StringConfigPath ONLY_PLAYER = new StringConfigPath("only-player", "§cOnly player");
  public static final StringConfigPath RELOADED = new StringConfigPath("reloaded", "§aReloaded {type} successfully");
  public static final StringConfigPath NAN = new StringConfigPath("not-a-number", "§f'{0}' §cis not a number!");

  public Messages(@NotNull File file) {
    super(file);
  }

  static void loadLocale(File file) {
    saveDefault(file,
        String.format("languages/%s/%s", GeneralSettings.LOCALE.getValue(), file.getName())
    );
    new Messages(file);
  }
}
