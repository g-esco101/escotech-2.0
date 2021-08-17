package com.escotech.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;


@Converter(autoApply = true)
public class ShippingMethodConverter implements AttributeConverter<ShippingMethod, String> {
    @Override
    public String convertToDatabaseColumn(ShippingMethod shippingMethod) {
        if (shippingMethod == null) {
            return null;
        }
        return shippingMethod.getCode();
    }

    @Override
    public ShippingMethod convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(ShippingMethod.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
