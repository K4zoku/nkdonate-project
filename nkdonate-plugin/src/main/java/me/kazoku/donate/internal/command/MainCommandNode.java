package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public final class MainCommandNode implements CommandNode {

  private static final MainCommandNode instance = new MainCommandNode();
  private static final String LABEL = "nkdonate";
  private static final List<String> ALIASES = Arrays.asList("donate", "napthe");
  private static final List<CommandNode> SUBCOMMANDS = Arrays.asList(
      InfoCommandNode.getInstance(), ModulesCommandNode.getInstance(),
      ReloadCommandNode.getInstance(), ChooseCommandNode.getInstance(),
      CheckCommandNode.getInstance()
  );

  private MainCommandNode() {
  }

  public static MainCommandNode getInstance() {
    return instance;
  }

  @Override
  public String label() {
    return LABEL;
  }

  @Override
  public List<String> aliases() {
    return ALIASES;
  }

  @Override
  public List<CommandNode> subCommands() {
    return SUBCOMMANDS;
  }

  @Override
  public boolean onlyPlayer() {
    return false;
  }

  @Override
  public CommandFeedback feedback() {
    return NKDonatePlugin.getInstance().customFeedback();
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    return sender instanceof Player ? ChooseCommandNode.getInstance().handle(sender, label) : InfoCommandNode.getInstance().handle(sender, label);
  }
}
