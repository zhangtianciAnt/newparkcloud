package com.nt.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoObject {

    public final static <T> Update CustmizeUpdate(T obj, Boolean UpWithoutNull) throws IllegalAccessException {
        Update update = new Update();

        Field[] field = getAllField(obj);

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

    public final static <T> Query CustmizeQuery(T obj) throws IllegalAccessException {
        Query query = new Query();

        Field[] field = getAllField(obj);


        for (int j = 2; j < field.length; j++) {
            field[j].setAccessible(true);
            String name = field[j].getName();

            //判断拥有Teantid时，将TenantId设置为检索条件
            if (name.equals(AuthConstants.TENANTID) && field[j].get(obj) != null) {
                query.addCriteria(Criteria.where(AuthConstants.TENANTID).is(field[j].get(obj)));
            }

            //判断拥有owner时，将owner设置为检索条件
            if (name.equals(AuthConstants.OWNERS) && field[j].get(obj) != null) {
                query.addCriteria(Criteria.where(AuthConstants.OWNER).in(field[j].get(obj)));
            }
        }
        return query;
    }

    private final static <T> Field[] getAllField(T obj) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = obj.getClass();
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
