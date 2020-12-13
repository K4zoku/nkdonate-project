package me.kazoku.donate.internal.util.logging;

import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DebugWriter extends Handler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final List<String> logs;
    private final File logFile;
    private File tempFile = null;
    private FileWriter writer;
    private boolean autoFlush;

    public DebugWriter(File logFile, boolean autoFlush) throws IOException {
        if (!logFile.getParentFile().exists() || !logFile.exists()) tempFile = File.createTempFile("debug", "log");
        this.logs = new LinkedList<>();
        this.logFile = logFile;
        if (tempFile != null) this.writer = new FileWriter(tempFile, true);
        else {
            this.writer = new FileWriter(logFile, true);
            addLogEntry("Log file not exists, using temp file");
        }
        this.autoFlush = autoFlush;
    }

    private String format(String msg) {
        return String.format("[%s]: %s%n",
                DATE_FORMAT.format(System.currentTimeMillis()),
                ChatColor.stripColor(msg)
        );
    }

    private void addLogEntry(String msg) {
        logs.add(format(msg));
    }

    @Override
    public void publish(LogRecord record) {
        if (!record.getLevel().equals(Level.DEBUG)) return;
        addLogEntry(record.getMessage());
        if (autoFlush) flush();
    }

    @Override
    public void flush() {
        try {
            for (String logEntry : logs) writer.append(logEntry);
            logs.clear();
            writer.flush();
            if (tempFile != null) {
                if (!logFile.getParentFile().exists() && !logFile.getParentFile().mkdirs()) logs.add("Cannot create parent directory");
                Files.copy(tempFile.toPath(), logFile.toPath());
                if (!tempFile.delete()) addLogEntry("Cannot delete temp file!");
                tempFile = null;
                writer = new FileWriter(logFile, true);
                addLogEntry("Transfer temp file to log file completed!");
            }
        } catch (IOException ignore) {}
    }

    @Override
    public void close() throws SecurityException {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }
}
