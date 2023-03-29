package com.example.androidchatgpt;

public class Message {
    public static String SENT_HUMAN = "human";
    public static String SENT_GPT = "gpt";

    String message;
    String sentBy;

    public Message(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public String getSentBy() {
        return sentBy;
    }

}
