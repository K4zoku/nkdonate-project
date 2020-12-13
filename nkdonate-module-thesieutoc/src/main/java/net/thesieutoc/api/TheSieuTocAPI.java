package net.thesieutoc.api;

import com.google.gson.JsonObject;
import me.kazoku.artxe.utils.SimpleWebUtils;
import com.google.gson.JsonParserUpdated;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.utils.Validate;

import java.util.*;

public class TheSieuTocAPI {

    private static final String API_SERVER = "https://thesieutoc.net/";

    private static final String TRANSACTION_URL = API_SERVER + "API/transaction";
    private static final String CHECKING_URL = API_SERVER + "card_charging_api/check-status.html";

    private final String apiKey;
    private final String apiSecret;

    public TheSieuTocAPI(@NotNull String apiKey, @NotNull String apiSecret) {
        Validate.notEmpty(apiKey, "Missing APIkey!");
        Validate.notEmpty(apiSecret, "Missing APIsecret!");
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public JsonObject createTransaction(String type, String price, String serial, String pin) {
        Validate.notEmpty(type, "Missing type!");
        Validate.notEmpty(price, "Missing price!");
        Validate.notEmpty(serial, "Missing serial!");
        Validate.notEmpty(pin, "Missing pin!");
        Map<String, String> params = new HashMap<>();
        updateApiParams(params);
        params.put("type", type);
        params.put("menhgia", price);
        params.put("seri", serial);
        params.put("mathe", pin);
        return JsonParserUpdated.parseString(SimpleWebUtils.sendGet(TRANSACTION_URL, params)).getAsJsonObject();
    }

    public JsonObject checkTransaction(@NotNull String transactionId) {
        Validate.notEmpty(transactionId, "Missing transaction_id!");
        Map<String, String> params = new HashMap<>();
        updateApiParams(params);
        params.put("transaction_id", transactionId);
        return JsonParserUpdated.parseString(SimpleWebUtils.sendGet(CHECKING_URL, params)).getAsJsonObject();
    }

    private void updateApiParams(Map<String, String> params) {
        params.compute("APIkey", (k, v) -> apiKey);
        params.compute("APIsecret", (k, v) -> apiSecret);
    }


}
