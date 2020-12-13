package me.kazoku.donate.internal.util;

import java.util.function.Consumer;

public interface ThrowableConsumer<T> extends Consumer<T> {
    @Override
    default void accept(T t) {
        try {
            acceptT(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void acceptT(T t) throws Exception;
}
