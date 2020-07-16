package io.memoria.jutils.etcd;

import io.etcd.jetcd.Client;
import io.memoria.jutils.core.utils.file.local.LocalFileReader;
import io.memoria.jutils.core.utils.file.FileReader;
import io.memoria.jutils.core.utils.file.YamlConfigMap;
import org.junit.jupiter.api.Test;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Random;

import static io.memoria.jutils.core.utils.file.FileReader.resourcePath;
import static io.vavr.API.Some;
import static io.vavr.control.Option.none;

public class EtcdIT {
  private static final FileReader reader = new LocalFileReader(Schedulers.boundedElastic());
  private static final YamlConfigMap config = reader.yaml(resourcePath("etcd.yaml").get()).block();

  private final Client clientBuilt;
  private final EtcdStoreClient client;
  private final String keyPrefix = "myKey";
  private final String value = "myValue";

  public EtcdIT() {
    clientBuilt = Client.builder().endpoints(config.asString("etcd.url").get()).build();
    client = new EtcdStoreClient(clientBuilt);
  }

  @Test
  public void deletionTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key, value)).expectComplete().verify();
    StepVerifier.create(client.delete(key)).expectComplete().verify();
    StepVerifier.create(client.get(key)).expectNext(none()).expectComplete().verify();
  }

  @Test
  public void getMapTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key + "0", value)).expectComplete().verify();
    StepVerifier.create(client.put(key + "1", value)).expectComplete().verify();
    StepVerifier.create(client.put(key + "2", value)).expectComplete().verify();
    StepVerifier.create(client.put(key + "3", value)).expectComplete().verify();
    StepVerifier.create(client.getAllWithPrefix(key))
                .expectNextMatches(m -> m.get(key + "0").get().equals(value) && m.get(key + "1").get().equals(value) &&
                                        m.get(key + "2").get().equals(value) && m.get(key + "3").get().equals(value));
  }

  @Test
  public void getTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key, value)).expectComplete().verify();
    StepVerifier.create(client.get(key)).expectNext(Some(value)).expectComplete().verify();
  }

  @Test
  public void putTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key, value)).expectComplete().verify();
    StepVerifier.create(client.put(key, "new_value")).expectComplete().verify();
  }
}
