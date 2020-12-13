package me.kazoku.donate.internal.util.logging;

public final class Level extends java.util.logging.Level {

    public static final java.util.logging.Level DEBUG = new Level("DEBUG", 0xdeb49);

    private Level(String name, int value) {
        super(name, value);
    }
}
