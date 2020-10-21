package com.nt.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @date 2020/10/20 15:58
 * fjl
 */
public class BaseUtil {

    private BaseUtil(){};

    /**
     * 构造登录参数
     * @param dbid 账套id
     * @param userName 用户名
     * @param password 密码
     * @param lang 语言
     * @return
     */
    public static String buildLogin(String dbid, String userName, String password, int lang){
        Map<String,Object> param = new HashMap<>(4);
        param.put("acctID",dbid);
        param.put("username",userName);
        param.put("password",password);
        param.put("lcid",lang);
        return JSON.toJSONString(param);
    }


    /**
     * 初始化物料信息
     * @param template 物料基础数据模板
     * @param formid 表单id
//     * @param code 物料编码
//     * @param name 物料名称
//     * @param attr 物料规格属性
     * @return
     */
    public static String buildMaterial(JSONObject template,String formid){
//        JSONObject basic = JSON.parseObject(template);
//        Map<String,Object> model = (Map<String, Object>) basic.get("Model");
//        model.put("FNumber",code);
//        model.put("FName",name);
//        model.put("FSpecification",attr);
//        basic.put("Model",model);

//        JSONObject jsonObject = new JSONObject();
        Map<String,Object> param = new HashMap<>(2);
        param.put("formid",formid);
        param.put("data",JSON.toJSONString(template));
        return JSON.toJSONString(param);
    }

    /**
     * 构造提交、审核参数
     * @param formid 表单id
     * @param numbers 编码 多个编码以,分隔
     * @param flags 审核标示 多个以,分隔 和编码一一对应
     * @return
     */
    public static String buildParam(String formid,String numbers,String flags){
        JSONObject jsonObject = new JSONObject();
        JSONObject param = new JSONObject();
        if(flags!=null){
            String[] arr_flag = flags.split(",");
            param.put("InterationFlags",arr_flag);
        }
        String[] arr_number = numbers.split(",");
        param.put("Numbers",arr_number);
        jsonObject.put("formid",formid);
        jsonObject.put("data",JSON.toJSONString(param));
        return JSON.toJSONString(jsonObject);
    }

    public static String jsonRead(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }
}
