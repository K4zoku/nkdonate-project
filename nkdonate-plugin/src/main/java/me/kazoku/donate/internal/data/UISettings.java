package me.kazoku.donate.internal.data;

import me.kazoku.artxe.configuration.path.prototype.SerializableConfigPath;
import me.kazoku.donate.internal.ui.ChatUI;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Supplier;

public class UISettings extends MemoryPathBundle {

  private static final ChatUI CHOOSE_CARD_DEFAULT = new ChatUI("§b§l{Card_Type}")
      .setHoverText(
          "§b§lCard: {Card_Type}\n" +
              "§aClick to choose price"
      )
      .setClickValue("/nkdonate choose {Card_Type_Val}");
  public static final SerializableConfigPath<ChatUI> CHOOSE_TYPE = new SerializableConfigPath<>("Choose.Type", CHOOSE_CARD_DEFAULT);
  private static final ChatUI CHOOSE_PRICE_DEFAULT = new ChatUI("§b§l{Card_Amount}")
      .setHoverText(
          "§b§lPrice: {Card_Amount}₫\n" +
              "§aClick to begin input"
      )
      .setClickValue("/nkdonate choose {Card_Type_Val} {Card_Amount_Val}");
  public static final SerializableConfigPath<ChatUI> CHOOSE_AMOUNT = new SerializableConfigPath<>("Choose.Price", CHOOSE_PRICE_DEFAULT);

  // flexible file
  private static final Supplier<File> UI_FILE = () -> new File(StorageStructure.LOCALE_DIRECTORY.get(), "ui.yml");
  private static UISettings instance;

  private UISettings(@NotNull File file) {
    super(file);
  }

  public static UISettings getInstance() {
    return instance == null ? newInstance() : instance;
  }

  private static UISettings newInstance() {
    File file = UI_FILE.get();
    String resourcePath = String.format("languages/%s/%s", GeneralSettings.LOCALE.getValue(), file.getName());
    boolean save = false;
    try {
      saveDefault(file, resourcePath);
    } catch (NullPointerException e) {
      save = true;
    }
    instance = new UISettings(file);
    if (save) instance.save();
    return instance;
  }

  public static void hardReload() {
    newInstance();
  }

  public static void softReload() {
    if (UI_FILE.get().exists()) getInstance().getConfig().reloadConfig();
    else hardReload();
  }

}
