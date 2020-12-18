package me.kazoku.donate.internal.ui;

import me.kazoku.artxe.utils.PlaceholderCache;
import me.kazoku.donate.NKDonatePlugin;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.serialization.ConfigurationSerializable;
import org.simpleyaml.configuration.serialization.SerializableAs;

import java.util.*;

import static me.kazoku.donate.internal.util.ChatColorUtils.colorize;

@SerializableAs("ChatUI")
public final class ChatUI implements UI, ConfigurationSerializable, Cloneable {

  private static final PlaceholderCache PLACEHOLDER_CACHE = NKDonatePlugin.getPlaceholderCache();

  private final Set<Player> players = new HashSet<>();
  private String text;
  private String click;
  private String hover;
  private ClickEvent.Action clickAction = ClickEvent.Action.RUN_COMMAND; // default

  public ChatUI(String text, boolean broadcast) {
    this(text, broadcast ? Bukkit.getServer().getOnlinePlayers() : Collections.emptyList());
  }

  private ChatUI(String text, Collection<? extends Player> target) {
    this.text = text;
    players.addAll(target);
  }

  public ChatUI(String text, Player... target) {
    this(text, Arrays.asList(target));
  }

  /**
   * Deserialize constructor
   *
   * @param serialized serialized map
   */
  private ChatUI(Map<String, Object> serialized) {
    this.text = colorize(String.valueOf(serialized.get("text")));
    this.clickAction = Optional.ofNullable(serialized.get("click-action"))
            .map(String::valueOf)
            .map(ClickEvent.Action::valueOf)
            .orElse(ClickEvent.Action.RUN_COMMAND);
    this.click = colorize(String.valueOf(serialized.get("click-value")));
    //noinspection unchecked
    this.hover = Optional.ofNullable(serialized.get("hover"))
            .filter(List.class::isInstance)
            .map(List.class::cast)
            .filter(list -> list.size() > 0 && list.get(0) instanceof String)
            .map(list -> colorize(String.join("\n", (List<String>) list)))
            .orElse("null");
  }

  /**
   * Clone constructor
   *
   * @param origin origin ChatUI
   */
  private ChatUI(ChatUI origin) {
    this.text = origin.text;
    this.hover = origin.hover;
    this.clickAction = origin.clickAction;
    this.click = origin.click;
    this.players.addAll(origin.players);
  }

  public ChatUI setText(String text) {
    this.text = text;
    return this;
  }

  public ChatUI applyPlaceholders(Map<String, String> placeholderMap) {
    placeholderMap.entrySet().stream().parallel()
            .forEach(e -> applyPlaceholder(e.getKey(), e.getValue()));
    return this;
  }

  public ChatUI applyPlaceholder(String placeholder, String value) {
    return clone()
            .setText(PLACEHOLDER_CACHE.apply(text, placeholder, value))
            .setClickValue(PLACEHOLDER_CACHE.apply(click, placeholder, value))
            .setHoverText(PLACEHOLDER_CACHE.apply(hover, placeholder, value));
  }

  public ChatUI setClickAction(ClickEvent.Action action) {
    this.clickAction = action;
    return this;
  }

  public ChatUI setClickValue(String value) {
    this.click = value;
    return this;
  }

  public ChatUI setHoverText(String text) {
    this.hover = text;
    return this;
  }

  public ChatUI addPlayer(Player player) {
    players.add(player);
    return this;
  }

  @Override
  public void display() {
    BaseComponent[] baseComponents = new ComponentBuilder(text)
            .event(new ClickEvent(clickAction, click))
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(hover).create()))
            .create();
    players.stream().parallel()
            .filter(Player::isOnline)
            .forEach(player -> player.spigot().sendMessage(baseComponents));
  }

  public ChatUI clone() {
    try {
      ChatUI clone = (ChatUI) super.clone();
      clone.players.addAll(this.players);
      clone.text = this.text;
      clone.click = this.click;
      clone.hover = this.hover;
      clone.clickAction = this.clickAction;
      return clone;
    } catch (CloneNotSupportedException e) {
      return new ChatUI(this);
    }
  }

  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> serialized = new LinkedHashMap<>();
    serialized.put("text", text);
    serialized.put("hover", hover.split("\n"));
    serialized.put("click-action", clickAction.toString());
    serialized.put("click-value", click);
    return serialized;
  }

  public static ChatUI deserialize(Map<String, Object> serialized) {
    return new ChatUI(serialized);
  }

}
