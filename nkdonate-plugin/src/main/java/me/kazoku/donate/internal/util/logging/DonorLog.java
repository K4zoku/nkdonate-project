package me.kazoku.donate.internal.util.logging;

import me.kazoku.donate.modular.topup.object.Card;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DonorLog {
  private final FileWriter writer;
  private final List<String> logEntries;

  public DonorLog(File file) throws IOException {
    writer = new FileWriter(file);
    logEntries = new LinkedList<>();
  }

  public void addEntry(Card card, String notes) {

  }
}
