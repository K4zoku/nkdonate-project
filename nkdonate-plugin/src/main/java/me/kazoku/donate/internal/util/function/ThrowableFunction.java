package me.kazoku.donate.internal.util.function;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowableFunction<T, R> extends Function<T, R> {
  static <T, R> Function<T, R> throwableFn(ThrowableFunction<T, R> fn) {
    return fn;
  }

  @Override
  default R apply(T t) {
    try {
      return applyT(t);
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }

  R applyT(T t) throws Throwable;
}
