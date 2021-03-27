package me.kazoku.donate;

import me.kazoku.artxe.bukkit.chat.v2.ChatInputAPI;
import me.kazoku.artxe.bukkit.command.CommandManager;
import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.plugin.shell.ShellPlugin;
import me.kazoku.artxe.utils.PlaceholderCache;
import me.kazoku.donate.bukkit.command.NKDonateCommand;
import me.kazoku.donate.internal.command.ChooseCommandNode;
import me.kazoku.donate.internal.data.GeneralSettings;
import me.kazoku.donate.internal.data.Messages;
import me.kazoku.donate.internal.data.StorageStructure;
import me.kazoku.donate.internal.data.UISettings;
import me.kazoku.donate.internal.ui.ChatUI;
import me.kazoku.donate.internal.util.logging.DebugLogger;
import me.kazoku.donate.internal.util.logging.DebugWriter;
import me.kazoku.donate.modular.NKModuleManager;
import me.kazoku.donate.modular.topup.TopupModule;
import me.kazoku.donate.modular.topup.object.CardQueue;
import org.bukkit.Bukkit;
import org.simpleyaml.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public final class NKDonatePlugin extends ShellPlugin {

  private static final PlaceholderCache PLACEHOLDER_CACHE = new PlaceholderCache();
  private static NKDonatePlugin instance;

  private NKModuleManager moduleManager;
  private CommandManager commandManager;
  private DebugLogger debugLogger;
  private CardQueue queue;

  public static synchronized NKDonatePlugin getInstance() {
    return instance;
  }

  private static synchronized void setInstance(NKDonatePlugin instance) {
    NKDonatePlugin.instance = instance;
  }

  public static PlaceholderCache getPlaceholderCache() {
    return PLACEHOLDER_CACHE;
  }

  @Override
  public void initialize() {
    setInstance(this);
    ConfigurationSerialization.registerClass(ChatUI.class);
    this.debugLogger = new DebugLogger(getLogger(), false);
    getCommandManager().register(NKDonateCommand.getInstance());
  }

  @Override
  public void load() {
    loadConfiguration();
    initDebug();
  }

  @Override
  public void startup() {
    ChatInputAPI.register(this);
  }

  @Override
  public void postStartup() {
    loadModules();
    getQueue().load();
  }

  @Override
  public void shutdown() {
    getModuleManager().disableModules();
    getQueue().save();
  }

  private void loadConfiguration() {
    GeneralSettings.hardReload();
    Messages.hardReload();
    UISettings.hardReload();
  }

  private void initDebug() {
    try {
      getLogger().addHandler(
          new DebugWriter(
              new File(StorageStructure.LOGS_DIRECTORY, "debug.log"),
              true
          )
      );
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "An error occurred while initialize debug environment: ", e);
    }
    getDebugLogger().setDebug(GeneralSettings.DEBUG.getValue());
  }

  public CommandManager getCommandManager() {
    return this.commandManager == null
        ? (this.commandManager = new CommandManager(this))
        : this.commandManager;
  }

  public NKModuleManager getModuleManager() {
    return this.moduleManager == null
        ? (this.moduleManager = new NKModuleManager(this))
        : this.moduleManager;
  }

  public void loadModules() {
    if (!getModuleManager().getLoadedModules().isEmpty()) getModuleManager().disableModules();

    getModuleManager().loadModules();
    Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
      getModuleManager().enableModules();
      getModuleManager().callPostEnable();
      ChooseCommandNode.reload();
      getTopupModule().ifPresent(t -> getDebugLogger().debug(t.getDisplayName()));
      getLogger().info(getModuleManager()::printListToString);
    });
  }

  public Optional<TopupModule> getTopupModule() {
    Map<String, TopupModule> topupModules = getModuleManager().getLoadedModules(TopupModule.class);
    if (topupModules.isEmpty()) return Optional.empty();
    final String name = GeneralSettings.ENABLED_TOPUP_MODULE.getValue();

    if (!name.isEmpty()) return Optional.ofNullable(topupModules.get(name));

    final Iterator<TopupModule> iterator = topupModules.values().iterator();
    final TopupModule first = iterator.next();
    while (iterator.hasNext()) getModuleManager().disableModule(iterator.next().getName(), true);
    return Optional.of(first);
  }

  public CardQueue getQueue() {
    return this.queue == null
        ? (this.queue = new CardQueue(new File(StorageStructure.DATA_DIRECTORY, "queue.dat")))
        : this.queue;
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

  public DebugLogger getDebugLogger() {
    return this.debugLogger;
  }

}
