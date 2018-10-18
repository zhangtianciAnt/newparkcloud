package com.nt.utils;

import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;

public class MongoObject {

    public final static <T> Update CustmizeUpdate(T obj, Boolean UpWithoutNull) throws IllegalAccessException {
        Update update = new Update();

        Field[] field = obj.getClass().getDeclaredFields();

        for (int j = 2; j < field.length; j++) {
            field[j].setAccessible(true);
            String name = field[j].getName();
            Object value = field[j].get(obj);
            if (!UpWithoutNull || (UpWithoutNull && value != null)) {
                update.set(name, value);
            }
        }
        return update;
    }
}
