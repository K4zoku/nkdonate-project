package me.kazoku.donate.internal.util.bukkit;

import me.kazoku.donate.NKDonatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandUtils {

  private CommandUtils() {
  }

  public static void dispatchCommand(String command) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
  }

  public static void dispatchCommand(UUID playerId, String command, boolean op) {
    PlayerUtils.getPlayer(playerId).ifPresent(player -> dispatchCommand(player, command, op));
  }

  private static void dispatchCommand(Player player, String command, boolean op) {
    final boolean playerOp = player.isOp();
    if (!playerOp && op) player.setOp(true);
    // TODO: Add external placeholder replacer
    command = NKDonatePlugin.getPlaceholderCache().apply(command, "player", player.getName());
    player.chat('/' + command);
    player.setOp(playerOp);
  }
}
