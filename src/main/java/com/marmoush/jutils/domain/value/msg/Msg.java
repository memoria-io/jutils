package com.marmoush.jutils.domain.value.msg;

import java.util.Objects;

public class Msg {
  public final String key;
  public final String value;

  public Msg(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Msg msg = (Msg) o;
    return key.equals(msg.key) && value.equals(msg.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }
}