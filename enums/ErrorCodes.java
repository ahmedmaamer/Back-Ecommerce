package com.example.iotDataGenerator.enums;

public enum ErrorCodes {

    CASCADE_DELETE("cascade-delete"),
    CUSTOM_SCHEDULER_OPERATION("custom-scheduler-operation"),
    DATA_VALUE("data-value"),
    NUMBER_FORMAT("number-format"),
    OUTBOUND_RESPONSE("outbound-response"),
    RESOURCE_ALREADY_EXISTS("resource-already-exists"),
    RESOURCE_NOT_FOUND("resource-not-found"),
    RESOURCE_NOT_SAVED("resource-not-saved"),
    DEFAULT("default"),
    EMPTY_OBJECT("empty-object"),
    ARGUMENT_NOT_VALID("argument-not-valid");

    private final String errorCode;
    ErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return errorCode;
    }
}
