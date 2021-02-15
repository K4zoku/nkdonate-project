package me.kazoku.donate.bukkit.command;

import me.kazoku.donate.internal.command.MainCommandNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class NKDonateCommand extends Command {

  private NKDonateCommand(String name, List<String> aliases) {
    super(name, "", "/<command>", aliases);
  }

  private static final NKDonateCommand instance = new NKDonateCommand();

  private NKDonateCommand() {
    this(MainCommandNode.getInstance().label(), MainCommandNode.getInstance().aliases());
  }

  public static NKDonateCommand getInstance() {
    return instance;
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    return MainCommandNode.getInstance().handle(sender, args);
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
    return MainCommandNode.getInstance().tabComplete(sender, args);
  }
}
