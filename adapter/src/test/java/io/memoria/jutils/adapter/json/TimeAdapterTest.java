package io.memoria.jutils.adapter.json;

import io.memoria.jutils.adapter.transformer.json.JsonGson;
import io.memoria.jutils.adapter.transformer.json.TimeAdapter;
import io.memoria.jutils.core.transformer.json.Json;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeAdapterTest {
  private final Json parser = new JsonGson(new TimeAdapter(DateTimeFormatter.ISO_LOCAL_TIME, ZoneOffset.UTC));
  // Given
  private final String timeJson = "\"18:04:04\"";
  private final LocalTime timeObj = LocalTime.of(18, 4, 4);

  @Test
  void deserializer() {
    // When
    LocalTime deserializedTime = parser.deserialize(timeJson, LocalTime.class).get();
    // Then
    assertEquals(timeObj, deserializedTime);
  }

  @Test
  void serializer() {
    // When
    String serializedJson = parser.serialize(timeObj).get();
    // Then
    assertEquals(timeJson, serializedJson);
  }
}
