package com.github.docsconverter.docsconvertertelegramservice.service;

import com.github.docsconverter.docsconvertertelegramservice.enums.Command;
import com.github.docsconverter.docsconvertertelegramservice.enums.FileType;
import com.github.docsconverter.docsconvertertelegramservice.to.Task;
import com.github.docsconverter.docsconvertertelegramservice.util.TaskUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.github.docsconverter.docsconvertertelegramservice.mq.RabbitConfiguration.CONVERT_QUEUE;

@Service
public class SendService {
    Logger logger = LoggerFactory.getLogger(SendService.class);

    private final RabbitTemplate template;

    public SendService(RabbitTemplate template) {
        this.template = template;
    }

    public void sendPhoto(long chatId, Command command, String filePath){
        Task task = new Task(chatId, FileType.PHOTO, command, null, filePath);
        sendTask(task);
    }

    public void sendText(long chatId, Command command, String text){
        Task task = new Task(chatId, FileType.TEXT, command, text);
        sendTask(task);
    }

    public void sendTask(Task task){
        String message = TaskUtil.serializeToJSON(task);

        logger.info("Send to convert: {}" + message);

        template.convertAndSend(CONVERT_QUEUE, message);
    }
}
