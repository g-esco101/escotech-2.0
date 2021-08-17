package com.escotech.enums;

public enum Condition {
    NEW("NEW", "New"), USED("USED","Used");

    private String code;
    private String displayName;

    private Condition(String code, String displayName) {
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
