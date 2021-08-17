package com.escotech.enums;

public enum ShippingMethod {
    PICKPUP("PICKUP", "Store pick-up"), TWOTOSEVEN("TWOSEV","Standard 2-7 Days");

    private String code;
    private String displayName;

    private ShippingMethod(String code, String displayName) {
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
