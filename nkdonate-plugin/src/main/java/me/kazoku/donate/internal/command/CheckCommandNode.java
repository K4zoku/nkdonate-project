package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.topup.object.Card;
import me.kazoku.donate.modular.topup.object.CardQueue;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CheckCommandNode implements CommandNode {
  private static final CheckCommandNode instance = new CheckCommandNode();
  private static final String LABEL = "check";
  private static final List<String> PERMISSIONS = Collections.singletonList("nkdonate.admin.check");

  private CheckCommandNode() {
  }

  static CheckCommandNode getInstance() {
    return instance;
  }

  @Override
  public String label() {
    return LABEL;
  }

  @Override
  public boolean onlyPlayer() {
    return false;
  }

  @Override
  public List<String> permissions() {
    return PERMISSIONS;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    CardQueue queue = NKDonatePlugin.getInstance().getQueue();
    Collection<Card> cardCollection = queue.toCollection();
    int size = cardCollection.size();
    queue.checkAll();
    sender.sendMessage(ChatColor.GREEN + "Checked " + size + " cards");
    return true;
  }
}
