package com.doicardnhanh.api;

import me.kazoku.donate.modular.topup.object.Card;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public enum CardPrice {
  VND_10K("10000", 10000),
  VND_20K("20000", 20000),
  VND_30K("30000", 30000),
  VND_50K("50000", 50000),
  VND_100K("100000", 100000),
  VND_200K("200000", 200000),
  VND_300K("300000", 300000),
  VND_500K("500000", 500000),
  VND_1000K("1000000", 1000000),
  ;

  private final String value;
  private final int numericValue;

  CardPrice(String value, int numericValue) {
    this.value = value;
    this.numericValue = numericValue;
  }

  public static Optional<CardPrice> of(@NotNull String val) {
    return Arrays.stream(values())
        .filter(price -> price.getValue().equalsIgnoreCase(val))
        .findFirst();
  }

  public static Optional<CardPrice> of(int val) {
    return Arrays.stream(values())
        .filter(price -> price.getNumericValue() == val)
        .findFirst();
  }

  public Card.Price toGenericPrice(String display) {
    return new Card.Price(value, numericValue, display);
  }

  public Card.Price toGenericPrice() {
    return toGenericPrice(Integer.toString(numericValue));
  }

  public String getValue() {
    return value;
  }

  public int getNumericValue() {
    return numericValue;
  }


}
