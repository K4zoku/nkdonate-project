package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.chat.v2.ChatInputAPI;
import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.artxe.bukkit.command.extra.SimpleCommandNode;
import me.kazoku.artxe.utils.PlaceholderCache;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.data.GeneralSettings;
import me.kazoku.donate.internal.data.Messages;
import me.kazoku.donate.internal.data.UISettings;
import me.kazoku.donate.internal.ui.ChatUI;
import me.kazoku.donate.modular.topup.Response;
import me.kazoku.donate.modular.topup.TopupModule;
import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ChooseCommandNode implements CommandNode {

  private static final DecimalFormat DF = new DecimalFormat("0.#");
  private static ChooseCommandNode instance = new ChooseCommandNode();
  private final CommandFeedback feedback;
  private final List<Card.Type> types;
  private final List<Card.Price> prices;
  private final List<CommandNode> subcommands;
  private final TopupModule module;

  private ChooseCommandNode() {
    final NKDonatePlugin main = NKDonatePlugin.getInstance();
    final PlaceholderCache placeholderCache = NKDonatePlugin.getPlaceholderCache();
    // type feedback
    this.feedback = main.customFeedback();
    feedback.INVALID_COMMAND.setFeedback(placeholderCache.apply(Messages.NVT.getValue(), "0", "%arg%"));

    // get module
    this.module = TopupModule.getInstance().orElse(null);
    // if no module loaded, empty
    if (module == null) {
      this.types = new ArrayList<>();
      this.prices = new ArrayList<>();
      this.subcommands = new ArrayList<>();
      return;
    }
    // Card type list
    this.types = module.getCardTypes();

    this.subcommands = new ArrayList<>();

    this.prices = module.getCardPrices();
    final CommandFeedback priceFeedback = main.customFeedback();
    priceFeedback.INVALID_COMMAND.setFeedback(placeholderCache.apply(Messages.NVT.getValue(), "0", "%arg%"));
    for (Card.Type type : types) {
      SimpleCommandNode typeNode = new SimpleCommandNode(
          type.getValue(),
          (sender, label, args) -> {
            choosePrice(type, sender);
            return true;
          }
      );

      List<CommandNode> priceCommands = new ArrayList<>();
      for (Card.Price price : prices) {
        SimpleCommandNode priceNode =
            new SimpleCommandNode(
                DF.format(
                    price.getNumericValue()
                ),
                (sender, label, args) -> {
                  inputCard(type, price, sender);
                  return true;
                }
            );
        priceCommands.add(priceNode);
      }

      typeNode.setFeedback(priceFeedback);
      typeNode.setSubcommands(priceCommands);
      subcommands.add(typeNode);
    }
  }

  public static ChooseCommandNode getInstance() {
    return instance;
  }

  public static void reload() {
    instance = new ChooseCommandNode();
  }

  private boolean isNotCancelWord(String txt) {
    return GeneralSettings.CANCEL_WORDS.getValue().stream().parallel().noneMatch(txt::equalsIgnoreCase);
  }

  @Override
  public String label() {
    return "choose";
  }

  @Override
  public boolean onlyPlayer() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String[] args) {
    if (NKDonatePlugin.getInstance().getModuleManager().getLoadedModules(TopupModule.class).isEmpty()) {
      sender.sendMessage(Messages.NO_TOPUP_MODULE.getValue());
      return false;
    }
    chooseType(sender);
    return true;
  }

  @Override
  public CommandFeedback feedback() {
    return feedback;
  }

  @Override
  public List<CommandNode> subCommands() {
    return subcommands;
  }

  private void chooseType(CommandSender sender) {
    ChatUI chatUi = UISettings.CHOOSE_TYPE.getValue().clone();
    for (Card.Type cardType : types) {
      chatUi.applyPlaceholder("Card_Type_Val", cardType.getValue())
          .applyPlaceholder("Card_Type", cardType.getDisplay())
          .display((Player) sender);
    }
  }

  private void choosePrice(Card.Type type, CommandSender sender) {
    ChatUI chatUi = UISettings.CHOOSE_AMOUNT.getValue().clone();
    for (Card.Price price : this.prices) {
      chatUi.applyPlaceholder("Card_Type_Val", type.getValue())
          .applyPlaceholder("Card_Amount_Val", DF.format(price.getNumericValue()))
          .applyPlaceholder("Card_Type", type.getDisplay())
          .applyPlaceholder("Card_Amount", price.getDisplay())
          .display((Player) sender);
    }
  }

  private void inputCard(Card.Type type, Card.Price price, CommandSender sender) {
    sender.sendMessage(Messages.ENTER_CARD_SERIAL.getValue());
    final UUID playerId = ((Player) sender).getUniqueId();
    final AtomicReference<String> atomicSerial = new AtomicReference<>();
    ChatInputAPI.newChain(playerId)
        .criteria(this::isNotCancelWord)
        .onFalse(cancelWord -> sender.sendMessage(Messages.CANCELLED.getValue()))
        .onTrue(serial -> {
          String msg = Messages.SERIAL_ENTERED.getValue();
          msg = NKDonatePlugin.getPlaceholderCache().apply(msg, "serial", serial);
          sender.sendMessage(msg);
          atomicSerial.set(serial);
          sender.sendMessage(Messages.ENTER_CARD_PIN.getValue());
        })
        .next()
        .criteria(this::isNotCancelWord)
        .onFalse(cancelWord -> sender.sendMessage(Messages.CANCELLED.getValue()))
        .onTrue(pin -> {
          String msg = Messages.PIN_ENTERED.getValue();
          msg = NKDonatePlugin.getPlaceholderCache().apply(msg, "pin", pin);
          sender.sendMessage(msg);
          final String serial = atomicSerial.get();
          final Card card = new Card(playerId, type, price, serial, pin);
          submitCard(card);
        });
  }

  private void submitCard(Card card) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(card.getPlayerId());
    List<String> messages = new LinkedList<>();
    Response response = module.sendCard(card);

    if (response.isSuccess()) {
      final NKDonatePlugin main = NKDonatePlugin.getInstance();
      main.getDebugLogger().debug(String.format("Player %s has submitted a card: %s", player.getName(), card));
      main.getQueue().add(card);
      messages.add(Messages.SEND_SUCCESS.getValue());
    } else {
      messages.add(Messages.SEND_FAILED.getValue());
      messages.add(response.getMsg());
    }

    Optional.of(player)
        .filter(OfflinePlayer::isOnline)
        .map(OfflinePlayer::getPlayer)
        .ifPresent(p -> messages.forEach(p::sendMessage));
  }

}
