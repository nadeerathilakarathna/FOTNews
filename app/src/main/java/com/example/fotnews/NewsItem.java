package com.example.fotnews;

public class NewsItem {
    String title;
    String content;
    String timestamp; // in millis
    int category;
    String image_url;

    public NewsItem(String title, String content, String timestamp, int category, String image_url) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.category = category;
        this.image_url = image_url;
    }
}
