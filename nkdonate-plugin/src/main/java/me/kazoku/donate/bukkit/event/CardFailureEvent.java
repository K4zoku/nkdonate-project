package me.kazoku.donate.bukkit.event;

import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CardFailureEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final Card card;
  private final String msg;

  public CardFailureEvent(Card card, String msg) {
    super();
    this.card = card;
    this.msg = msg;
  }

  public Card getCard() {
    return card;
  }

  public String getMsg() {
    return msg;
  }

  @Override
  public String getEventName() {
    return getClass().getSimpleName();
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

}
