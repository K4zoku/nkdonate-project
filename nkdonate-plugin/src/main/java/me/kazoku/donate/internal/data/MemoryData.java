package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.ConfigPath;
import me.kazoku.artxe.configuration.path.PathLoader;
import me.kazoku.artxe.configuration.yaml.YamlConfig;
import me.kazoku.artxe.utils.ClassUtils;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.util.ThrowableConsumer;
import me.kazoku.donate.internal.util.ThrowableFunction;
import me.kazoku.donate.internal.util.file.FileUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.utils.Validate;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class MemoryData extends YamlConfig {

  public MemoryData(@NotNull File file) {
    super(file);
    load();
  }

  protected static void saveDefault(File file, String resourcePath) {
    if (file.exists()) return;
    Validate.notEmpty(resourcePath, "ResourcePath cannot be null or empty");

    final Logger logger = NKDonatePlugin.getInstance().getLogger();
    if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
      logger.warning("Cannot create parent directory");

    resourcePath = resourcePath.replace('\\', '/');
    final InputStream resourceStream = NKDonatePlugin.getInstance().getResource(resourcePath);

    if (resourceStream != null) FileUtils.toFile(resourceStream, file, logger);
    else logger.info("Default file not found, ignoring...");

  }

  protected void load() {
    getConfig().options().copyDefaults(true);
    PathLoader.loadPath(this);
    saveConfig();
    colorize(getConfig());
  }

  protected <T extends MemoryData> void loadPaths(T t, Config config) {
    Arrays.stream(t.getClass().getDeclaredFields())
            .filter(field -> ConfigPath.class.isAssignableFrom(field.getType()))
            .map((ThrowableFunction<Field, Object>) field -> field.get(t))
            .filter(Objects::nonNull)
            .map(ConfigPath.class::cast)
            .forEach(path -> path.setConfig(config));
  }

  protected static void colorize(ConfigurationSection section) {
    section.getValues(true).forEach((key, value) -> {
      if (value instanceof String) section.set(key, colorize((String) value));
    });
  }

  public static String colorize(String str) {
    return ChatColor.translateAlternateColorCodes('&', str);
  }

}
