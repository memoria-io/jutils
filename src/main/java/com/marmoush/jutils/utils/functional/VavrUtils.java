package com.marmoush.jutils.utils.functional;

import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import io.vavr.control.Try;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static java.util.Objects.requireNonNullElseGet;

public final class VavrUtils {
  private VavrUtils() {}

  public static <T, R> API.Match.Case<T, R> instanceOfCase(Class<?> c, R r) {
    return Case($(instanceOf(c)), () -> r);
  }

  public static <A extends Traversable<B>, B> Traversable<Try<B>> traversableT(Try<A> tt) {
    if (tt.isSuccess())
      return tt.get().map(Try::success);
    else
      return List.of(Try.failure(tt.getCause()));
  }

  /**
   * This can handle CompletableFuture.handle bi function
   */
  public static <T> CompletableFuture<Try<T>> toTry(CompletableFuture<T> c) {
    return c.handle((v, t) -> {
      if (v != null) {
        return Try.success(v);
      } else
        return Try.failure(requireNonNullElseGet(t, () -> new NullPointerException("Both values are null")));
    });
  }
}
