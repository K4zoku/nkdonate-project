package net.thesieutoc.api;

import me.kazoku.donate.modular.topup.object.Card;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum CardType {
  MOBIFONE("11"),
  VINAPHONE("12"),
  VIETTEL("16"),
  VIETNAMOBILE("27"),
  ZING("28"),
  GATE("29");

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
