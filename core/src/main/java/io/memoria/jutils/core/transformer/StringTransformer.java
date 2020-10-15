package io.memoria.jutils.core.transformer;

import io.vavr.control.Try;

public interface StringTransformer {
  <T> Try<T> deserialize(String str, Class<T> tClass);

  <T> Try<String> serialize(T t);
}
