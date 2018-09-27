package com.nt.newparkcloud.utils.impl;

import com.nt.newparkcloud.utils.LogicalException;
import com.nt.newparkcloud.utils.services.MongoBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class MongoBaseServiceImpl<T> implements MongoBaseService<T> {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static Logger log = LoggerFactory.getLogger(MongoBaseServiceImpl.class);

    @Override
    public List<T> selectAllWithAuth(T record, HttpServletRequest request) throws LogicalException {

        if (record == null) {
            throw new LogicalException("参数对象不能为空");
        }
        Query query = new Query();
        //取得所有字段
        Field[] fields = record.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if ("serialVersionUID".equals(fields[i].getName())) {
                continue;
            }
            Object value = getFieldValueByName(fields[i].getName(), record);
            if (value != null) {
                Criteria criteria = Criteria.where(fields[i].getName()).is(value);
                query.addCriteria(criteria);
            }
        }
        return (List<T>) mongoTemplate.find(query, record.getClass());
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
