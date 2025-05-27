package com.example.fotnews;

public class NewsItem {
    String title;
    String content;
    long timestamp; // in millis
    int category;
    int imageResId;

    public NewsItem(String title, String content, long timestamp, int category,  int imageResId) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.category = category;
        this.imageResId = imageResId;
    }
}
