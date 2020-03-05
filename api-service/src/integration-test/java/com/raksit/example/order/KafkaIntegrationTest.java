package com.raksit.example.order;

import java.util.Map;
import kafka.server.KafkaServer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;

@EmbeddedKafka(partitions = 1, topics = { "order.created" })
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext
public abstract class KafkaIntegrationTest extends IntegrationTest {

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  public Consumer<Integer, String> consumer;

  @BeforeEach
  void setUp() {
    Map<String, Object> consumerProps = KafkaTestUtils
        .consumerProps("test-group", "true", embeddedKafkaBroker);
    consumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    ConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<>(
        consumerProps, new IntegerDeserializer(), new StringDeserializer());
    consumer = consumerFactory.createConsumer();
  }

  @AfterEach
  void tearDown() {
    consumer.close();
  }

  @AfterAll
  void afterAll() {
    embeddedKafkaBroker.getKafkaServers().forEach(KafkaServer::shutdown);
    embeddedKafkaBroker.getKafkaServers().forEach(KafkaServer::awaitShutdown);
  }
}
