package com.marmoush.jutils.utils.functional;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.marmoush.jutils.utils.functional.VavrUtils.traversableT;

public class VavrUtilsTest {
  @Test
  public void traversableTTest() {
    Try<List<Integer>> success = Try.of(() -> List.of(1, 2, 3));
    var e = new Exception();
    Try<List<Integer>> failure = Try.failure(e);
    List<Try<Integer>> su = List.ofAll(traversableT(success));
    List<Try<Integer>> fa = List.ofAll(traversableT(failure));
    Assertions.assertEquals(List.of(Try.success(1), Try.success(2), Try.success(3)), su);
    Assertions.assertEquals(List.of(Try.failure(e)), fa);
  }
}