package me.kazoku.donate.internal.util;

import me.kazoku.donate.internal.util.collection.CollectionUtils;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChatColorUtils {
  private static final Map<Class<?>, Function<Object, Object>> COLORIZATION = new LinkedHashMap<>();

  static {
    registerDefaults();
  }

  private ChatColorUtils() {
  }

  private static void registerDefaults() {
    register(String.class, ChatColorUtils::colorize);
    register(List.class, value -> {
      AtomicReference<Object> result = new AtomicReference<>(value);
      CollectionUtils.objToStrList(value)
          .map(ChatColorUtils::colorize)
          .ifPresent(result::set);
      return result.get();
    });
  }

  public static <T> void register(Class<T> type, Function<T, Object> colorizeFn) {
    COLORIZATION.put(type, obj -> colorizeFn.apply(type.cast(obj)));
  }

  public static void unregister(Class<?> type) {
    COLORIZATION.remove(type);
  }

  public static String colorize(char alt, String text) {
    char[] array = text.toCharArray();
    int i = 0;
    while (i < array.length - 1) {
      if (array[i] == alt && isColorCode(array[i + 1])) {
        array[i++] = '§';
        array[i] = Character.toLowerCase(array[i]);
      }
      i++;
    }
    return new String(array);
  }

  private static boolean isColorCode(char c) {
    // regex pattern (?i)[0-9a-fk-orx]
    return c == 'R' || c == 'X' ||
        c == 'r' || c == 'x' ||
        c >= '0' && c <= '9' ||
        c >= 'A' && c <= 'F' ||
        c >= 'K' && c <= 'O' ||
        c >= 'a' && c <= 'f' ||
        c >= 'k' && c <= 'o';
  }

  public static String colorize(String text) {
    return colorize('&', text);
  }

  public static List<String> colorize(List<String> list) {
    return list.stream().map(ChatColorUtils::colorize).collect(Collectors.toList());
  }

  public static void colorize(ConfigurationSection section) {
    section.getValues(true)
        .forEach((key, value) ->
            COLORIZATION.keySet().stream()
                .filter(clazz -> clazz.isInstance(value))
                .findFirst()
                .map(COLORIZATION::get)
                .map(action -> action.apply(value))
                .ifPresent(newValue -> section.set(key, newValue))
        );
  }

}
