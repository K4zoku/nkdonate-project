package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommandNode implements CommandNode {
  static final QueueCommandNode INSTANCE = new QueueCommandNode();


  private QueueCommandNode() {

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
            .map(Card::getSerial)
            .forEach(player::sendMessage);
    return true;
  }
}
