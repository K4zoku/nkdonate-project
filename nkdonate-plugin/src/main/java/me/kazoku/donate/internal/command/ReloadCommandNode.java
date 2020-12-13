package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.artxe.bukkit.command.extra.SimpleCommandNode;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.data.GeneralSettings;
import me.kazoku.donate.internal.data.Messages;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ReloadCommandNode implements CommandNode {

  static final ReloadCommandNode INSTANCE = new ReloadCommandNode();

  private final List<CommandNode> subcommands = new ArrayList<>();

  private final Matcher reloadMsg = Pattern.compile("(?iu)[{]type[}]").matcher(Optional.ofNullable(Messages.RELOADED.getValue()).orElse("{type}"));

  private ReloadCommandNode() {
    CommandNode reloadAll = new SimpleCommandNode(
            "all",
            "nkdonate.admin.reload.all",
            (sender, label, args) -> {
              GeneralSettings.reload(true, true, true);
              sender.sendMessage(reloadMsg.replaceAll("ALL"));
              return true;
            });
    CommandNode reloadSettings = new SimpleCommandNode(
            "settings",
            "setting",
            "nkdonate.admin.reload.settings",
            (sender, label, args) -> {
              GeneralSettings.reload(true, false, false);
              NKDonatePlugin.getInstance().getCustomLogger().setDebug(GeneralSettings.DEBUG.getValue());
              NKDonatePlugin.getInstance().getCustomLogger().debug("TEST");
              sender.sendMessage(reloadMsg.replaceAll("SETTINGS"));
              return true;
            });
    CommandNode reloadMessages = new SimpleCommandNode(
            "messages",
            Arrays.asList("message", "msg"),
            "nkdonate.admin.reload.messages",
            (sender, label, args) -> {
              GeneralSettings.reload(false, true, false);
              sender.sendMessage(reloadMsg.replaceAll("MESSAGES"));
              return true;
            });
    CommandNode reloadUi = new SimpleCommandNode(
            "ui",
            "nkdonate.admin.reload.ui",
            (sender, label, args) -> {
              GeneralSettings.reload(false, false, true);
              sender.sendMessage(reloadMsg.replaceAll("UI"));
              return true;
            }
    );
    List<CommandNode> subcommands = new ArrayList<>();
    subcommands.add(reloadAll);
    subcommands.add(reloadSettings);
    subcommands.add(reloadMessages);
    subcommands.add(reloadUi);
    CommandNode numberAsType = new SimpleCommandNode(
            label -> {
              try {
                Integer.parseInt(label);
              } catch (NumberFormatException e) {
                return false;
              }
              return true;
            },
            "nkdonate.admin.reload.all",
            (sender, label, args) -> {
              try {
                return subcommands.get(Integer.parseInt(label)).execute(sender, label, args);
              } catch (IndexOutOfBoundsException e) {

                return false;
              }
            }
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
    return NKDonatePlugin.getInstance().customFeedback();
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    GeneralSettings.reload(true, true, true);
    sender.sendMessage(reloadMsg.replaceAll("ALL"));
    return true;
  }
}
