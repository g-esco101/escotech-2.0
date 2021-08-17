package com.escotech.enums;

public enum  UserRole {
    ADMIN("ADMIN"), EMPLOYEE("EMP"), CUSTOMER("CUST"), GUEST("GUEST");

    private String code;

    private UserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
