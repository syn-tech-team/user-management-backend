package com.example.usermanagement.config;


import com.example.usermanagement.dto.UserEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfigs() {
        return Map.of(
                // Address of the Kafka broker(s)
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                // Serializer for message keys (String)
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,

                // Serializer for message values (JSON)
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,

//               ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class,

                // Uses default partitioning strategy (hash of key % number of partitions)
                ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class,  // This is the default,

                // Ensures exactly-once delivery semantics
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true,

                // Requires acknowledgment from all in-sync replicas
                ProducerConfig.ACKS_CONFIG, "all"
        );
    }

    // Creates a producer factory that creates Kafka producer instances
    @Bean
    public ProducerFactory<String, UserEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    // Creates a thread-safe template for sending messages
    @Bean
    public KafkaTemplate<String, UserEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Defines a Kafka topic with specific configurations
    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user.events")  // Topic name
                .partitions(3)                  // Number of partitions
                .replicas(1)                    // Number of replicas
                // Keeps the latest value for each key (compact) and deletes old data after retention
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, "compact,delete")
                // Retains data for 7 days (in milliseconds)
                .config(TopicConfig.RETENTION_MS_CONFIG, "604800000")
                .build();
    }
}
/*---
Configuration
    Tells Spring this class contains bean definitions
    Processed at startup to configure the application context
Value
    Injects values from application.properties
    :localhost:9092 provides a default if the property isn't set
ProducerFactory
    Factory for creating Kafka producer instances
    Configured to produce messages with:
        String keys
        UserEvent values (serialized to JSON)
KafkaTemplate
    Thread-safe template for sending messages
    Injected into services that need to produce messages
    Handles connection pooling and message sending
How It Works Together:
    At startup, Spring creates a KafkaTemplate bean
    The template uses the ProducerFactory to create producers
    When you send a message:
        kafkaTemplate.send("user.events", userId, userEvent);
            The userId is serialized as a String
            The userEvent is converted to JSON
            The message is sent to the specified topic

bootstrapServers
    A list of host:port pairs (comma-separated) used to establish the initial connection to the Kafka cluster
    Default: localhost:9092 (for local development)
    Why Do We Need It?
        Initial Connection Point
        Your application needs to know where to find at least one Kafka broker
        It only needs to connect to one broker initially (even in a multi-broker cluster)

Thread-safe
    a piece of code can be safely used by multiple threads at the same time without
    causing problems like data corruption or inconsistent states. Here's a simple explanation:

Topic Creation:
Automatically creates the user.events topic if it doesn't exist
        3 partitions for parallel processing
        1 replica (sufficient for local development)
Producer Setup:
    Connects to the Kafka bootstrap server
    Uses String for message keys and JSON for values
No complex retry or batching (keeps it simple)
KafkaTemplate:
Thread-safe template for sending messages
Injected into your UserEventProducer
Why This Is "Just Enough":
Simple: No complex error handling or retry logic

 */