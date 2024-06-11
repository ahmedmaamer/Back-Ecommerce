package com.example.iotDataGenerator.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
    AND("and"), OR("or");
    private final String value;

    Operator(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
