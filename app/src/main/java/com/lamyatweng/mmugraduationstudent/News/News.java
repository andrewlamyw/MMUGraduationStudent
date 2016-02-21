package com.lamyatweng.mmugraduationstudent.News;

public class News {
    String date;
    String message;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public News() {
    }

    public News(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
