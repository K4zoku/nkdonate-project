package me.kazoku.donate.external.api;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.NKModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class NKDonateAPI {

  public static BukkitTask runTaskAsync(Runnable runnable) {
    return Bukkit.getScheduler().runTaskAsynchronously(NKDonatePlugin.getInstance(), runnable);
  }

  public static BukkitTask runAsyncTimerTask(Runnable runnable, long delay, long period) {
    return Bukkit.getScheduler().runTaskTimerAsynchronously(NKDonatePlugin.getInstance(), runnable, delay, period);
  }

  public static NKModuleManager getModuleManager() {
    return NKDonatePlugin.getInstance().getModuleManager();
  }


}
