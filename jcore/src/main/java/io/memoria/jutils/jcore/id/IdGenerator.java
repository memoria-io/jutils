package io.memoria.jutils.jcore.id;

import java.util.function.Supplier;

@FunctionalInterface
public interface IdGenerator extends Supplier<Id> {
  @Override
  Id get();
}
