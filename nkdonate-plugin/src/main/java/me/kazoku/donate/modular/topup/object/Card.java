package me.kazoku.donate.modular.topup.object;

import com.google.gson.JsonObject;
import me.kazoku.donate.bukkit.event.CardFailureEvent;
import me.kazoku.donate.bukkit.event.CardSuccessEvent;
import me.kazoku.donate.internal.data.Messages;
import me.kazoku.donate.internal.util.bukkit.PlayerUtils;
import me.kazoku.donate.modular.topup.TopupModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class Card {

  private final Type type;
  private final Price price;
  private final String serial;
  private final String pin;
  private final UUID playerId;
  private String id;
  private Status status;
  private State state;

  public Card(String id, UUID playerId, Type type, Price price, String serial, String pin, Status status, State state) {
    this.id = id;
    this.playerId = playerId;
    this.type = type;
    this.price = price;
    this.serial = serial;
    this.pin = pin;
    this.status = status;
    this.state = state;
  }

  public Card(UUID playerId, Type type, Price price, String serial, String pin) {
    this(null, playerId, type, price, serial, pin, Status.AWAITING, State.PREPARE);
  }

  public static Card deserialize(JsonObject serialized) {
    try {
      String id = null;
      if (serialized.has("id")) id = serialized.get("id").getAsString();
      UUID uuid = UUID.fromString(serialized.get("player").getAsString());
      Type type = Type.fromJsonObject(serialized.get("type").getAsJsonObject());
      Price price = Price.fromJsonObject(serialized.get("price").getAsJsonObject());
      String serial = serialized.get("serial").getAsString();
      String pin = serialized.get("pin").getAsString();
      return new Card(id, uuid, type, price, serial, pin, Status.AWAITING, id == null ? State.PREPARE : State.SENT);
    } catch (Exception e) {
      return null;
    }
  }

  public Type getType() {
    return type;
  }

  public Price getPrice() {
    return price;
  }

  public String getSerial() {
    return serial;
  }

  public String getPin() {
    return pin;
  }

  public Status getStatus() {
    return status;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public String getId() {
    State.PREPARE.reject(this, "Card does not have an id when it is preparing");
    return id;
  }

  private State getState() {
    return state;
  }

  public void updateStatus(Status newStatus, String msg) {
    State.CHECKED.reject(this, "Unable to update status when it has been checked!");
    if (!Status.AWAITING.equals(this.status = newStatus)) this.state = State.CHECKED;
    Optional<Player> player = PlayerUtils.getPlayer(getPlayerId());
    switch (getStatus()) {
      case AWAITING:
        // Do nothing
        break;
      case SUCCESS:
        player.ifPresent(p -> p.sendMessage(Messages.DONATE_SUCCESS.getValue()));
        TopupModule.getInstance().ifPresent(m -> m.getRewards().giveRewards(this));
        Bukkit.getPluginManager().callEvent(new CardSuccessEvent(this));
        break;
      case FAILED:
        player.ifPresent(p -> p.sendMessage(Messages.DONATE_FAILED.getValue()));
        Bukkit.getPluginManager().callEvent(new CardFailureEvent(this, msg));
        break;
    }
  }

  public void updateId(Supplier<String> supplier) {
    State.PREPARE.validate(this, "Unable to update id when it has been sent or checked!");
    this.id = supplier.get();
    this.state = State.SENT;
  }

  public String toString() {
    return serialize().toString();
  }

  public JsonObject serialize() {
    JsonObject json = new JsonObject();
    if (id != null) json.addProperty("id", id);
    json.addProperty("player", playerId.toString());
    json.add("type", type.toJsonObject());
    json.add("price", price.toJsonObject());
    json.addProperty("serial", serial);
    json.addProperty("pin", pin);
    return json;
  }

  public enum State {
    PREPARE, SENT, CHECKED;

    public void validate(Card card, String msg) {
      if (!this.equals(card.getState())) throw new IllegalStateException(msg);
    }

    public void reject(Card card, String msg) {
      if (this.equals(card.getState())) throw new IllegalStateException(msg);
    }
  }

  public enum Status {
    SUCCESS, AWAITING, FAILED;

    public boolean inStatus(@NotNull Card card) {
      return this.equals(card.getStatus());
    }
  }

  public static class Price {

    private final String value;
    private final double numericValue;
    private String display;

    public Price(String value, double numericValue, String display) {
      this.value = value;
      this.numericValue = numericValue;
      this.display = display;
    }

    public static Price fromJsonObject(JsonObject json) {
      String value = json.get("Value").getAsString();
      double numericValue = json.get("NumericValue").getAsDouble();
      String display = Double.toString(numericValue);
      return new Price(value, numericValue, display);
    }

    public String getValue() {
      return value;
    }

    public double getNumericValue() {
      return numericValue;
    }

    public String getDisplay() {
      return display;
    }

    public void setDisplay(String display) {
      this.display = display;
    }

    public JsonObject toJsonObject() {
      JsonObject json = new JsonObject();
      json.addProperty("Value", getValue());
      json.addProperty("NumericValue", getNumericValue());
      return json;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Price)) return false;
      Price that = (Price) o;
      return Double.compare(that.numericValue, numericValue) == 0 && value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, numericValue);
    }
  }

  public static class Type {
    private final String value;
    private String display;

    public Type(String value, String display) {
      this.value = value;
      this.display = display;
    }

    public Type(String value) {
      this(value, value);
    }

    public static Type fromJsonObject(JsonObject json) {
      String value = json.get("Value").getAsString();
      return new Type(value);
    }

    public String getValue() {
      return value;
    }

    public String getDisplay() {
      return display;
    }

    public void setDisplay(String display) {
      this.display = display;
    }

    public JsonObject toJsonObject() {
      JsonObject json = new JsonObject();
      json.addProperty("Value", getValue());
      return json;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Type)) return false;
      Type that = (Type) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

}
