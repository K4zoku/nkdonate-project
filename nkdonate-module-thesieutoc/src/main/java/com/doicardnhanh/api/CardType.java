package com.doicardnhanh.api;

import me.kazoku.donate.modular.topup.object.Card;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum CardType {
  VIETTEL("Viettel"),
  VINAPHONE("Vinaphone"),
  MOBIFONE("Mobifone"),
  VIETNAMOBILE("Vietnamobile"),
  VCOIN("Vcoin"),
  ZING("Zing"),
  GATE("Gate"),
  GARENA("Garena");

  private final String value;

  CardType(String value) {
    this.value = value;
  }

  public static Optional<CardType> of(@NotNull String name) {
    try {
      return Optional.of(Enum.valueOf(CardType.class, name.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public Card.Type toGenericType(String display) {
    return new Card.Type(value, display);
  }

  public Card.Type toGenericType() {
    return new Card.Type(value);
  }

  public String getValue() {
    return value;
  }
}
