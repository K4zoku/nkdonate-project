package me.kazoku.donate.module.topup.thesieutoc;

import com.google.gson.JsonObject;
import me.kazoku.donate.modular.topup.Response;
import me.kazoku.donate.modular.topup.TopupModule;
import me.kazoku.donate.modular.topup.object.Card;
import net.thesieutoc.api.TheSieuTocAPI;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.Configuration;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.util.*;

public class TheSieuTocModule extends TopupModule {

  private static final List<Card.Type> CARD_TYPES = new ArrayList<>();
  private static final List<Card.Price> CARD_PRICES = new ArrayList<>();
  private TheSieuTocAPI theSieuTocApi;

  @Override
  public void onEnable() {
    saveDefaults();
    loadConfig();
  }

  private void saveDefaults() {
    Configuration config = getConfig();
    Optional.ofNullable(getClassLoader().getResourceAsStream("config.yml"))
            .map(YamlConfiguration::loadConfiguration)
            .ifPresent(config::setDefaults);
    config.options().copyDefaults(true);
    saveConfig();
  }

  private void loadConfig() {
    final Configuration config = getConfig();
    final String apiKey = config.getString("APIKey", "");
    final String apiSecret = config.getString("APISecret", "");
    theSieuTocApi = new TheSieuTocAPI(apiKey, apiSecret);

    CARD_TYPES.clear();
    final ConfigurationSection cardTypes = config.getConfigurationSection("Card.Type");
    cardTypes.getKeys(false)
            .forEach(type ->
                    CARD_TYPES.add(new Card.Type(type, cardTypes.getString(type, type)))
            );

    CARD_PRICES.clear();
    final ConfigurationSection cardPrices = config.getConfigurationSection("Card.Price");
    cardPrices.getKeys(false)
            .forEach(price ->
                    CARD_PRICES.add(new Card.Price(price, cardPrices.getString(price, price)))
            );
  }

  @NotNull
  @Override
  public Response sendCard(Card card) {
    JsonObject json = theSieuTocApi.createTransaction(
            card.getType().getValue(),
            card.getPrice().getValue(),
            card.getSerial(),
            card.getPin()
    );
    boolean success = json.get("status").getAsString().equals("00");
    if (success) card.updateId(json.get("transaction_id")::getAsString);
    return new Response(success, json.get("msg").getAsString());
  }

  @Override
  public void checkCard(Card card) {
    JsonObject json = theSieuTocApi.checkTransaction(card.getId());
    Card.Status status;
    switch (json.get("status").getAsString()) {
      case "00":
        status = Card.Status.SUCCESS;
        break;
      case "-9":
        status = Card.Status.AWAITING;
        break;
      default:
        status = Card.Status.FAILED;
        break;
    }
    card.updateStatus(status, json.get("msg").getAsString());
  }

  @Override
  public List<Card.Type> getCardTypes() {
    return CARD_TYPES;
  }

  @Override
  public List<Card.Price> getCardPrices() {
    return CARD_PRICES;
  }
}
