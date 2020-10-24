package com.nt.utils.Encryption;

import cn.hutool.core.codec.Base64;
import com.nt.utils.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@ConditionalOnProperty(value = "encryption", havingValue = "true")
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})

})
@Component
public class ResultInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        // 获取该sql语句的类型，例如update，insert
        String methodName = invocation.getMethod().getName();
        // 获取该sql语句放入的参数
        Object parameter = invocation.getArgs()[1];


        // 如果有就扫描是否是更新操作和是否有加密注解
        // 如果是更新或者插入时，就对数据进行加密后保存在数据库
        if (StringUtils.equalsIgnoreCase("update", methodName) ||
                StringUtils.equalsIgnoreCase("insert", methodName)) {
            // 对参数内含注解的字段进行加密
            encryptField(parameter);
        }


        // 继续执行sql语句,调用当前拦截的执行方法
        Object returnValue = invocation.proceed();
        try {
            // 当返回值类型为数组集合时，就判断是否需要进行数据解密
            if (returnValue instanceof ArrayList<?>) {
                List<?> list = (List<?>) returnValue;
                // 判断结果集的数据是否为空
                if (list == null || list.size() < 1) {
                    return returnValue;
                }
                Object object = list.get(0);
                // 为空值就返回数据
                if (object == null) {
                    return returnValue;
                }
                // 判断第一个对象是否有解密注解
                Field[] fields = object.getClass().getDeclaredFields();
                // 定义一个临时变量
                int len;
                if (fields != null && 0 < (len = fields.length)) {
                    for (Object o : list) {
                        //调用解密,在这个方法中针对某个带有解密注解的字段进行解密
                        decryptField(o);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnValue;
        }
        return returnValue;

    }

    /**
     * <p>声明这是一个泛型方法,让所有参数都能够调用这个方法扫描带有解密注解的字段，
     * 进行解密，然后显示在前端页面中</p>
     *
     * @param <T>
     */
    public <T> void decryptField(T t) {
        // 获取对象的域
        Field[] declaredFields = t.getClass().getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            // 遍历这些字段
            for (Field field : declaredFields) {
                // 如果这个字段存在解密注解就进行解密
                if (field.isAnnotationPresent(Encryption.class) && field.getType().toString().endsWith("String")) {
                    field.setAccessible(true);
                    try {
                        // 获取这个字段的值
                        String fieldValue = (String) field.get(t);
                        // 判断这个字段的数值是否不为空
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            // 首先调用一个方法判断这个数据是否是未经过加密的，如果可以解密就进行解密，解密失败就返回元数据
                            boolean canDecrypt;
                            canDecrypt = StringUtils.isBase64Encode(fieldValue);
                            if (canDecrypt) {
                                // 进行解密
                                String encryptData = Base64.decodeStr(fieldValue);

                                if(encryptData.indexOf("�") < 0){
                                    // 将值反射到对象中
                                    field.set(t, encryptData);
                                }
                                else{
                                    // 将值反射到对象中
                                    field.set(t, fieldValue);
                                }
                            } else {
                                // 不能解密的情况下，就不对这个对象做任何操作，即默认显示元数据
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * <p>声明这是一个泛型方法,让所有参数都能够调用这个方法扫描带有加密注解的字段，
     * 进行加密，然后存在数据库中</p>
     *
     * @param <T>
     */
    public <T> void encryptField(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            for (Field field : declaredFields) {
                // 查找当字段带有加密注解,并且字段类型为字符串类型
                if (field.isAnnotationPresent(Encryption.class) && field.getType().toString().endsWith("String")) {
                    field.setAccessible(true);
                    String fieldValue = null;
                    try {
                        // 获取这个值
                        fieldValue = (String) field.get(t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    // 判断这个值是否为空
                    if (StringUtils.isNotEmpty(fieldValue)) {
                        try {
                            if(!StringUtils.isBase64Encode(fieldValue)){
                                // 不为空时，就进行加密
                                field.set(t, Base64.encode(fieldValue));
                            }else{
                                // 进行解密特殊字符串不识别base64的处理
                                String encryptData = Base64.decodeStr(fieldValue);
                                if(encryptData.indexOf("�") < 0){
                                    // 不为空时，就进行加密
                                    field.set(t, fieldValue);
                                }
                                else{
                                    // 不为空时，就进行加密
                                    field.set(t, Base64.encode(fieldValue));
                                }
                            }

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
