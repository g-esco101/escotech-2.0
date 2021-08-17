package com.escotech.enums;

public enum Category {
    PHONE("PAY", "Phone"), BOOTH("BOOTH","Booth"), BRITISH_BOOTH("BRIT","British Booth"), PART("PART","Parts"), OTHER("OTHER","Everything Else");

    private String code;
    private String displayName;


    private Category(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }
    public String getDisplayName() {
        return displayName;
    }
}
