package com.dostal.loveapp;

import java.security.Timestamp;

public class Messages {
    private String message;
    private int counter=0;

    public Messages() {
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Messages(String message, int counter) {
        this.message = message;
        this.counter = counter;
    }

    public Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
