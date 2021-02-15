package me.kazoku.donate.internal.handler.action;

import me.kazoku.donate.internal.util.bukkit.CommandUtils;

import java.util.UUID;

public class ConsoleCommandAction extends CommandAction {

  public ConsoleCommandAction(String command) {
    super(command);
  }

  @Override
  protected void runCommand(UUID uuid, String command) {
    CommandUtils.dispatchCommand(command);
  }

}
