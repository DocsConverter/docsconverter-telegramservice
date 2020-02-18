package com.github.docsconverter.docsconvertertelegramservice.mq;

import com.github.docsconverter.docsconvertertelegramservice.service.ReceiveService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.github.docsconverter.docsconvertertelegramservice.mq.RabbitConfiguration.CONVERT_RESULT_QUEUE;

@EnableRabbit
@Component
public class ResultListener {

    private final ReceiveService receiveService;

    public ResultListener(ReceiveService receiveService) {
        this.receiveService = receiveService;
    }

    @RabbitListener(queues = CONVERT_RESULT_QUEUE)
    public void processConvertResult(String message) {
        receiveService.receive(message);
    }
}
