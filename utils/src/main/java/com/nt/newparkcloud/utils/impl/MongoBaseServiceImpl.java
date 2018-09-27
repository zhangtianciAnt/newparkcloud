package com.nt.newparkcloud.utils.impl;

import com.nt.newparkcloud.utils.LogicalException;
import com.nt.newparkcloud.utils.services.MongoBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MongoBaseServiceImpl implements MongoBaseService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static Logger log = LoggerFactory.getLogger(MongoBaseServiceImpl.class);

    @Override
    public <T>T selectAllWithAuth(T record, HttpServletRequest request) throws LogicalException {

        if (record == null) {
            throw new LogicalException("");
        }

        Field[] fields = record.getClass().getDeclaredFields();
        // TODO : selectAllWithAuth
        return null;
    }

    /**
     * @方法名：getFieldValueByName
     * @描述：获取bean中各属性的值
     * @创建日期：2018/9/26
     * @作者：SKAIXX
     * @参数：[fieldName, o]
     * @返回值：java.lang.Object
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }
}
