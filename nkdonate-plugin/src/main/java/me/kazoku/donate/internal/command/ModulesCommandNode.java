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

  static final ModulesCommandNode INSTANCE = new ModulesCommandNode();

  private static final List<CommandNode> subcommands = Collections.singletonList(
          new SimpleCommandNode(
                  "reload",
                  "nkdonate.admin.module",
                  (sender, label, args) -> {
                    NKDonatePlugin.getInstance().loadModules();
                    return true;
                  })
  );

  private ModulesCommandNode() {
  }

  @Override
  public List<String> aliases() {
    return Arrays.asList("module", "addon", "addons", "add-on");
  }

  @Override
  public String label() {
    return "modules";
  }

  @Override
  public List<String> permissions() {
    return Collections.singletonList("nkdonate.admin.module");
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
