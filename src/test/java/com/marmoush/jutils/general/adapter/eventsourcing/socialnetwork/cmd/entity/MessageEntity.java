package com.marmoush.jutils.general.adapter.eventsourcing.socialnetwork.cmd.entity;

import com.marmoush.jutils.general.adapter.eventsourcing.socialnetwork.cmd.value.Message;
import com.marmoush.jutils.general.domain.entity.Entity;

public class MessageEntity extends Entity<Message> {
  public MessageEntity(String id, Message value) {
    super(id, value);
  }
}