package com.marmoush.jutils.core.domain.port.crud;

import com.marmoush.jutils.core.domain.entity.Entity;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public interface EntityWriteRepo<T extends Entity<?>> {
  Mono<Try<T>> create(T t);

  Mono<Try<T>> update(T t);

  Mono<Void> delete(String id);
}