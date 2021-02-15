package me.kazoku.donate.modular.topup.object;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.kazoku.artxe.security.BiCrypt;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.util.Strings;
import me.kazoku.donate.internal.util.file.FileUtils;
import me.kazoku.donate.internal.util.json.JsonParser;
import me.kazoku.donate.internal.util.logging.Level;
import me.kazoku.donate.modular.topup.TopupModule;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CardQueue {
  private static final BiCrypt CRYPT = new BiCrypt(NKDonatePlugin.class.getSimpleName(), NKDonatePlugin.class.getName());

  private final Queue<Card> queue;
  private final File file;

  public CardQueue(@NotNull File file) {
    queue = new LinkedList<>();
    this.file = file;
  }

  public void checkAll(Predicate<Card> filter) {
    queue.removeIf(card -> {
      if (filter.test(card)) TopupModule.getInstance().ifPresent(m -> m.checkCard(card));
      return !Card.Status.AWAITING.inStatus(card);
    });
  }

  public void checkAll(UUID uuid) {
    checkAll(card -> card.getPlayerId().equals(uuid));
  }

  public void checkAll() {
    checkAll(card -> true);
  }

  public void add(@NotNull Card card) {
    if (Card.Status.AWAITING.inStatus(card)) queue.add(card);
  }

  public Optional<Card> getCardByID(String id) {
    return queue.stream()
        .filter(card -> card.getId().equals(id))
        .findFirst();
  }

  public Collection<Card> getCardsByPlayer(UUID uuid) {
    return queue.stream()
        .filter(card -> card.getPlayerId().equals(uuid))
        .collect(Collectors.toSet());
  }

  public void loadFromFile(@NotNull File file) {
    if (!file.exists()) return;
    char[] content;
    try {
      content = new String(Files.readAllBytes(file.toPath())).toCharArray();
    } catch (IOException e) {
      NKDonatePlugin.getInstance().getLogger().log(Level.SEVERE, "An error occurred:", e);
      return;
    }
    byte[] bytes = new byte[content.length / 2];
    int i = 0;
    while (i < content.length) bytes[i / 2] = Byte.parseByte(Strings.fromChars(content[i++], content[i++]), 0x10);
    StreamSupport.stream(
        JsonParser.parseString(
            CRYPT.decrypt(new String(bytes))
        ).getAsJsonArray().spliterator(), false
    )
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(Card::deserialize)
        .forEach(queue::add);
  }

  public void saveToFile(@NotNull File file) {
    final JsonArray jsonData = new JsonArray();
    queue.stream()
        .filter(Card.Status.AWAITING::inStatus)
        .map(Card::serialize)
        .forEach(jsonData::add);

    final String data = CRYPT.encrypt(jsonData.toString());

    if (!FileUtils.create(file)) return;

    try (FileWriter writer = new FileWriter(file)) {
      for (byte datum : data.getBytes(StandardCharsets.UTF_8)) writer.append(String.format("%02x", datum));
    } catch (IOException e) {
      NKDonatePlugin.getInstance().getLogger().log(Level.SEVERE, "An error occurred:", e);
    }
  }

  public void load() {
    loadFromFile(file);
  }

  public void save() {
    saveToFile(file);
  }
}
