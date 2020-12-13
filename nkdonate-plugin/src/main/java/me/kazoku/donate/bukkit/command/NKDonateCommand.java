package me.kazoku.donate.bukkit.command;

import me.kazoku.donate.internal.command.MainCommandNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class NKDonateCommand extends Command {

  private NKDonateCommand(String name, List<String> aliases) {
    super(name, "", "/<command>", aliases);
  }

  public NKDonateCommand() {
    this(MainCommandNode.INSTANCE.label(), MainCommandNode.INSTANCE.aliases());
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    return MainCommandNode.INSTANCE.handle(sender, args);
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
    return MainCommandNode.INSTANCE.tabComplete(sender, args);
  }
}
