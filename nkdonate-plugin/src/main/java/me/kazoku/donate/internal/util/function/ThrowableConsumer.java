package me.kazoku.donate.internal.util.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowableConsumer<T> extends Consumer<T> {
  @Override
  default void accept(T t) {
    try {
      acceptT(t);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  void acceptT(T t) throws Throwable;
}
