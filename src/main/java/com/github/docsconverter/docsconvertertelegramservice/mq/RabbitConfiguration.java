package com.github.docsconverter.docsconvertertelegramservice.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class RabbitConfiguration {
    public static final String CONVERT_QUEUE = "convert";
    public static final String CONVERT_RESULT_QUEUE = "convert_result";

    @Bean
    public ConnectionFactory connectionFactory() {
        try {
            return new CachingConnectionFactory(new URI(System.getenv("CLOUDAMQP_URL")));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue convertQueue() {
        return new Queue(CONVERT_QUEUE);
    }

    @Bean
    public Queue convertResultQueue() {
        return new Queue(CONVERT_RESULT_QUEUE);
    }
}
