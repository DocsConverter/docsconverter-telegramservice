package com.github.docsconverter.docsconvertertelegramservice;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.Max;
import java.util.Collections;
import java.util.List;

import static com.github.docsconverter.docsconvertertelegramservice.util.BotUtil.createInlineKeyboardButton;
import static com.github.docsconverter.docsconvertertelegramservice.util.BotUtil.setInline;

public class Keyboards {

    public static InlineKeyboardMarkup getPhotoKeyboard(){
        List<InlineKeyboardButton> buttons = List.of(
                createInlineKeyboardButton("To pdf", "to_pdf"),
                createInlineKeyboardButton("Cancel", "cancel"));

        return setInline(Collections.singletonList(buttons));
    }

    public static InlineKeyboardMarkup getTextKeyboard(){
        List<InlineKeyboardButton> buttons = List.of(
                createInlineKeyboardButton("To txt", "to_txt"),
                createInlineKeyboardButton("Cancel", "cancel"));

        return setInline(Collections.singletonList(buttons));
    }
}
