package com.github.docsconverter.docsconvertertelegramservice.to;

import com.github.docsconverter.docsconvertertelegramservice.enums.Command;
import com.github.docsconverter.docsconvertertelegramservice.enums.FileType;

public class Task {
    private Long id;
    private Long chatId;
    private FileType type;
    private Command command;
    private String text;
    private String url;
    private boolean completed;

    public Task(Long id, Long chatId, FileType type, Command command, String text, String url, boolean completed) {
        this.id = id;
        this.chatId = chatId;
        this.type = type;
        this.command = command;
        this.text = text;
        this.url = url;
        this.completed = completed;
    }

    public Task(Long chatId, FileType type, Command command, String text, String url) {
        this(null, chatId, type, command, text, url, false);
    }

    public Task(Long chatId, FileType type, Command command, String text) {
        this(null, chatId, type, command, text, null, false);
    }

    public Task(){
    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public FileType getType() {
        return type;
    }

    public Command getAction() {
        return command;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public void setAction(Command command) {
        this.command = command;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
