package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Duty;
import com.nt.service_BASF.DutyServices;
import com.nt.service_BASF.mapper.DutyMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.util.*;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: DutyServicesImpl
 * @Author: Newtouch
 * @Description: 值班信息表接口实现类
 * @Date: 2019/12/25 10:59
 * @Version: 1.0
 */
@Service
public class DutyServicesImpl implements DutyServices {

    @Autowired
    private DutyMapper dutyMapper;

    //excel文档导入
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importexcel(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        //存放消息
        List<String> result = new ArrayList<>();
        //存放值班信息list
//        List<Duty> dutyList = new ArrayList<Duty>();
        //存放值班信息list
        HashMap<Duty, String> dutyList = new HashMap<Duty, String>();
        //临时文件
        File f = null;
        //插入成功数
        int successCount = 0;
        //插入失败数
        int errorCount = 0;
        //excel集合
        List<List<Object>> list;
        //当前执行行数
        int k = 3;
        try {
            //转换成文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
        } catch (Exception e) {
            result.add("系统在读取并存储为临时文件时发生未知异常！");
            return result;
//            throw new LogicalException(e.getMessage());
        }
        try {
            //读取excel临时文件
            ExcelReader reader = ExcelUtil.getReader(f);
            //读取到的excel集合
            list = reader.read();
        } catch (Exception e) {
            result.add("系统在读取Excel临时文件时发生未知异常！");
            return result;
//            throw new LogicalException(e.getMessage());
        }
        try {
            //判断是否是空文件
            if (list == null) {
                result.add("空文件，请上传正确的值班文件");
                return result;
            }
            //判断是否是正确格式的值班文件(方案一：检查行数是否足够)
            if (list.size() < 3) {
                result.add("值班文件格式不正确！");
                return result;
            }
            //校验值班人员信息是否正确
            List<Object> dutyname = list.get(1);
            if (!judgename(dutyname)) {
                result.add("值班人员姓名信息不完整！");
                return result;
            }
            //循环获取值班信息
            for (int i = 3; i < list.size(); i++) {
                k += 1;
                List<Object> olist = list.get(i);
                //判断是否是空的值班信息
                //是空值班
                if (judgenull(olist))
                    continue;
                    //不是空值班
                else {
                    Duty duty = combination(olist, dutyname, tokenModel);
                    if (duty != null) {
                        dutyList.put(duty, String.valueOf(k));
                    } else {
                        errorCount += 1;
                        result.add("值班表" + k + "行数据异常，导入系统失败！");
                    }
                }
            }
            //循环添加数据
            for (Map.Entry<Duty, String> duty : dutyList.entrySet()) {
                try {
                    Duty d = new Duty();
                    d.setDutytime(duty.getKey().getDutytime());
                    List<Duty> list1 = dutyMapper.select(d);
                    if (list1 != null && list1.size() > 0) {
                        dutyMapper.delete(d);
                        dutyMapper.insert(duty.getKey());
                        successCount += 1;
                    } else {
                        dutyMapper.insert(duty.getKey());
                        successCount += 1;
                    }
                } catch (Exception e) {
                    errorCount += 1;
                    result.add("值班表" + duty.getValue() + "行导入系统失败！");
//            throw new LogicalException(e.getMessage());
                }

            }
            result.add("失败数：" + errorCount);
            result.add("成功数：" + successCount);
            return result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    //校验是否是合理的值班人员信息
    public boolean judgename(List<Object> dutyname) throws Exception {
        if (dutyname.size() < 8) {
            return false;
        }
        if (dutyname.get(2) == null || dutyname.get(3) == null || dutyname.get(4) == null || dutyname.get(5) == null || dutyname.get(6) == null || dutyname.get(7) == null) {
            return false;
        } else if (String.valueOf(dutyname.get(2)).equals("") || String.valueOf(dutyname.get(3)).equals("") || String.valueOf(dutyname.get(4)).equals("") || String.valueOf(dutyname.get(5)).equals("") || String.valueOf(dutyname.get(6)).equals("") || String.valueOf(dutyname.get(7)).equals("")) {
            return false;
        } else
            return true;
    }

    //判断是否是空值班信息,连带校验
    public boolean judgenull(List<Object> objectList) throws Exception {
        //判断是否符合要求
        if (objectList.size() < 8)
            return true;
        //判断值班时间是否存在
        if (objectList.get(1) == null || String.valueOf(objectList.get(1)).equals(""))
            return true;
        //判断是否是空值班信息
        if (objectList.get(2) != null || objectList.get(3) != null || objectList.get(4) != null
                || objectList.get(5) != null || objectList.get(6) != null || objectList.get(7) != null) {
            return String.valueOf(objectList.get(2)).equals("")
                    && String.valueOf(objectList.get(3)).equals("") && String.valueOf(objectList.get(4)).equals("")
                    && String.valueOf(objectList.get(5)).equals("") && String.valueOf(objectList.get(6)).equals("")
                    && String.valueOf(objectList.get(7)).equals("");
        } else
            return true;
    }

    //拼接一条数据
    public Duty combination(List<Object> olist, List<Object> dutyname, TokenModel tokenModel) throws Exception {
        try {
            Duty duty = new Duty();
            duty.setDutyid(UUID.randomUUID().toString());
            duty.preInsert(tokenModel);
            //设置值班时间
            java.util.Date dutydate = (java.util.Date) olist.get(1);
            long lon = dutydate.getTime();
            duty.setDutytime(new java.sql.Date(lon));
            //循环判断添加数据
            for (int i = 2; i < 8; i++) {
                switch (String.valueOf(olist.get(i))) {
                    case "D":
                        duty.setDutydaytime(String.valueOf(dutyname.get(i)));
                        break;
                    case "N":
                        duty.setDutynight(String.valueOf(dutyname.get(i)));
                        break;
                    case "d":
                        duty.setBackupdaytime(String.valueOf(dutyname.get(i)));
                        break;
                    case "n":
                        duty.setBackupnight(String.valueOf(dutyname.get(i)));
                        break;
                    case "null":
                        break;
                    case "":
                        break;
                    default:
                        if (i <= 4)
                            duty.setDutyother(dutyname.get(i) + "-" + olist.get(i));
                        else
                            duty.setBackupother(dutyname.get(i) + "-" + olist.get(i));
                }
            }
            return duty;
        } catch (Exception e) {
            return null;
        }

    }

    //当天值班人员查询
    @Override
    public Duty selectDayDuty() throws Exception {
        Duty duty = new Duty();
        duty.setDutytime(new java.sql.Date(System.currentTimeMillis()));
        List<Duty> dutyList = dutyMapper.select(duty);
        if (dutyList != null && dutyList.size() > 0) {
            return dutyList.get(0);
        } else
            return new Duty();
    }
}
