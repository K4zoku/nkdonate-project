package me.kazoku.donate.internal.handler;

import me.kazoku.donate.internal.util.collection.CollectionUtils;
import me.kazoku.donate.modular.topup.object.Card;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class RewardsProfile {
  private final Map<String, Map<String, List<Action>>> rewards;

  public RewardsProfile() {
    rewards = new HashMap<>();
  }

  public void load(ConfigurationSection section) {
    rewards.clear();
    rewards.put("general", new HashMap<>());
    section.getKeys(false).forEach(type ->
        section.getConfigurationSection(type).getValues(false)
            .forEach((price, rawList) ->
                CollectionUtils.objToStrList(rawList)
                    .ifPresent(list ->
                        rewards.computeIfAbsent(type, t -> new HashMap<>())
                            .put(
                                price,
                                list.stream()
                                    .map(ActionBuilder.getInstance()::build)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toList())
                            )
                    )
            )
    );
  }

  public void giveRewards(Card card) {
    final Map<String, List<Action>> generalMap = rewards.computeIfAbsent("general", s -> new HashMap<>());
    final String type = card.getType().getValue();
    final String price = card.getPrice().getValue();
    rewards.getOrDefault(type, generalMap)
        .getOrDefault(price, generalMap.getOrDefault(price, Collections.emptyList()))
        .forEach(action -> action.doAction(card.getPlayerId()));
  }
}
