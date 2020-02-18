package com.github.docsconverter.docsconvertertelegramservice.mq;

import com.github.docsconverter.docsconvertertelegramservice.service.ReceiveService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class ResultListener {

    private final ReceiveService receiveService;

    public ResultListener(ReceiveService receiveService) {
        this.receiveService = receiveService;
    }

    @RabbitListener(queues = "convert_result")
    public void processConvertResult(String message) {
        receiveService.receive(message);
    }
}
