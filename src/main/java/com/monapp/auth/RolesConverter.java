package com.monapp.auth;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Converter
public class RolesConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> roles) {
        if (roles == null || roles.isEmpty()) return "";
        return String.join(",", roles);
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return new HashSet<>();
        return new HashSet<>(Arrays.asList(dbData.split(",")));
    }
}
