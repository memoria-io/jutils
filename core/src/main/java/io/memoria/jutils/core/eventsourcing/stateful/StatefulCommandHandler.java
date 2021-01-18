package io.memoria.jutils.core.eventsourcing.stateful;

import io.memoria.jutils.core.eventsourcing.Command;
import io.memoria.jutils.core.eventsourcing.CommandHandler;
import io.memoria.jutils.core.eventsourcing.Decider;
import io.memoria.jutils.core.eventsourcing.Evolver;
import io.memoria.jutils.core.value.Id;
import io.vavr.control.Option;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentMap;

/**
 * A State based command handler
 */
public final class StatefulCommandHandler<S, C extends Command> implements CommandHandler<C> {
  // State 
  private final transient ConcurrentMap<Id, S> db;
  // Logic
  private final transient S initialState;
  private final Evolver<S> evolver;
  private final Decider<S, C> decider;

  public StatefulCommandHandler(ConcurrentMap<Id, S> db, S initialState, Evolver<S> evolver, Decider<S, C> decider) {
    this.db = db;
    this.initialState = initialState;
    this.evolver = evolver;
    this.decider = decider;
  }

  @Override
  public Mono<Void> apply(C cmd) {
    return Mono.fromRunnable(() -> {
      var state = Option.of(db.get(cmd.aggId())).getOrElse(initialState);
      db.putIfAbsent(cmd.aggId(), state);
      var events = decider.apply(state, cmd).get();
      var newState = evolver.apply(state, events);
      db.put(cmd.aggId(), newState);
    });
  }
}
