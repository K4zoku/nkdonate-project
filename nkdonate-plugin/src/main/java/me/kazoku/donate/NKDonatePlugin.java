package me.kazoku.donate;

import me.kazoku.artxe.bukkit.chat.ChatInput;
import me.kazoku.artxe.bukkit.command.CommandManager;
import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.donate.bukkit.command.NKDonateCommand;
import me.kazoku.donate.external.api.NKDonateAPI;
import me.kazoku.donate.internal.data.GeneralSettings;
import me.kazoku.donate.internal.data.Messages;
import me.kazoku.donate.internal.data.StorageStructure;
import me.kazoku.donate.internal.ui.ChatUI;
import me.kazoku.donate.internal.util.logging.CustomLogger;
import me.kazoku.donate.internal.util.logging.DebugWriter;
import me.kazoku.donate.modular.NKModuleManager;
import me.kazoku.donate.modular.topup.TopupModule;
import me.kazoku.donate.modular.topup.object.CardQueue;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.simpleyaml.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public final class NKDonatePlugin extends JavaPlugin {

  private static NKDonatePlugin instance;
  private NKDonateAPI api;
  private NKModuleManager moduleManager;
  private CardQueue queue;
  private ChatInput chatInput;
  private CustomLogger customLogger;

  public static NKDonatePlugin getInstance() {
    return instance;
  }

  private void initEnvironment() {
    ConfigurationSerialization.registerClass(ChatUI.class);

    instance = this;

    api = new NKDonateAPI() {
      @Override
      public BukkitTask runTaskAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(instance, runnable);
      }

      @Override
      public NKModuleManager getModuleManager() {
        return null;
      }
    };


    this.customLogger = new CustomLogger(getLogger(), false);
    try {
      getLogger().addHandler(
              new DebugWriter(
                      new File(StorageStructure.LOGS_DIRECTORY, "debug.log"),
                      true
              )
      );
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "An error occurred: ", e);
    }
    GeneralSettings.loadSettings(new File(StorageStructure.SETTINGS_DIRECTORY, "general.yml"));
    chatInput = new ChatInput(this);

    getCustomLogger().setDebug(GeneralSettings.DEBUG.getValue());
    moduleManager = new NKModuleManager(this);
    queue = new CardQueue(new File(StorageStructure.DATA_DIRECTORY, "queue.dat"));
  }

  public void loadModules() {
    if (!moduleManager.getLoadedModules().isEmpty()) {
      moduleManager.disableModules();
    }

    moduleManager.loadModules();
    Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
      moduleManager.enableModules();
      moduleManager.callPostEnable();
      getLogger().info(moduleManager::printListToString);
    });
  }

  private void registerCommand() {
    new CommandManager(this).register(new NKDonateCommand());
  }


  @Override
  public void onEnable() {
    initEnvironment();
    registerCommand();
    Bukkit.getScheduler().runTaskAsynchronously(this, this::loadModules);
    Bukkit.getScheduler().runTaskAsynchronously(this, queue::load);
  }

  @Override
  public void onDisable() {
    Optional.ofNullable(moduleManager).ifPresent(NKModuleManager::disableModules);
    Optional.ofNullable(queue).ifPresent(CardQueue::save);
  }

  public NKModuleManager getModuleManager() {
    return moduleManager;
  }

  public Optional<TopupModule> getTopupModule() {
    Map<String, TopupModule> topupModules = getModuleManager().getLoadedModules(TopupModule.class);
    if (topupModules.isEmpty()) return Optional.empty();
    String name = Optional.ofNullable(GeneralSettings.ENABLED_TOPUP_MODULE.getValue()).orElse("");
    return name.isEmpty() ? Optional.of(topupModules.values().iterator().next()) : Optional.ofNullable(topupModules.get(name));
  }

  public CardQueue getQueue() {
    return queue;
  }

  public CommandFeedback customFeedback() {
    return new CommandFeedback(
            Messages.UNKNOWN_COMMAND::getValue,
            Messages.ONLY_PLAYER::getValue,
            Messages.NO_PERMISSION::getValue,
            Messages.TOO_MANY_ARGS::getValue,
            Messages.TOO_FEW_ARGS::getValue
    );
  }

  public ChatInput getChatInput() {
    return chatInput;
  }

  public NKDonateAPI getApi() {
    return api;
  }

  public CustomLogger getCustomLogger() {
    return customLogger;
  }

}
