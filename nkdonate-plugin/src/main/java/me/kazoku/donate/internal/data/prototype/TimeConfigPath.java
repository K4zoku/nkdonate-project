package me.kazoku.donate.internal.data.prototype;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.AdvancedConfigPath;
import me.kazoku.artxe.converter.time.object.Time;
import me.kazoku.artxe.converter.time.prototype.TickConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimeConfigPath extends AdvancedConfigPath<Object, Time> {

  public TimeConfigPath(@NotNull String path, @NotNull Object def) {
    super(path, TickConverter.convertToTick(def));
  }

  @Override
  public @Nullable Object getFromConfig(@NotNull Config config) {
    return config.getConfig().get(getPath());
  }

  @Override
  public @Nullable Time convert(@NotNull Object rawValue) {
    return TickConverter.convertToTick(rawValue);
  }

  @Override
  public @Nullable Object convertToRaw(@NotNull Time value) {
    return value.toString();
  }
}
