package com.marmoush.jutils.eventsourcing.domain.port;

import com.marmoush.jutils.eventsourcing.domain.entity.QueryRequest;
import com.marmoush.jutils.eventsourcing.domain.entity.QueryResponse;
import io.vavr.Function1;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface QueryHandler extends Function1<QueryRequest, Mono<Try<QueryResponse>>> {}