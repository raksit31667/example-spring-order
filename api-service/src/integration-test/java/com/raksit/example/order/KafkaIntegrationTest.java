package com.raksit.example.order;

import kafka.server.KafkaServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@EmbeddedKafka(partitions = 1, topics = { "order.created" })
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext
public abstract class KafkaIntegrationTest extends IntegrationTest {

  @Autowired
  protected EmbeddedKafkaBroker embeddedKafkaBroker;

  @AfterAll
  void afterAll() {
    embeddedKafkaBroker.getKafkaServers().forEach(KafkaServer::shutdown);
    embeddedKafkaBroker.getKafkaServers().forEach(KafkaServer::awaitShutdown);
  }
}
