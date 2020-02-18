package com.github.docsconverter.docsconvertertelegramservice.util;

import com.github.docsconverter.docsconvertertelegramservice.enums.FileType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

public class BotUtil {

    public static InlineKeyboardMarkup setInline(List<List<InlineKeyboardButton>> buttons) {
        return new InlineKeyboardMarkup().setKeyboard(buttons);
    }

    public static InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData){
        return new InlineKeyboardButton(text).setCallbackData(callbackData);
    }

    public static FileType getFileType(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return FileType.getFileType(extension);
    }

    public static SendMessage getMessage(long chatId, String text) {
        return new SendMessage(chatId, text);
    }

    public static SendMessage getMessageReply(long chatId, String text, int messageId, InlineKeyboardMarkup keyboardMarkup) {
        return new SendMessage(chatId, text)
                .setReplyToMessageId(messageId)
                .setReplyMarkup(keyboardMarkup);
    }

    public static SendPhoto getPhoto(long chatId, String name, String url) throws IOException {
        return new SendPhoto()
                .setChatId(chatId)
                .setPhoto(name, new URL(url).openStream());
    }

    public static SendDocument getDocument(long chatId, String name, String url) throws IOException {
        InputStream stream = new URL(url).openStream();

        if(stream.available() > 0) {

            return new SendDocument()
                    .setChatId(chatId)
                    .setDocument(name, stream);
        }

        return null;
    }

    public static GetFile getFile(String fileId) {
        return new GetFile()
                .setFileId(fileId);
    }
}
