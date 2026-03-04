package com.demoapp.demoapp.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {
    @Bean
    public AdminClient adminClient(KafkaAdmin kafkaAdmin) {
        // This extracts the configuration from Spring and builds the actual Client
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }
}
