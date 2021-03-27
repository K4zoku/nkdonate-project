package com.nap1s.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.kazoku.artxe.utils.SimpleWebUtils;
import me.kazoku.donate.internal.util.json.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.utils.Validate;

import java.util.logging.Logger;

public class Nap1SAPI {

  private static final String API_SERVER = "https://api.nap1s.com/";

  private final String apiKey;
  private final Logger logger;

  public Nap1SAPI(@NotNull String apiKey, Logger logger) {
    Validate.notEmpty(apiKey, "Missing APIkey!");
    Validate.notNull(logger, "Missing Logger!");
    this.apiKey = apiKey;
    this.logger = logger;
  }

  public JsonObject createTransaction(String type, String price, String serial, String pin) {
    Validate.notEmpty(type, "Missing type!");
    Validate.notEmpty(price, "Missing price!");
    Validate.notEmpty(serial, "Missing serial!");
    Validate.notEmpty(pin, "Missing pin!");
    JsonObject data = new JsonObject();
    data.addProperty("APIKey", apiKey);
    data.addProperty("method", "recharge");
    data.addProperty("type", type);
    data.addProperty("amount", price);
    data.addProperty("serial", serial);
    data.addProperty("code", pin);
    try {
      String raw = SimpleWebUtils.sendRawPost(API_SERVER, data.toString(), "", "application/json", 3, logger);
      return JsonParser.parseString(raw).getAsJsonObject();
    } catch (IllegalStateException | JsonParseException e) {
      throw new IllegalStateException(e);
    }
  }

  public JsonObject checkTransaction(@NotNull String transactionId) {
    Validate.notEmpty(transactionId, "Missing transaction_id!");
    JsonObject data = new JsonObject();
    data.addProperty("APIKey", apiKey);
    data.addProperty("transactionId", transactionId);
    try {
      String raw = SimpleWebUtils.sendRawPost(API_SERVER, data.toString(), "", "application/json", 3, logger);
      return JsonParser.parseString(raw).getAsJsonObject();
    } catch (IllegalStateException | JsonParseException e) {
      throw new IllegalStateException(e);
    }
  }


}
