package com.example.iotDataGenerator.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MatchMode {
    CONTAINS("contains"),
    NOT_CONTAINS("notContains"),
    EQUALS("equals"),
    NOT_EQUAL("notEquals"),
    STARTS_WITH("startsWith"),
    ENDS_WITH("endsWith"),
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL("lte"),
    GREATER_THAN_OR_EQUAL("gte"),
    DATE_IS("dateIs"),
    DATE_IS_NOT("dateIsNot"),
    DATE_BEFORE("dateBefore"),
    DATE_AFTER("dateAfter");
    public final String value;
    MatchMode(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}
