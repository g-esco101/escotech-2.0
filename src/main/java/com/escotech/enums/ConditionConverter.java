package com.escotech.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConditionConverter implements AttributeConverter<Condition, String> {
    @Override
    public String convertToDatabaseColumn(Condition condition) {
        if (condition == null) {
            return null;
        }
        return condition.getCode();
    }

    @Override
    public Condition convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Condition.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
