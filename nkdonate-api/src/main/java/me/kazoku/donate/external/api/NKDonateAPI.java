package me.kazoku.donate.external.api;

import me.kazoku.donate.modular.NKModuleManager;
import org.bukkit.scheduler.BukkitTask;

public final class NKDonateAPI {

  private NKDonateAPI() {
    // EMPTY
  }

  public static native BukkitTask runTaskAsync(Runnable runnable);

  public static native BukkitTask runAsyncTimerTask(Runnable runnable, long delay, long period);

  public static native NKModuleManager getModuleManager();

}
