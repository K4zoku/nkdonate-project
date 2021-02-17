package me.kazoku.donate.internal.ui;

import me.kazoku.artxe.utils.PlaceholderCache;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.util.bukkit.ChatColorUtils;
import me.kazoku.donate.internal.util.collection.CollectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.simpleyaml.configuration.serialization.ConfigurationSerializable;
import org.simpleyaml.configuration.serialization.SerializableAs;

import java.util.*;
import java.util.function.Function;

@SerializableAs("ChatUI")
public final class ChatUI implements UI, ConfigurationSerializable, Cloneable {

  private static final PlaceholderCache PLACEHOLDER_CACHE = NKDonatePlugin.getPlaceholderCache();

  static {
    ChatColorUtils.register(ChatUI.class, value ->
        value.getAndUpdateText(ChatColorUtils::colorize)
            .getAndUpdateClickValue(ChatColorUtils::colorize)
            .getAndUpdateHoverText(ChatColorUtils::colorize)
    );
  }

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
    this.text = (String) serialized.getOrDefault("text", "");
    this.clickAction = Optional.ofNullable(serialized.get("click-action"))
        .map(String::valueOf)
        .map(ClickEvent.Action::valueOf)
        .orElse(ClickEvent.Action.RUN_COMMAND);
    this.click = (String) serialized.getOrDefault("click-value", "");
    this.hover = Optional.ofNullable(serialized.get("hover"))
        .flatMap(CollectionUtils::objToStrList)
        .map(list -> String.join("\n", list))
        .orElse("");
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

  public static ChatUI deserialize(Map<String, Object> serialized) {
    return new ChatUI(serialized);
  }

  public void test(EntityDamageByEntityEvent event, ItemStack bow) {
    if (!(event.getEntity() instanceof Player)) return;
    Optional.ofNullable(bow.getItemMeta())
        .filter(ItemMeta::hasDisplayName)
        .map(ItemMeta::getDisplayName)
        .filter(name -> name.equals("Thunder Sword"))
        .ifPresent(name -> {
          // do something
        });
  }

  public ChatUI setText(String text) {
    this.text = text;
    return this;
  }

  public ChatUI getAndUpdateText(Function<String, String> updateFunction) {
    return setText(updateFunction.apply(text));
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

  public ChatUI getAndUpdateClickValue(Function<String, String> updateFunction) {
    return setClickValue(updateFunction.apply(click));
  }

  public ChatUI setHoverText(String text) {
    this.hover = text;
    return this;
  }

  public ChatUI getAndUpdateHoverText(Function<String, String> updateFunction) {
    return setHoverText(updateFunction.apply(hover));
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

}
