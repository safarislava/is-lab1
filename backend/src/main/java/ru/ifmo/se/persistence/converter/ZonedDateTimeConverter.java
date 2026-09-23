package ru.ifmo.se.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, OffsetDateTime> {

    @Override
    public OffsetDateTime convertToDatabaseColumn(ZonedDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toOffsetDateTime();
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(OffsetDateTime dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toZonedDateTime();
    }
}
