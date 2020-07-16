package io.memoria.jutils.messaging.adapter.kafka;

import io.memoria.jutils.core.utils.file.local.LocalFileReader;
import io.memoria.jutils.core.utils.file.FileReader;
import io.memoria.jutils.core.utils.file.YamlConfigMap;
import io.memoria.jutils.messaging.domain.Message;
import io.memoria.jutils.messaging.domain.MessageFilter;
import io.memoria.jutils.messaging.domain.port.MsgReceiver;
import io.memoria.jutils.messaging.domain.port.MsgSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Random;

import static io.memoria.jutils.core.utils.file.FileReader.resourcePath;
import static io.memoria.jutils.messaging.adapter.kafka.KafkaUtils.consumer;
import static io.memoria.jutils.messaging.adapter.kafka.KafkaUtils.producer;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;

public class KafkaIT {
  private static final FileReader reader = new LocalFileReader(Schedulers.boundedElastic());
  private static final YamlConfigMap configs = requireNonNull(reader.yaml(resourcePath("kafka.yaml").get()).block());
  private static final MessageFilter messageFilter = new MessageFilter("topic-" + new Random().nextInt(1000), 0, 0);
  private static final int MSG_COUNT = 10;

  private final MsgSender msgSender;
  private final MsgReceiver msgReceiver;
  private final Flux<Message> infiniteMsgsFlux;
  private final Flux<Message> limitedMsgsFlux;
  private final Message[] limitedMsgsArr;

  public KafkaIT() {
    msgSender = new KafkaSender(producer(configs), messageFilter, Schedulers.boundedElastic(), ofSeconds(1));
    msgReceiver = new KafkaReceiver(consumer(configs), messageFilter, Schedulers.boundedElastic(), ofSeconds(1));
    infiniteMsgsFlux = Flux.interval(ofMillis(100)).map(this::iToMessage);
    limitedMsgsFlux = infiniteMsgsFlux.take(MSG_COUNT);
    limitedMsgsArr = requireNonNull(limitedMsgsFlux.collectList().block()).toArray(new Message[0]);
  }

  @Test
  @DisplayName("Consumed messages should be same as published ones, and In same order")
  public void kafkaPubSub() {
    var publisher = msgSender.apply(limitedMsgsFlux);
    var consumer = msgReceiver.get().take(MSG_COUNT);
    StepVerifier.create(publisher).expectNextCount(MSG_COUNT).expectComplete().verify();
    StepVerifier.create(consumer).expectNext(limitedMsgsArr).expectComplete().verify();
  }

  private Message iToMessage(long i) {
    return new Message("Msg number" + i).withId(i);
  }
}

