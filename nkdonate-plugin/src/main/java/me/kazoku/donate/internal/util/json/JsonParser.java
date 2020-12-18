package me.kazoku.donate.internal.util.json;

import com.google.gson.JsonElement;

public final class JsonParser {

  private static final com.google.gson.JsonParser PARSER = new com.google.gson.JsonParser();

  public static JsonElement parseString(String json) {
    return PARSER.parse(json);
  }

}