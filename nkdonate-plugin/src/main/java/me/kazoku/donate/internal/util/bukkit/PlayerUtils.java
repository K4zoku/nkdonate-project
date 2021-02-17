package me.kazoku.donate.internal.util.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerUtils {

  private PlayerUtils() {
  }

  public static Optional<Player> getPlayer(UUID playerUUID) {
    return Optional.of(Bukkit.getOfflinePlayer(playerUUID))
        .filter(OfflinePlayer::isOnline)
        .map(OfflinePlayer::getPlayer);
  }
}
