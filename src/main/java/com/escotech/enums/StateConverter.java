package com.escotech.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StateConverter implements AttributeConverter<State, String> {
    @Override
    public String convertToDatabaseColumn(State state) {
        if (state == null) {
            return null;
        }
        return state.getCode();
    }

    @Override
    public State convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(State.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
