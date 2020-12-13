package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public final class InfoCommandNode implements CommandNode {

  static final InfoCommandNode INSTANCE = new InfoCommandNode();

  private InfoCommandNode() {
  }

  public String label() {
    return "info";
  }

  @Override
  public List<String> aliases() {
    return Collections.singletonList("about");
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    sender.sendMessage("§a===========================");
    sender.sendMessage("§bNKDonate v" + NKDonatePlugin.getInstance().getDescription().getVersion());
    sender.sendMessage("§a===========================");
    return false;
  }

  @Override
  public CommandFeedback feedback() {
    return NKDonatePlugin.getInstance().customFeedback();
  }
}
