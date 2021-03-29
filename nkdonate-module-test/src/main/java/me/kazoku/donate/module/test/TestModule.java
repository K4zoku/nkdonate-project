package me.kazoku.donate.module.test;

import me.kazoku.donate.external.api.NKDonateAPI;
import me.kazoku.donate.modular.NKModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TestModule extends NKModule {
  @Override
  public void onStartup() {
    NKDonateAPI.registerCommand(new Command("donate-test") {
      @Override
      public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        sender.sendMessage("TEST");
        return true;
      }
    });
  }
}
