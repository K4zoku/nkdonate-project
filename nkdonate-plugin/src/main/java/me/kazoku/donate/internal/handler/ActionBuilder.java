package me.kazoku.donate.internal.handler;

import me.kazoku.donate.internal.handler.action.ConsoleCommandAction;
import me.kazoku.donate.internal.handler.action.OpCommandAction;
import me.kazoku.donate.internal.handler.action.PlayerCommandAction;
import me.kazoku.donate.internal.util.Builder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ActionBuilder extends Builder<String, Action> {

  private static final ActionBuilder instance = new ActionBuilder();

  private ActionBuilder() {
    registerDefaults();
  }

  public static ActionBuilder getInstance() {
    return instance;
  }

  private void registerDefaults() {
    register(OpCommandAction::new, "op");
    register(ConsoleCommandAction::new, "console");
    register(PlayerCommandAction::new, "player", "");
  }

  public Optional<Action> build(String raw) {
    AtomicReference<String> identifier = new AtomicReference<>("");
    AtomicReference<String> data = new AtomicReference<>("");
    int index = raw.indexOf(':');
    if (index != -1) {
      identifier.set(raw.substring(0, index));
      data.set(raw.substring(index + 1));
    }
    return build(identifier.get(), data.get());
  }

}
