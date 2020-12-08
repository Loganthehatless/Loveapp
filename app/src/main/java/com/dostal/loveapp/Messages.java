package com.dostal.loveapp;

import java.security.Timestamp;

public class Messages {
    private String message;
    private int counter=0;
    private String sentuserId;

    public Messages() {
    }

    public String getSentuserId() {
        return sentuserId;
    }

    public void setSentuserId(String sentuserId) {
        this.sentuserId = sentuserId;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Messages(String message, int counter, String sentuserId) {
        this.message = message;
        this.counter = counter;
        this.sentuserId = sentuserId;
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
