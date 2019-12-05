package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Program;
import com.nt.dao_BASF.BASFUser;
import com.nt.dao_BASF.Record;
import com.nt.service_BASF.RecordServices;
import com.nt.service_BASF.mapper.BASFUserMapper;
import com.nt.service_BASF.mapper.ProgramMapper;
import com.nt.service_BASF.mapper.RecordMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: RecordServicesImpl
 * @Author: Newtouch
 * @Description: 人员培训记录接口实现类
 * @Date: 2019/11/25 13:49
 * @Version: 1.0
 */
@Service
public class RecordServicesImpl implements RecordServices {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ProgramMapper programMapper;
    @Autowired
    private BASFUserMapper basfuserMapper;
    @Override
    public List<Record> list(Record record) throws Exception {
//        Record record = new Record();
        record.setStatus("0");
        Program program = programMapper.selectByPrimaryKey(record.getProgramid());
        int num = recordMapper.selectCount(record);
        List<Record> list1=new ArrayList();
        list1 = recordMapper.select(record);
        int i = 0;
        for(Record record1:list1){
            if(record1.getIfmakeup() == "1"){
                i = i+1;
            }
        }
        Double per =  Double.valueOf(i) /Double.valueOf(num)*100;
        String a = per + "%";
        program.setPassrate(a);

        program.setActualpeo(num);
        programMapper.updateByPrimaryKeySelective(program);
        return recordMapper.select(record);
    }

    @Override
    public void insert(Record record, TokenModel tokenModel) throws Exception {
        record.preInsert(tokenModel);
        record.setRecordid(UUID.randomUUID().toString());
        recordMapper.insert(record);
    }

    @Override
    public void delete(Record record) throws Exception {
        //逻辑删除（status -> "1"）
        recordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Record one(String recordid) throws Exception {
        return recordMapper.selectByPrimaryKey(recordid);
    }

    @Override
    public void update(Record record, TokenModel tokenModel) throws Exception {
        record.preUpdate(tokenModel);
        recordMapper.updateByPrimaryKey(record);
    }

    /**
     * @param request
     * @param tokenModel
     * @Method insert
     * @Author LXY
     * @Version 1.0
     * @Description execl导入
     * @Return void
     * @Date 2019/12/04
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> insert(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Record> listVo = new ArrayList<Record>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("员工号");
            model.add("培训项目名称");
            model.add("考核成绩");
            model.add("是否补考");
            model.add("有无证书");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Record record = new Record();
                List<Object> value = list.get(k);
                k++;
                BASFUser basfUser = new BASFUser();
                basfUser.setPeonum(value.get(0).toString());
                List<BASFUser> basfuserList = basfuserMapper.select(basfUser);
                for (BASFUser basf : basfuserList) {
                    if (basfUser != null) {
                        record.setUsername(basf.getUsername());
                        record.setPersonneltype(basf.getPersonneltype());
                        record.setPeonum(value.get(0).toString());
                    }
                    if (basfUser == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的员工工号字段没有找到，请输入正确的工号，导入失败");
                        continue;
                    }
                }
                Program program = new Program();
                program.setProgramname(value.get(1).toString());
                List<Program> programList = programMapper.select(program);
                for (Program pro : programList) {
                    if (program != null) {
                        record.setRecordtype(pro.getProgramtype());
                        record.setRecordpeo(pro.getProgrampeo());
                        record.setRecordmod(pro.getProgrammod());
                        record.setRecordtime(pro.getProgramtime());
                        record.setRecordclass(pro.getProgramclass());
                        record.setRecordname(value.get(1).toString());
                    }
                    if (program == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的培训项目名称没有找到，请输入正确的培训项目名，导入失败");
                        continue;
                    }
                }
                record.preInsert(tokenModel);
                record.setRecordid(UUID.randomUUID().toString());
                String T = "1";
                String F = "0";
                int a = Integer.parseInt(value.get(2).toString());
                int b = 100;
                int c = 0;
                int hege = 60;
                if (a >= hege || a < b) {
                    record.setResult(T);
                    record.setGrade(value.get(2).toString());
                } else if (a < hege || a > c) {
                    record.setResult(F);
                    record.setGrade(value.get(2).toString());
                } else {
                    error = error + 1;
                    Result.add("模板第" + (k - 1) + "行的考核成绩输入错误，请输入范围0~100的考核成绩，导入失败");
                    continue;
                }
                String shi = value.get(3).toString();
                String Tshi = "是";
                String Fshi = "否";
                if (shi.equals(Tshi)) {
                    record.setIfmakeup(T);
                } else if (shi.equals(Fshi)) {
                    record.setIfmakeup(F);
                } else {
                    error = error + 1;
                    Result.add("模板第" + (k - 1) + "行的是否是补考输入错误，请输入是或否，导入失败");
                    continue;
                }
                String you = value.get(4).toString();
                String Tyou = "有";
                String Fyou = "无";
                if (you.equals(Tyou)) {
                    record.setCertificate(T);
                } else if (you.equals(Fyou)) {
                    record.setCertificate(F);
                } else {
                    error = error + 1;
                    Result.add("模板第" + (k - 1) + "行的是否头证书填写错误，请输入有或无，导入失败");
                    continue;
                }
                recordMapper.insert(record);
                listVo.add(record);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
