package com.escotech.enums;

public enum SaleMethod {

    INPERSON("PER", "In-person"), REVERB("REV","Reverb"), WEBAPP("WEB","Web App");

    private String code;
    private String displayName;


    private SaleMethod(String code, String displayName) {
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
