package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ForceUpdateCard implements CommandNode {

  private static final ForceUpdateCard instance = new ForceUpdateCard();
  private final CommandFeedback feedback;

  private ForceUpdateCard() {
    feedback = NKDonatePlugin.getInstance().customFeedback();
  }

  static ForceUpdateCard getInstance() {
    return instance;
  }

  @Override
  public String label() {
    return "force";
  }

  @Override
  public boolean consume() {
    return true;
  }

  @Override
  public CommandFeedback feedback() {
    return feedback;
  }

  @Override
  public List<String> permissions() {
    return Collections.singletonList("nkdonate.dev.debug");
  }

  @Override
  public boolean execute(CommandSender sender, String label, String... args) {

    if (args.length == 0) {
      feedback().TOO_FEW_ARGUMENTS.send(sender);
      return false;
    }

    final Queue<String> argq = new LinkedList<>(Arrays.asList(args));
    final Optional<Card> cardq = NKDonatePlugin.getInstance().getQueue().getCardByID(argq.poll());

    if (args.length == 1) {
      cardq.ifPresent(card -> card.updateStatus(Card.Status.SUCCESS, "Success"));
      return true;
    }

    cardq.ifPresent(card -> card.updateStatus(Card.Status.FAILED, String.join(" ", argq)));
    return true;
  }
}
