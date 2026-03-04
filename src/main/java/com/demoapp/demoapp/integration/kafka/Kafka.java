package com.demoapp.demoapp.integration.kafka;

public interface Kafka {
    /**
     * Process incoming Kafka message
     * 
     * @param content the content of the message
     */
    void processMessage(String content);

    /**
     * Send message to Kafka topic
     * 
     * @param topic the {@link String topic} to which we want to send the
     *        message
     * @param message the {@link String message} that we want to send
     */
    void sendMessage(String topic, String message);

    /**
     * Subscribe to Kafka topic
     * 
     * @param topic the {@link String topic} that we want to subscribe toP
     */
    void subscribeToTopic(String topic);

    /**
     * Unsubscribe from Kafka topic
     * 
     * @param topic the {@link String topic} that we want to unsubscribe from
     */
    void unsubscribeFromTopic(String topic);

}
