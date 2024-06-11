package com.example.iotDataGenerator.enums;

public enum Topic {
    DATAGENERATION("Data_Generation") ,
    LIMITTEST("Limit_Test");

    private final String topic;

    Topic(String topic) {
        this.topic = topic;

    }

    public String getTopic() {
        return topic;
    }
}
