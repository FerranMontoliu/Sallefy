package com.example.sallefy.objectbox.converters;

import com.example.sallefy.model.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

public class GenreListConverter implements PropertyConverter<List<Genre>, String> {
    @Override
    public List<Genre> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null)
            return new ArrayList<>();
        try {
            return new Gson().fromJson(databaseValue, new TypeToken<List<Genre>>(){}.getType());
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseValue(List<Genre> entityProperty) {
        return entityProperty == null ? null : new Gson().toJson(entityProperty);
    }
}
