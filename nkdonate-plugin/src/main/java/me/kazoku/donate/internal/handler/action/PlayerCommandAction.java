package me.kazoku.donate.internal.handler.action;

import me.kazoku.donate.internal.util.bukkit.CommandUtils;

import java.util.UUID;

public class PlayerCommandAction extends CommandAction {

  public PlayerCommandAction(String command) {
    super(command);
  }

  @Override
  protected void runCommand(UUID uuid, String command) {
    CommandUtils.dispatchCommand(uuid, command, false);
  }

}
