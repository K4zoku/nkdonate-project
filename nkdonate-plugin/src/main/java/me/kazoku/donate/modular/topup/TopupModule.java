package me.kazoku.donate.modular.topup;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.modular.NKModule;
import me.kazoku.donate.modular.topup.object.Card;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class TopupModule extends NKModule {

  public static Optional<TopupModule> getInstance() {
    return NKDonatePlugin.getInstance().getTopupModule();
  }

  public abstract List<Card.Type> getCardTypes();

  public abstract List<Card.Price> getCardPrices();

  @Override
  public abstract void onEnable();

  @NotNull
  public abstract Response sendCard(Card card);

  public abstract void checkCard(Card card);

  @Override
  public final boolean onLoad() {
    verify();
    return true;
  }
}
