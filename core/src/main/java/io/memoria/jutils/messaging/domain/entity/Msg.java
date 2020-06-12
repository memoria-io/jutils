package io.memoria.jutils.messaging.domain.entity;

import io.memoria.jutils.core.domain.entity.Entity;
import io.vavr.control.Option;

import java.time.LocalDateTime;

public class Msg extends Entity<String> {

  public Msg(String id, String value) {
    super(id, value);
  }

  public Msg(String id, String value, LocalDateTime creationTime, Option<String> flowId) {
    super(id, value, creationTime, flowId);
  }
}