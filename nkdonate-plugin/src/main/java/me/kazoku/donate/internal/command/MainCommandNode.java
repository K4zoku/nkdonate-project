package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public final class MainCommandNode implements CommandNode {

  public static final MainCommandNode INSTANCE = new MainCommandNode();

  private MainCommandNode() {
  }

  @Override
  public String label() {
    return "nkdonate";
  }

  @Override
  public List<String> aliases() {
    return Arrays.asList("donate", "napthe");
  }

  @Override
  public List<CommandNode> subCommands() {
    return Arrays.asList(InfoCommandNode.INSTANCE, ModulesCommandNode.INSTANCE, ReloadCommandNode.INSTANCE, ChooseCommandNode.getInstance(), QueueCommandNode.INSTANCE);
  }

  @Override
  public CommandFeedback feedback() {
    return NKDonatePlugin.getInstance().customFeedback();
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    return ChooseCommandNode.getInstance().execute(sender, label, args);
  }
}
