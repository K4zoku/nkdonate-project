package me.kazoku.donate.internal.data.prototype;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.AdvancedConfigPath;
import me.kazoku.artxe.utils.time.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TimeConfigPath extends AdvancedConfigPath<String, Long> {

  public TimeConfigPath(@NotNull String path, @Nullable Object def) {
    super(path, TimeUtils.toTick(def));
  }

  @Override
  public @Nullable String getFromConfig(@NotNull Config config) {
    return config.getConfig().getString(getPath());
  }

  @Override
  public @Nullable Long convert(@NotNull String rawValue) {
    return TimeUtils.toTick(rawValue);
  }

  @Override
  public @Nullable String convertToRaw(@NotNull Long value) {
    return value + "tick";
  }

  @Override
  public @NotNull Long getValue() {
    return Objects.requireNonNull(super.getValue());
  }
}
