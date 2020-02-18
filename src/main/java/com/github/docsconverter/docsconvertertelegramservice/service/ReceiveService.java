package com.github.docsconverter.docsconvertertelegramservice.service;

import com.github.docsconverter.docsconvertertelegramservice.Bot;
import com.github.docsconverter.docsconvertertelegramservice.mq.ResultListener;
import com.github.docsconverter.docsconvertertelegramservice.to.Task;
import com.github.docsconverter.docsconvertertelegramservice.util.TaskUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.github.docsconverter.docsconvertertelegramservice.util.TaskUtil.getName;

@Service
public class ReceiveService {
    private Logger logger = LoggerFactory.getLogger(ReceiveService.class);

    private final Bot bot;

    public ReceiveService(Bot bot) {
        this.bot = bot;
    }

    public void receive(String message){
        logger.info("Received from convert_result: " + message);

        Task task = TaskUtil.deserializeToObject(message);

        if(!task.isCompleted()){
            logger.info("Task not completed: " + message);

            bot.sendMessage(task.getChatId(), "Task not completed, try later");
            return;
        }

        switch (task.getType()){
            case TEXT:
                bot.sendMessage(task.getChatId(), task.getText());
                break;
            case PHOTO:
                bot.sendPhoto(task.getChatId(), getName(task.getUrl()), task.getUrl());
                break;
            case DOCUMENT:
            case BOOK:
                bot.sendDocument(task.getChatId(), getName(task.getUrl()), task.getUrl());
        }
    }
}
