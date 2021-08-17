package com.escotech.enums;

public enum Country {
    // These abbreviations adhere to the PayPal country codes.
    US("USA"), CN("Canada");

    private String displayName;

    private Country(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
