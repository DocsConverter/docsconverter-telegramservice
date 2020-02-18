package com.github.docsconverter.docsconvertertelegramservice;

import com.github.docsconverter.docsconvertertelegramservice.enums.Command;
import com.github.docsconverter.docsconvertertelegramservice.enums.FileType;
import com.github.docsconverter.docsconvertertelegramservice.service.SendService;
import com.github.docsconverter.docsconvertertelegramservice.util.BotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.github.docsconverter.docsconvertertelegramservice.enums.Command.*;
import static com.github.docsconverter.docsconvertertelegramservice.Keyboards.getPhotoKeyboard;
import static com.github.docsconverter.docsconvertertelegramservice.Keyboards.getTextKeyboard;
import static com.github.docsconverter.docsconvertertelegramservice.util.BotUtil.*;

@Component
public class Bot extends TelegramLongPollingBot {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SendService sendService;

    public Bot(SendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();

            long chatId = message.getChatId();
            Integer messageId = message.getMessageId();

            if(message.hasText()){
                String text = message.getText()
                        .toUpperCase()
                        .replace("/", "");

                log.info("CHAT_ID={} -- RECEIVE MESSAGE WITH TEXT = {}", chatId, text);

                Command command;

                try{
                    command = valueOf(text.toUpperCase());
                } catch (IllegalArgumentException e){
                    command = UNDEFINED;
                }

                switch(command){
                    case SAY:
                        sendMessage(chatId, "Hello, World!");
                        break;
                    case START:
                        sendHelp(chatId);
                        break;
                    default:
                        sendMessageReply(chatId, "Convert to:", messageId, getTextKeyboard());
                }
            } else if(message.hasPhoto()){
                log.info("CHAT_ID={} -- RECEIVE PHOTOS COUNT = {}", message.getChatId(), message.getPhoto().size());

                sendMessageReply(chatId, "Convert to:", messageId, getPhotoKeyboard());
            } else if(message.hasDocument()){
                Document document = message.getDocument();

                log.info("CHAT_ID={} -- RECEIVE DOCUMENT WITH FILE_ID = {}", chatId, document.getFileId());

                FileType type = getFileType(document.getFileName());

                switch (type){
                    case PHOTO:
                        sendMessageReply(chatId, "Convert to:", messageId, getPhotoKeyboard());
                        break;
                    default:
                        log.debug("CHAT_ID={} -- RECEIVE DOCUMENT WITH TYPE = {}, NAME = {} THIS TYPE IS'NT SUPPORTED!",
                                chatId, type, document.getFileName());

                        sendMessage(chatId, "It's file type not supported!");
                }

            }
        } else if(update.hasCallbackQuery()){
            Message message = update.getCallbackQuery().getMessage();

            long chatId = message.getChatId();

            if(message.isReply()){
                Message reply = message.getReplyToMessage();

                Command command;

                String data = update.getCallbackQuery().getData();
                try {
                    command = Command.valueOf(data.toUpperCase());
                } catch (IllegalArgumentException e){
                    log.debug("CHAT_ID={} -- RECEIVE COMMAND = {} THIS COMMAND IS'NT SUPPORTED!", chatId, data);
                    return;
                }

                log.info("CHAT_ID={} -- RECEIVE COMMAND = {}", chatId, command);

                if(command.equals(CANCEL)){
                    deleteMessage(chatId, message.getMessageId());
                    return;
                }

                if(reply.hasPhoto()){
                    List<PhotoSize> photoSizes = reply.getPhoto();

                    String fileId = photoSizes.get(photoSizes.size() - 1)
                            .getFileId();

                    sendService.sendPhoto(chatId, command, getFilePath(fileId));

                } else if(reply.hasDocument()){
                    String fileId = reply.getDocument().getFileId();
                    sendService.sendPhoto(chatId, command, getFilePath(fileId));

                } else if(reply.hasText()) {
                    sendService.sendText(chatId, command, reply.getText());
                }
            }
        }
    }

    public void sendHelp(long chatId){
        sendMessage(chatId, "DocsConverterBot convert Docs, Text and Photo to different formats");
    }

    public void deleteMessage(long chatId, int messageId){
        log.info("CHAT_ID={} -- DELETE MESSAGE ID = {}", chatId, messageId);

        try {
            execute(new DeleteMessage(chatId, messageId));
        } catch (TelegramApiException e) {
            log.info("CHAT_ID={} -- NOT DELETED MESSAGE ID = {}", chatId, messageId);
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(long chatId, String text) {
        log.info("CHAT_ID={} -- SEND MESSAGE WITH TEXT = {}", chatId, text);

        try {
            execute(getMessage(chatId, text));
        } catch (TelegramApiException e) {
            log.error("CHAT_ID={} -- NOT SEND MESSAGE WITH TEXT = {}", chatId, text);
            throw new IllegalStateException(e);
        }
    }

    public void sendMessageReply(long chatId, String text, int messageId, InlineKeyboardMarkup keyboardMarkup) {
        log.info("CHAT_ID={} -- SEND MESSAGE REPLY WITH TEXT = {}, MESSAGE_ID = {} AND KEYBOARD", chatId, text, messageId);

        try {
            execute(getMessageReply(chatId, text, messageId, keyboardMarkup));
        } catch (TelegramApiException e) {
            log.error("CHAT_ID={} -- NOT SEND MESSAGE REPLY WITH TEXT = {}, MESSAGE_ID = {} AND KEYBOARD", chatId, text, messageId);
            throw new IllegalStateException(e);
        }
    }

    public void sendPhoto(long chatId, String name, String url){
        log.info("CHAT_ID = {} -- SEND PHOTO WITH NAME = {}, URL = {}", chatId, name, url);

        try {
            execute(getPhoto(chatId, name, url));
        } catch (TelegramApiException | IOException e) {
            log.error("CHAT_ID = {} -- SEND PHOTO WITH WITH NAME = {}, URL = {}", chatId, name, url);
            throw new IllegalStateException(e);
        }
    }

    public void sendDocument(long chatId, String name, String url){
        log.info("CHAT_ID = {} -- SEND DOCUMENT WITH NAME = {}, URL = {}", chatId, name, url);

        try {
            execute(getDocument(chatId, name, url));
        } catch (TelegramApiException | IOException e) {
            log.error("CHAT_ID = {} -- ERROR SEND DOCUMENT WITH NAME = {}, URL = {}", chatId, name, url);
            throw new IllegalStateException(e);
        }
    }

    public String getFilePath(String fileId) {
        log.info("-- CREATE LINK FOR FILE_ID = {}", fileId);

        try {
            return execute(getFile(fileId))
                    .getFileUrl(getBotToken());

        } catch (TelegramApiException e) {
            log.error("-- NOT CREATED LINK FOR FILE_ID = {}", fileId);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "@docsconverterbot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_TOKEN");
    }
}