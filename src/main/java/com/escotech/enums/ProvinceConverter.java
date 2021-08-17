package com.escotech.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ProvinceConverter implements AttributeConverter<Province, String> {

    @Override
    public String convertToDatabaseColumn(Province province) {
        if (province == null) {
            return null;
        }
        return province.getCode();
    }

    @Override
    public Province convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Province.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
