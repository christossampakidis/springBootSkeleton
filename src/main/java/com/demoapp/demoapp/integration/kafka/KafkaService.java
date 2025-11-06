package com.demoapp.demoapp.integration.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;


@Service
public class KafkaService implements Kafka {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AdminClient adminClient;
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, KafkaAdmin kafkaAdmin) {
        this.kafkaTemplate = kafkaTemplate;
        this.adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    @Override
    @KafkaListener(topics = "invoices", groupId = "crm")
    public void processMessage(String content) {
        System.out.println("Received Kafka message: " + content);
    }

    @Override
    public void sendMessage(String topic, String message) {
        System.out.println("Sending message to topic [" + topic + "]: " + message);
        kafkaTemplate.send(new ProducerRecord<>(topic, message));
    }

    @Override
    public void subscribeToTopic(String topic) {
        try {
            System.out.println("Subscribing to topic: " + topic);
            NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to subscribe to topic " + topic + ": " + e.getMessage());
        }
    }

    @Override
    public void unsubscribeFromTopic(String topic) {
        try {
            System.out.println("Unsubscribing from topic: " + topic);
            adminClient.deleteTopics(Collections.singletonList(topic)).all().get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to unsubscribe from topic " + topic + ": " + e.getMessage());
        }
    }

}
