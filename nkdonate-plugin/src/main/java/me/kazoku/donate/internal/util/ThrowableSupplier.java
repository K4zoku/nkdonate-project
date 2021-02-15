package me.kazoku.donate.internal.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowableSupplier<T> extends Supplier<T> {
  static <T> Supplier<T> throwableSupplier(ThrowableSupplier<T> supplier) {
    return supplier;
  }

  @Override
  default T get() {
    try {
      return getT();
    } catch (Throwable e) {
      return null;
    }
  }

  T getT() throws Throwable;
}
