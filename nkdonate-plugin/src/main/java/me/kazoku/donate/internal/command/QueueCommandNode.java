package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommandNode implements CommandNode {
  private static final QueueCommandNode instance = new QueueCommandNode();

  private QueueCommandNode() {
  }

  static QueueCommandNode getInstance() {
    return instance;
  }


  @Override
  public String label() {
    return "queue";
  }

  @Override
  public boolean onlyPlayer() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    Player player = (Player) sender;

    NKDonatePlugin.getInstance().getQueue().getCardsByPlayer(player.getUniqueId()).stream()
        .map(Card::toString)
        .forEach(player::sendMessage);
    return true;
  }
}
