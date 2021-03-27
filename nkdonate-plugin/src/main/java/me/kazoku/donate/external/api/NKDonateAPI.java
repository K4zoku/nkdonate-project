package me.kazoku.donate.external.api;

import me.kazoku.artxe.bukkit.event.EventHandlerManager;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.NKModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public final class NKDonateAPI {

  private NKDonateAPI() {
    // EMPTY
  }

  public static BukkitTask runTaskAsync(Runnable runnable) {
    return Bukkit.getScheduler().runTaskAsynchronously(NKDonatePlugin.getInstance(), runnable);
  }

  public static BukkitTask runAsyncTimerTask(Runnable runnable, long delay, long period) {
    return Bukkit.getScheduler().runTaskTimerAsynchronously(NKDonatePlugin.getInstance(), runnable, delay, period);
  }

  public static NKModuleManager getModuleManager() {
    return NKDonatePlugin.getInstance().getModuleManager();
  }

  public static void registerCommand(Command command) {
    NKDonatePlugin.getInstance().getCommandManager().register(command);
  }

  public static void registerEvents(Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, NKDonatePlugin.getInstance());
  }

  public static <E extends Event> void registerEvent(Class<E> eventClass, Consumer<E> handler) {
    EventHandlerManager.getInstance(NKDonatePlugin.getInstance()).addEventHandler(eventClass, handler);
  }

}
