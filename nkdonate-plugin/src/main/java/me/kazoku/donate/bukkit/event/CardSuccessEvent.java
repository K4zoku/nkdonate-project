package me.kazoku.donate.bukkit.event;

import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CardSuccessEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final Card card;

  public CardSuccessEvent(Card card) {
    super();
    this.card = card;
  }

  public Card getCard() {
    return card;
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
