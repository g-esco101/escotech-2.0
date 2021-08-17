package com.escotech.enums;

public enum Province {

    //Provinces
    ALBERTA("CA-AB","Alberta"),
    BRITISH_COLUMBIA("CA-BC","British Columbia"),
    MANITOBA("CA-MB","Manitoba"),
    NEW_BRUNSWICK("CA-NB","New Brunswick"),
    NEWFOUNDLAND_AND_LABRADOR("CA-NL","Newfoundland and Labrador"),
    NOVA_SCOTIA("CA-NS","Nova Scotia"),
    ONTARIO("CA-ON","Ontario"),
    PRINCE_EDWARD_ISLAND("CA-PE","Prince Edward Island"),
    QUEBEC("CA-QC","Québec"),
    SASKATCHEWAN("CA-SK","Saskatchewan"),
    //Territories
    NORTHWEST_TERRITORIES("CA-NT","Northwest Territories"),
    NUNAVUT("CA-NU","Nunavut"),
    YUKON("CA-YT","Yukon");

    private String displayName;
    private String code;

    private Province(String code, String displayName) {
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
