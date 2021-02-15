package me.kazoku.donate.modular.topup;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.handler.RewardsProfile;
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

  public abstract RewardsProfile getRewards();

  @NotNull
  public abstract Response sendCard(Card card);

  public abstract void checkCard(Card card);

}
