package com.mxs.voting.factory;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.mxs.voting.constant.QueueConstant.VOTE;

@Configuration
public class RabbitMqFactory {
    @Bean
    public Queue myQueue() {
        return new Queue(VOTE, false);
    }
}
