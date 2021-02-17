package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.ConfigPath;
import me.kazoku.artxe.configuration.yaml.YamlConfig;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.util.file.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.utils.Validate;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

import static me.kazoku.donate.internal.util.bukkit.ChatColorUtils.colorize;
import static me.kazoku.donate.internal.util.function.ThrowableFunction.throwableFn;

public class MemoryPathBundle {

  private final YamlConfig config;

  public MemoryPathBundle(@NotNull File file) {
    this(new YamlConfig(file));
  }

  public MemoryPathBundle(@NotNull YamlConfig config) {
    this.config = config;
    load();
  }

  protected static void saveDefault(File out, String resourcePath) {
    if (out.exists()) return;
    Validate.notEmpty(resourcePath, "ResourcePath cannot be null or empty");

    final Logger logger = NKDonatePlugin.getInstance().getLogger();
    File parentDir = out.getParentFile();
    if (!FileUtils.mkdirs(parentDir)) logger.warning("Cannot create directory " + parentDir.getPath());

    resourcePath = resourcePath.replace('\\', '/');
    final InputStream resourceStream = NKDonatePlugin.getInstance().getResource(resourcePath);

    if (resourceStream == null) throw new NullPointerException("Couldn't find resource " + resourcePath);

    FileUtils.toFile(resourceStream, out, logger);
  }

  protected static <T extends MemoryPathBundle> void loadPaths(T t, Config config) {
    Arrays.stream(t.getClass().getDeclaredFields())
        .filter(field -> ConfigPath.class.isAssignableFrom(field.getType()))
        .map(throwableFn(field -> field.get(t)))
        .filter(Objects::nonNull)
        .map(ConfigPath.class::cast)
        .forEach(path -> path.setConfig(config));
  }

  protected Config getConfig() {
    return config;
  }

  protected void load() {
    config.getConfig().options().copyDefaults(true);
    loadPaths(this, config);
    colorize(config.getConfig());
  }

  protected void save() {
    config.saveConfig();
  }

}
