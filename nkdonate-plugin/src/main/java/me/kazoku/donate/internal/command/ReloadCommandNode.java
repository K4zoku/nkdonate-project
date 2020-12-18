package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.artxe.bukkit.command.extra.SimpleCommandNode;
import me.kazoku.artxe.utils.PlaceholderCache;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.data.GeneralSettings;
import me.kazoku.donate.internal.data.Messages;
import me.kazoku.donate.internal.data.UISettings;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ReloadCommandNode implements CommandNode {

  static final ReloadCommandNode INSTANCE = new ReloadCommandNode();

  private final List<CommandNode> subcommands = new ArrayList<>();
  private final CommandFeedback feedback;

  private ReloadCommandNode() {
    feedback = NKDonatePlugin.getInstance().customFeedback();
    final String reloadMsg = Messages.RELOADED.getValue();
    final PlaceholderCache cache = NKDonatePlugin.getPlaceholderCache();
    final String reloadAllMsg = cache.apply(reloadMsg, "type", "ALL");
    feedback.COMMAND_SUCCESS.setFeedback(reloadAllMsg);
    CommandNode reloadAll = new SimpleCommandNode(
        "all",
        "nkdonate.admin.reload.all",
        this::execute
    );
    reloadAll.feedback().COMMAND_SUCCESS.setFeedback(reloadAllMsg);
    CommandNode reloadSettings = new SimpleCommandNode(
        "settings",
        "setting",
        "nkdonate.admin.reload.settings",
        (sender, label, args) -> {
          GeneralSettings.softReload();
          return true;
        }
    );
    reloadSettings.feedback().COMMAND_SUCCESS.setFeedback(cache.apply(reloadMsg, "type", "SETTINGS"));
    CommandNode reloadLanguages = new SimpleCommandNode(
        "language",
        Arrays.asList("lang", "languages"),
        "nkdonate.admin.reload.ui",
        (sender, label, args) -> {
          UISettings.softReload();
          return true;
        }
    );
    reloadLanguages.feedback().COMMAND_SUCCESS.setFeedback(cache.apply(reloadMsg, "type", "LANGUAGES"));
    CommandNode reloadMessages = new SimpleCommandNode(
        "messages",
        Arrays.asList("message", "msg"),
        "nkdonate.admin.reload.messages",
        (sender, label, args) -> {
          Messages.softReload();
          return true;
        }
    );
    reloadMessages.feedback().COMMAND_SUCCESS.setFeedback(cache.apply(reloadMsg, "type", "MESSAGES"));
    CommandNode reloadUi = new SimpleCommandNode(
        "ui",
        "nkdonate.admin.reload.ui",
        (sender, label, args) -> {
          UISettings.softReload();
          return true;
        }
    );
    reloadUi.feedback().COMMAND_SUCCESS.setFeedback(cache.apply(reloadMsg, "type", "UI"));

    List<CommandNode> subcommands = new ArrayList<>();
    subcommands.add(reloadAll);
    subcommands.add(reloadSettings);
    subcommands.add(reloadLanguages);
    subcommands.add(reloadMessages);
    subcommands.add(reloadUi);
    CommandNode numberAsType = new SimpleCommandNode(
        label -> {
          try {
            int i = Integer.parseInt(label);
            return i >= 0 && i < subcommands.size();
          } catch (NumberFormatException e) {
            return false;
          }
        },
        "nkdonate.admin.reload.*",
        (sender, label, args) -> subcommands.get(Integer.parseInt(label)).execute(sender, label, args)
    );
    this.subcommands.addAll(subcommands);
    this.subcommands.add(numberAsType);
  }

  @Override
  public List<String> aliases() {
    return Collections.emptyList();
  }

  @Override
  public String label() {
    return "reload";
  }

  @Override
  public List<String> permissions() {
    return Collections.singletonList("nkdonate.admin.reload.*");
  }

  @Override
  public List<CommandNode> subCommands() {
    return this.subcommands;
  }

  @Override
  public CommandFeedback feedback() {
    return feedback;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    GeneralSettings.softReload();
    Messages.softReload();
    UISettings.softReload();
    return true;
  }
}
