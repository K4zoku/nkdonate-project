package me.kazoku.donate.internal.handler;

import me.kazoku.donate.internal.util.bukkit.CommandUtils;

import java.util.UUID;

public class PlayerCommandAction implements Action {
  @Override
  public final void doAction(UUID uuid) {
    CommandUtils.dispatchCommand(uuid, "", false);
  }
}
