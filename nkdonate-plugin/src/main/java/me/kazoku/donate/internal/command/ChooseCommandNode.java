package me.kazoku.donate.internal.command;

import me.kazoku.artxe.bukkit.command.extra.CommandFeedback;
import me.kazoku.artxe.bukkit.command.extra.CommandNode;
import me.kazoku.artxe.bukkit.command.extra.SimpleCommandNode;
import me.kazoku.donate.NKDonatePlugin;
import me.kazoku.donate.internal.data.UISettings;
import me.kazoku.donate.internal.ui.ChatUI;
import me.kazoku.donate.modular.topup.Response;
import me.kazoku.donate.modular.topup.TopupModule;
import me.kazoku.donate.modular.topup.object.Card;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChooseCommandNode implements CommandNode {

  private static final ChooseCommandNode INSTANCE = new ChooseCommandNode();

  public static ChooseCommandNode getInstance() {
    return INSTANCE;
  }

  private final CommandFeedback feedback;
  private final List<Card.Type> types;
  private final List<CommandNode> subcommands;

  private ChooseCommandNode() {
    // type feedback
    this.feedback = NKDonatePlugin.getInstance().customFeedback();
    feedback.INVALID_COMMAND.setFeedback("%arg% is not valid card type");

    // get module
    final TopupModule module = TopupModule.getInstance().orElse(null);

    // if no module loaded, empty
    if (module == null) {
      this.types = Collections.emptyList();
      this.subcommands = Collections.emptyList();
      return;
    }
    // Card type list
    this.types = module.getCardTypes();

    this.subcommands = new ArrayList<>();

    final List<Card.Price> prices = module.getCardPrices();
    final CommandFeedback priceFeedback = NKDonatePlugin.getInstance().customFeedback();
    priceFeedback.INVALID_COMMAND.setFeedback("%arg% is not valid card price");

    for (Card.Type type : types) {
      List<CommandNode> priceCommands = new ArrayList<>();
      for (Card.Price price : prices) {
        SimpleCommandNode priceNode = new SimpleCommandNode(
                price.getValue(),
                (sender, label, args) -> {
                  sender.sendMessage("Nhap so serial: ");
                  final Player player = (Player) sender;
                  AtomicReference<String> serialA = new AtomicReference<>();
                  AtomicBoolean cancelled = new AtomicBoolean(false);
                  NKDonatePlugin.getInstance().getChatInput().newChain(player)
                          .then(serial -> {
                            if (serial.equalsIgnoreCase("cancel")) {
                              sender.sendMessage("Da huy");
                              cancelled.set(true);
                              return;
                            }
                            sender.sendMessage("Ban da nhap: " + serial);
                            serialA.set(serial);
                            sender.sendMessage("Nhap ma the: ");
                          })
                          .then(pin -> {
                            if (cancelled.get()) return false;
                            if (pin.equalsIgnoreCase("cancel")) {
                              sender.sendMessage("Da huy");
                              return true;
                            }
                            sender.sendMessage("Ban da nhap: " + pin);
                            Card card = new Card(
                                    player.getUniqueId(),
                                    type,
                                    price,
                                    serialA.get(),
                                    pin);
                            Response response = module.sendCard(card);
                            if (response.isSuccess()) {
                              NKDonatePlugin.getInstance().getQueue().add(card);
                              sender.sendMessage("Thanh cong! Dang cho duyet");
                            } else {
                              sender.sendMessage("That bai!");
                              sender.sendMessage(response.getMsg());
                            }
                            // cancel chat true
                            return true;
                          });
                  // return of executor
                  return true;
                }
        );
        priceCommands.add(priceNode);
      }
      SimpleCommandNode typeNode = new SimpleCommandNode(
              type.getValue(),
              (sender, label, args) -> {
                ChatUI ui = UISettings.CHOOSE_AMOUNT.getValue()
                        .clone()
                        .addPlayer((Player) sender);
                for (Card.Price price : prices) {
                  ui.applyPlaceholder("Card_Type_Val", type.getValue())
                          .applyPlaceholder("Card_Amount_Val", price.getValue())
                          .applyPlaceholder("Card_Type", type.getDisplay())
                          .applyPlaceholder("Card_Amount", price.getDisplay())
                          .display();
                }
                return true;
              }
      );

      typeNode.setFeedback(priceFeedback);
      typeNode.setSubcommands(priceCommands);
      subcommands.add(typeNode);
    }
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
    if (types.isEmpty()) {
      sender.sendMessage("There is no module to handle this!");
      return false;
    }
    ChatUI ui = UISettings.CHOOSE_TYPE.getValue()
            .clone()
            .addPlayer((Player) sender);
    for (Card.Type cardType : types) {
      ui.applyPlaceholder("Card_Type_Val", cardType.getValue())
              .applyPlaceholder("Card_Type", cardType.getDisplay())
              .display();
    }
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
}
