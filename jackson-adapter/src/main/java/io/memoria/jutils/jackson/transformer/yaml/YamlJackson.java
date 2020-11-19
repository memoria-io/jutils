package io.memoria.jutils.jackson.transformer.yaml;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.memoria.jutils.core.transformer.PropertyException;
import io.memoria.jutils.core.transformer.yaml.Yaml;
import io.vavr.control.Try;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

public record YamlJackson(ObjectMapper mapper) implements Yaml {

  @Override
  public <T> Try<T> deserialize(String str, Class<T> tClass) {
    //noinspection unchecked
    return Try.of(() -> mapper.readValue(str, tClass))
              .mapFailure(Case($(instanceOf(JacksonException.class)), e -> new PropertyException(e.getMessage())));
  }

  @Override
  public <T> Try<String> serialize(T t) {
    return Try.of(() -> mapper.writeValueAsString(t));
  }
}