package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.prototype.SerializableConfigPath;
import me.kazoku.donate.internal.ui.ChatUI;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class UISettings extends MemoryData {

  private static final ChatUI CHOOSE_PRICE_DEFAULT = new ChatUI("§b§l{Card_Amount}")
          .setHoverText(
                  "§b§lMệnh giá {Card_Amount}₫\n" +
                          "§aClick vào để bắt đầu nạp"
          )
          .setClickValue("/donate choose {Card_Type_Val} {Card_Amount_Val}");
  private static final ChatUI CHOOSE_CARD_DEFAULT = new ChatUI("§b§l{Card_Type}")
          .setHoverText(
                  "§b§lNạp thẻ {Card_Type}\n" +
                          "§aClick vào để chọn mệnh giá"
          )
          .setClickValue("/donate choose {Card_Type_Val}");
  public static final SerializableConfigPath<ChatUI> CHOOSE_TYPE = new SerializableConfigPath<>("Choose-Card-Type", CHOOSE_CARD_DEFAULT);
  public static final SerializableConfigPath<ChatUI> CHOOSE_AMOUNT = new SerializableConfigPath<>("Choose-Card-Price", CHOOSE_PRICE_DEFAULT);

  public UISettings(@NotNull File file) {
    super(file);
  }

  static void loadUi(File file) {
    saveDefault(file,
        String.format("languages/%s/ui/%s", GeneralSettings.LOCALE.getValue(), file.getName())
    );
    new UISettings(file);
  }

}
