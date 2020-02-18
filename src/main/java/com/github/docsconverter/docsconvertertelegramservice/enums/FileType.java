package com.github.docsconverter.docsconvertertelegramservice.enums;

public enum FileType {
    TEXT,
    BOOK,
    PHOTO,
    DOCUMENT,
    AUDIO,
    UNDEFINED;

    public static FileType getFileType(String extension){
        switch (extension){
            case "pdf":
            case "txt":
                return DOCUMENT;
            case "jpg":
            case "png":
            case "bmp":
            case "gif":
                return PHOTO;
            default:
                return UNDEFINED;
        }
    }
}
