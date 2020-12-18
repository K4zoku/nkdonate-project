package me.kazoku.donate.internal.util.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class CommandUtils {
  public static void dispatchCommand(String command) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
  }

  public static void dispatchCommand(UUID player, String command, boolean op) {
    Optional.of(Bukkit.getOfflinePlayer(player))
        .filter(OfflinePlayer::isOnline)
        .map(OfflinePlayer::getPlayer)
        .ifPresent(p -> {
              boolean playerOp = p.isOp();
              if (!playerOp && op) p.setOp(true);
              dispatchCommand(p, command);
              p.setOp(playerOp);
            }
        );
  }

  private static void dispatchCommand(Player player, String command) {
    player.chat('/' + command);
  }
}
