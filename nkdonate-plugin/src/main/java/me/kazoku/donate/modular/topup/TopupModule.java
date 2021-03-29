package me.kazoku.donate.modular.topup;

import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.data.GeneralSettings;
import me.kazoku.donate.internal.handler.RewardsProfile;
import me.kazoku.donate.modular.NKModule;
import me.kazoku.donate.modular.topup.object.Card;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class TopupModule extends NKModule {

  @Override
  public final boolean onLoad() {
    super.onLoad();
    String enabledModule = GeneralSettings.ENABLED_TOPUP_MODULE.getValue();
    return enabledModule.equals(getName()) ||
        (enabledModule.isEmpty() && NKDonatePlugin.getInstance().getModuleManager().getLoadedModules(TopupModule.class).isEmpty());

  }

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
