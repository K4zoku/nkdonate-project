package me.kazoku.donate.internal.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ActionHandler {
  private final Map<String, Action> actionMap;

  public ActionHandler() {
    this.actionMap = new HashMap<>();
  }

  public void register(Action action, String identifier, String... aliases) {
    actionMap.put(identifier, action);
    for (String alias : aliases) actionMap.put(alias, action);
  }

  public Optional<Action> parse(String rawAction) {
    AtomicReference<String> identifier = new AtomicReference<>("");
    AtomicReference<String> args = new AtomicReference<>("");
    int i;
    if ((i = rawAction.indexOf(':')) > -1) {
      identifier.set(rawAction.substring(0, i));
      args.set(rawAction.substring(i + 1));
    }
    return Optional.ofNullable(actionMap.get(identifier.get()));
  }

}
