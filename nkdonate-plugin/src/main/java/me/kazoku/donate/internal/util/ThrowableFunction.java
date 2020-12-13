package me.kazoku.donate.internal.util;

import java.util.function.Function;

public interface ThrowableFunction<T, R> extends Function<T, R> {
    @Override
    default R apply(T t) {
        try {
            return applyT(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    R applyT(T t) throws Exception;
}
