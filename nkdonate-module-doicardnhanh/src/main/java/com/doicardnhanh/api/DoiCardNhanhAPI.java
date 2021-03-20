package com.doicardnhanh.api;

import me.kazoku.artxe.utils.SimpleWebUtils;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.utils.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DoiCardNhanhAPI {

    private static final String API_SERVER = "https://doicardnhanh.com/";

    private static final String TRANSACTION_URL = API_SERVER + "card_charging_api/getcard.html";
    private static final String CHECKING_URL = API_SERVER + "card_charging_api/check-status.html";

    private final Map<String, String> apiInfo;

    public DoiCardNhanhAPI(@NotNull String apiKey, @NotNull String apiSecret) {
        Validate.notEmpty(apiKey, "Missing APIkey!");
        Validate.notEmpty(apiSecret, "Missing APIsecret!");
        Map<String, String> map = new HashMap<>();
        map.put("APIkey", apiKey);
        map.put("APIsecret", apiSecret);
        apiInfo = Collections.unmodifiableMap(map);
    }

    public String createTransaction(String type, String price, String serial, String pin) {
        Validate.notEmpty(type, "Missing type!");
        Validate.notEmpty(price, "Missing price!");
        Validate.notEmpty(serial, "Missing serial!");
        Validate.notEmpty(pin, "Missing pin!");
        Map<String, String> params = new HashMap<>(apiInfo);
        params.put("type", type);
        params.put("menhgia", price);
        params.put("seri", serial);
        params.put("mathe", pin);
        return SimpleWebUtils.sendGet(TRANSACTION_URL, params);
    }

    public String checkTransaction(@NotNull String transactionId) {
        Validate.notEmpty(transactionId, "Missing transaction_id!");
        Map<String, String> params = new HashMap<>(apiInfo);
        params.put("transaction_id", transactionId);
        return SimpleWebUtils.sendGet(CHECKING_URL, params);
    }


}
