package hr.rba.credit_card_client_service.configuration.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumersConfig {
  @Value(value = "${kafka.server}")
  private String server;

  @Value(value = "${kafka.consumer.group-id}")
  private String groupId;

  private <K, V> ConsumerFactory<K, V> generateFactory(
      Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer, false);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<Void, Map<String, String>>
      kafkaListenerContainerFactoryStatusChange() {
    ConcurrentKafkaListenerContainerFactory<Void, Map<String, String>> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(
        generateFactory(new VoidDeserializer(), new JsonDeserializer<>(Map.class, false)));
    return factory;
  }
}
