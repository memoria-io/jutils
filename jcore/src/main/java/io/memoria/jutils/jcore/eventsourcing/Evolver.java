package io.memoria.jutils.jcore.eventsourcing;

import io.vavr.Function2;

@FunctionalInterface
public interface Evolver extends Function2<State, Event, State> {}
