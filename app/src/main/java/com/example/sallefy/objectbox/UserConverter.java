package com.example.sallefy.objectbox;

import com.example.sallefy.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

public class UserConverter implements PropertyConverter<User, String> {
    @Override
    public User convertToEntityProperty(String databaseValue) {
        if (databaseValue == null)
            return null;
        try {
            return new Gson().fromJson(databaseValue, new TypeToken<User>(){}.getType());
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseValue(User entityProperty) {
        return entityProperty == null ? null : new Gson().toJson(entityProperty);
    }
}
