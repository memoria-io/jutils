package io.memoria.jutils.core.utils.yaml;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.ArrayList;

public record YamlConfigMap(Map<String, Object>map) {

  public YamlConfigMap(java.util.Map<String, Object> conf) {
    this(HashMap.ofAll(conf));
  }

  public Boolean asBoolean(String key) {
    return Boolean.parseBoolean(asString(key));
  }

  public String asString(String key) {
    return (String) map.get(key).get();
  }

  public Integer asInteger(String key) {
    return Integer.parseInt(asString(key));
  }

  public Long asLong(String key) {
    return Long.parseLong(asString(key));
  }

  public Double asDouble(String key) {
    return Double.parseDouble(asString(key));
  }

  public List<Boolean> asBooleanList(String key) {
    var list = asStringList(key).map(Boolean::parseBoolean);
    return List.ofAll(list);
  }

  public List<String> asStringList(String key) {
    @SuppressWarnings("unchecked")
    var list = (ArrayList<String>) map.get(key).get();
    return List.ofAll(list);
  }

  public List<Integer> asIntegerList(String key) {
    return List.ofAll(asStringList(key)).map(Integer::parseInt);
  }

  public List<Long> asLongList(String key) {
    return List.ofAll(asStringList(key)).map(Long::parseLong);
  }

  public List<Double> asDoubleList(String key) {
    return List.ofAll(asStringList(key)).map(Double::parseDouble);
  }

  public java.util.Map<String, Object> asJavaMap() {
    return asMap().toJavaMap();
  }

  public Map<String, Object> asMap() {
    return this.map;
  }

  public java.util.Map<String, Object> asJavaMap(String key) {
    return asMap(key).toJavaMap();
  }

  public Map<String, Object> asMap(String key) {
    @SuppressWarnings("unchecked")
    var m = (java.util.Map<String, Object>) map.get(key).get();
    return HashMap.ofAll(m);
  }

  public YamlConfigMap asYamlConfigMap(String key) {
    @SuppressWarnings("unchecked")
    var m = (java.util.Map<String, Object>) map.get(key).get();
    return new YamlConfigMap(m);
  }
}
