package me.kazoku.donate.external.api;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.NKModuleManager;
import org.bukkit.scheduler.BukkitTask;

public interface NKDonateAPI {

  static NKDonateAPI getInstance() {
    return NKDonatePlugin.getInstance().getApi();
  }

  BukkitTask runTaskAsync(Runnable runnable);

  NKModuleManager getModuleManager();
}
