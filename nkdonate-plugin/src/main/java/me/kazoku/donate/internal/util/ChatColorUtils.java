package me.kazoku.donate.internal.util;

import me.kazoku.donate.internal.util.collection.CollectionUtils;
import org.bukkit.ChatColor;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ChatColorUtils {
    private static final Map<Class<?>, Function<Object, Object>> ACTIONS = new LinkedHashMap<>();

    static {
        ACTIONS.put(String.class, value -> colorize((String) value));
        ACTIONS.put(List.class, value -> {
            List<?> origin = (List<?>) value;
            if (!CollectionUtils.isStringCollection(origin)) return origin;
            List<String> colorized = new LinkedList<>();
            origin.stream()
                .map(String.class::cast)
                .map(ChatColorUtils::colorize)
                .forEach(colorized::add);
            return colorized;
        });
        // todo: api to execute action on config section
    }

    private ChatColorUtils() {
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @SuppressWarnings("unchecked")
    public static List<String> colorize(List<String> list) {
        return (List<String>) ACTIONS.get(List.class).apply(list);
    }

    public static void colorize(ConfigurationSection section) {
        section.getValues(true).forEach((key, value) ->
            ACTIONS.keySet().stream()
                .filter(clazz -> clazz.isInstance(value))
                .findFirst()
                .map(ACTIONS::get)
                .map(action -> action.apply(value))
                .ifPresent(newValue -> section.set(key, newValue))
        );
    }


}
