package com.escotech.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SaleMethodConverter implements AttributeConverter<SaleMethod, String> {
    @Override
    public String convertToDatabaseColumn(SaleMethod saleMethod) {
        if (saleMethod == null) {
            return null;
        }
        return saleMethod.getCode();
    }

    @Override
    public SaleMethod convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(SaleMethod.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
