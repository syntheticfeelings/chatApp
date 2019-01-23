package com.syntheticfeelings.chatApp.chat.model;

public class Message {
    private String userName;
    private String message;


    public Message(String userName, String message) {
        this.message = message;
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
