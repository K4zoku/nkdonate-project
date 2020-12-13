package me.kazoku.donate.modular.topup.object;

import com.google.gson.JsonObject;
import me.kazoku.donate.bukkit.event.CardFailureEvent;
import me.kazoku.donate.bukkit.event.CardSuccessEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class Card {

  private final Type type;
  private final Price price;
  private final String serial;
  private final String pin;
  private final UUID playerUUID;
  private String id;
  private Status status;
  private State state;

  public Card(String id, UUID playerUUID, Type type, Price price, String serial, String pin, Status status, State state) {
    this.id = id;
    this.playerUUID = playerUUID;
    this.type = type;
    this.price = price;
    this.serial = serial;
    this.pin = pin;
    this.status = status;
    this.state = state;
  }

  public Card(UUID playerUUID, Type type, Price price, String serial, String pin) {
    this(null, playerUUID, type, price, serial, pin, Status.AWAITING, State.PREPARE);
  }

  public static Card deserialize(JsonObject serialized) {
    try {

      String id = null;
      if (serialized.has("id")) id = serialized.get("id").getAsString();
      UUID uuid = UUID.fromString(serialized.get("player").getAsString());
      Type type = new Type(serialized.get("type").getAsString());
      Price price = new Price(serialized.get("price").getAsString());
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

  public UUID getPlayerUUID() {
    return playerUUID;
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
    switch (getStatus()) {
      case AWAITING:
        // Do nothing
        break;
      case SUCCESS:
        // TODO: Direct call handler and callEvent later
        Bukkit.getPluginManager().callEvent(new CardSuccessEvent(this));
        break;
      case FAILED:
        // TODO: Direct call handler and callEvent later
        Bukkit.getPluginManager().callEvent(new CardFailureEvent(this, msg));
        break;
    }
  }

  public void updateId(Supplier<String> supplier) {
    State.PREPARE.validate(this, "Unable to update id when it has been sent or checked!");
    this.id = supplier.get();
    this.state = State.SENT;
  }

  public JsonObject serialize() {
    JsonObject json = new JsonObject();
    if (id != null) json.addProperty("id", id);
    json.addProperty("player", playerUUID.toString());
    json.addProperty("type", type.getValue());
    json.addProperty("price", price.getValue());
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
      if (card == null) return false;
      return this.equals(card.getStatus());
    }
  }

  public static class Price {

    private final String value;
    private String display;

    public Price(String value, String display) {
      this.value = value;
      this.display = display;
    }

    public Price(String value) {
      this(value, value);
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

    public String getValue() {
      return value;
    }

    public String getDisplay() {
      return display;
    }

    public void setDisplay(String display) {
      this.display = display;
    }

  }

}
