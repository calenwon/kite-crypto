package com.kite.enums;

/**
 * Console text colors
 *
 * @author Calendar
 */
public enum ColorEnum {
    DEFAULT("\33[0m"),

    RED("\33[1m\33[31m"),

    GREEN("\33[1m\33[32m"),

    YELLOW("\33[1m\33[33m"),

    BLUE("\33[1m\33[34m"),

    PINK("\33[1m\33[35m"),

    CYAN("\33[1m\33[36m");

    private final String value;

    ColorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
