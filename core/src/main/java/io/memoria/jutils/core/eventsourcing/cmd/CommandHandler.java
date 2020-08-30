package io.memoria.jutils.core.eventsourcing.cmd;

import io.memoria.jutils.core.eventsourcing.event.Event;
import io.memoria.jutils.core.eventsourcing.event.EventStore;
import io.memoria.jutils.core.eventsourcing.event.Evolver;
import io.memoria.jutils.core.eventsourcing.state.State;
import reactor.core.publisher.Mono;

import static io.memoria.jutils.core.utils.functional.ReactorVavrUtils.toMono;

public final class CommandHandler<S extends State, E extends Event, C extends Command> {
  private final EventStore<E> store;
  private final Evolver<S, E> evolver;
  private final Decider<S, C, E> decider;
  private final S initialState;

  public CommandHandler(EventStore<E> store, Evolver<S, E> evolver, Decider<S, C, E> decider, S initialState) {
    this.store = store;
    this.evolver = evolver;
    this.decider = decider;
    this.initialState = initialState;
  }

  public Mono<Void> handle(String aggId, C cmd) {
    var eventFlux = store.stream(aggId);
    var stateMono = evolver.apply(initialState, eventFlux);
    return stateMono.flatMap(state -> toMono(decider.apply(state, cmd))).flatMap(list -> store.add(aggId, list));
  }
}
