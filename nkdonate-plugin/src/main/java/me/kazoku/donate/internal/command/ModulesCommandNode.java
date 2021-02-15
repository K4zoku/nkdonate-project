package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.artxe.bukkit.command.extra.SimpleCommandNode;
import me.kazoku.donate.NKDonatePlugin;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ModulesCommandNode implements CommandNode {

  private static final ModulesCommandNode instance = new ModulesCommandNode();

  private static final List<CommandNode> subcommands = Arrays.asList(
      new SimpleCommandNode(
          "reload",
          "nkdonate.admin.module.reload",
          (sender, label, args) -> {
            NKDonatePlugin.getInstance().loadModules();
            return true;
          }),
      new SimpleCommandNode(
          "list",
          "nkdonate.admin.module.list",
          (sender, label, args) -> {
            sender.sendMessage(NKDonatePlugin.getInstance().getModuleManager().printListToString());
            return true;
          }
      )
  );

  private static final List<String> ALIASES = Arrays.asList("module", "addon", "addons", "add-on");
  private static final String LABEL = "modules";
  private static final List<String> PERMISSIONS = Collections.singletonList("nkdonate.admin.module");

  private ModulesCommandNode() {
  }

  static ModulesCommandNode getInstance() {
    return instance;
  }

  @Override
  public List<String> aliases() {
    return ALIASES;
  }

  @Override
  public String label() {
    return LABEL;
  }

  @Override
  public List<String> permissions() {
    return PERMISSIONS;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    sender.sendMessage(NKDonatePlugin.getInstance().getModuleManager().printListToString());
    return true;
  }

  @Override
  public List<CommandNode> subCommands() {
    return subcommands;
  }

  @Override
  public CommandFeedback feedback() {
    return NKDonatePlugin.getInstance().customFeedback();
  }
}
