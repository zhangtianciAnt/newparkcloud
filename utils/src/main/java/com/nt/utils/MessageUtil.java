package com.nt.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.newparkcloud.utils.impl
 * @ClassName: MessageUtilServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2018/9/26
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/9/26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MessageUtil {

    /**
     * @方法名：getMessage
     * @描述：通过msgId获取消息内容
     * @创建日期：2018/9/26
     * @作者：SKAIXX
     * @参数：[msgId]
     * @返回值：java.lang.String
     */
    public static String getMessage(String msgId) {
        return getValueByKey(msgId);
    }

    /**
     * @方法名：getValueByKey
     * @描述：读取properties文件
     * @创建日期：2018/9/26
     * @作者：SKAIXX
     * @参数：[msgId]
     * @返回值：java.lang.String
     */
    private static String getValueByKey(String msgId) {
        try {
            //读取文件流
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("message.properties");
            //转变为字符流
            InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, "utf-8");
            //创建 Properties 对象
            Properties properties = new Properties();
            //加载字符流
            properties.load(inputStreamReader);
            //获取所有key
            Enumeration enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                //遍历key
                String key = (String) enumeration.nextElement();
                if (key.equals(msgId)) {
                    //根据key取值
                    return properties.getProperty(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
