package com.nt.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @类名称：MongoObject
 * @描述：MongoDB数据库操作共通类
 * @创建日期：2018/10/22
 * @作者：赵国栋
 */
public class MongoObject {
    /**
     * @方法名：CustmizeUpdate
     * @描述：更新数据
     * @创建日期：2018/10/22
     * @作者：赵国栋
     * @参数：[obj：要更新的对象, UpWithoutNull：true空值不更新，false空值更新 ]
     * @返回值：org.springframework.data.mongodb.core.query.Update
     */
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

    /**
     * @方法名：CustmizeQuery
     * @描述：查询数据（自动判断是否带权限查询）
     * @创建日期：2018/10/22
     * @作者：赵国栋
     * @参数：[obj：要查询的对象]
     * @返回值：org.springframework.data.mongodb.core.query.Query
     */
    public final static <T> Query CustmizeQuery(T obj) throws IllegalAccessException {
        Query query = new Query();

        Field[] field = getAllField(obj);


        for (int j = 2; j < field.length; j++) {
            field[j].setAccessible(true);
            String name = field[j].getName();

            //判断拥有Teantid时，将TenantId设置为检索条件
            if (name.equals(AuthConstants.TENANTID) && !StringUtils.isEmpty(field[j].get(obj).toString())) {
                query.addCriteria(Criteria.where(AuthConstants.TENANTID).is(field[j].get(obj)));
            }

            //判断拥有owner时，将owner设置为检索条件
            if (name.equals(AuthConstants.OWNERS) && field[j].get(obj) != null) {
                if (((List) field[j].get(obj)).size() > 0) {
                    query.addCriteria(Criteria.where(AuthConstants.OWNER).in(field[j].get(obj)));
                }
            }
        }
        return query;
    }

    /**
     * @方法名：getAllField
     * @描述：取得对象的所有字段
     * @创建日期：2018/10/22
     * @作者：赵国栋
     * @参数：[obj]
     * @返回值：java.lang.reflect.Field[]
     */
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
