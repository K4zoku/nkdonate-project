package me.kazoku.donate.internal.handler.action;

import me.kazoku.donate.internal.handler.Action;

import java.util.UUID;

public abstract class CommandAction implements Action {
  private final String command;

  protected CommandAction(String command) {
    this.command = command;
  }

  protected abstract void runCommand(UUID uuid, String command);

  @Override
  public final void doAction(UUID uuid) {
    runCommand(uuid, command);
  }
}
