package com.nap1s.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.kazoku.donate.internal.util.json.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.utils.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static me.kazoku.artxe.utils.SimpleWebUtils.sendGet;

public class TheCaoFastAPI {

  private static final String API_SERVER = "https://thecaofast.net/API/v4/?method=";

  private static final String TRANSACTION_URL = API_SERVER + "charging";
  private static final String CHECKING_URL = API_SERVER + "check";
  private static final String USER_AGENT = "Mozilla/5.0";

  private final Map<String, String> apiInfo;
  private final Logger logger;

  public TheCaoFastAPI(@NotNull String apiKey, @NotNull String apiSecret, Logger logger) {
    Validate.notEmpty(apiKey, "Missing APIkey!");
    Validate.notEmpty(apiSecret, "Missing APIsecret!");
    Validate.notNull(logger, "Missing Logger!");
    Map<String, String> map = new HashMap<>();
    map.put("APIKey", apiKey);
    map.put("APISecret", apiSecret);
    this.apiInfo = Collections.unmodifiableMap(map);
    this.logger = logger;
  }

  public JsonObject createTransaction(String type, String price, String serial, String pin) {
    Validate.notEmpty(type, "Missing type!");
    Validate.notEmpty(price, "Missing price!");
    Validate.notEmpty(serial, "Missing serial!");
    Validate.notEmpty(pin, "Missing pin!");
    Map<String, String> params = new HashMap<>(apiInfo);
    params.put("CardType", type);
    params.put("CardAmount", price);
    params.put("CardSerial", serial);
    params.put("CardPin", pin);
    try {
      String raw = sendGet(TRANSACTION_URL, params, USER_AGENT, 3, logger);
      return JsonParser.parseString(raw).getAsJsonObject().get("result").getAsJsonObject();
    } catch (IllegalStateException | JsonParseException e) {
      throw new IllegalStateException();
    }
  }

  public JsonObject checkTransaction(@NotNull String transactionId) {
    Validate.notEmpty(transactionId, "Missing transaction_id!");
    Map<String, String> params = new HashMap<>(apiInfo);
    params.put("Transaction_ID", transactionId);
    try {
      String raw = sendGet(CHECKING_URL, params, USER_AGENT, 3, logger);
      return JsonParser.parseString(raw).getAsJsonObject().get("result").getAsJsonObject();
    } catch (IllegalStateException | JsonParseException e) {
      throw new IllegalStateException(e);
    }
  }


}
