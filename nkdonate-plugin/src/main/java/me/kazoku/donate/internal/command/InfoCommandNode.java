package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Arrays;
import java.util.List;

public final class InfoCommandNode implements CommandNode {

  private static final InfoCommandNode instance = new InfoCommandNode();

  private static final String LABEL = "info";
  private static final List<String> ALIASES = Arrays.asList("about", "information");

  private InfoCommandNode() {
  }

  static InfoCommandNode getInstance() {
    return instance;
  }

  public String label() {
    return LABEL;
  }

  @Override
  public List<String> aliases() {
    return ALIASES;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    final PluginDescriptionFile description = NKDonatePlugin.getInstance().getDescription();
    sender.sendMessage("§7| §aNKDonate §7\\______________________________");
    sender.sendMessage("");
    sender.sendMessage("   §aVersion:       §7" + description.getVersion());
    sender.sendMessage("   §aAuthor(s):     §7" + String.join(", ", description.getAuthors()));
    sender.sendMessage("   §aDescription:   §7" + description.getDescription());
    sender.sendMessage("§7________________________________________");
    return false;
  }

  @Override
  public CommandFeedback feedback() {
    return NKDonatePlugin.getInstance().customFeedback();
  }
}
