package com.example.sallefy.objectbox.converters;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import io.objectbox.converter.PropertyConverter;

public class StringListConverter implements PropertyConverter<List<String>, String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null)
            return new ArrayList<>();
        try {
            JSONArray array = new JSONArray(databaseValue);
            ArrayList<String> ret = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                ret.add(array.getString(i));
            }
            return ret;
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        try {
            if (entityProperty == null)
                return null;
            return new JSONArray(entityProperty).toString();
        }
        catch (Exception e) {
            return null;
        }
    }
}