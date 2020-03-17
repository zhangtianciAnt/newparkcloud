package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
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
import java.io.File;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpatriatesinforServiceImpl implements ExpatriatesinforService {

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private PricesetMapper pricesetMapper;

    @Override
    public List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception {
        return expatriatesinforMapper.select(expatriatesinfor);
    }

    @Override
    public Expatriatesinfor getexpatriatesinforApplyOne(String expatriatesinfor_id) throws Exception {
        return expatriatesinforMapper.selectByPrimaryKey(expatriatesinfor_id);
    }

    @Override
    public void updateinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
    }

    @Override
    public void updateexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        String yes = "是";
        String no = "否";
        if (expatriatesinfor.getOperationform().equals("BP024001")) {
            expatriatesinfor.setDistriobjects(yes);
            expatriatesinfor.setVenuetarget(yes);
        } else if (expatriatesinfor.getOperationform().equals("BP024002")) {
            expatriatesinfor.setDistriobjects(no);
            expatriatesinfor.setVenuetarget(yes);
        } else {
            expatriatesinfor.setDistriobjects(no);
            expatriatesinfor.setVenuetarget(no);
        }
        expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
        String thisDate = DateUtil.format(new Date(), "yyyy-MM-dd");
        Priceset priceset = new Priceset();
        priceset.preInsert(tokenModel);
        priceset.setPricesetid(UUID.randomUUID().toString());
        priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
        priceset.setGraduation(expatriatesinfor.getGraduation_year());
        priceset.setCompany(expatriatesinfor.getSuppliername());
        priceset.setAssesstime(thisDate);
        priceset.setStatus("0");
        pricesetMapper.insert(priceset);
    }


    @Override
    public void createexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinfor.preInsert(tokenModel);
        expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
        expatriatesinfor.setExits("1");
        expatriatesinforMapper.insert(expatriatesinfor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> expimport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
//            创建listVo集合方便存储导入信息
            List<Expatriatesinfor> listVo = new ArrayList<Expatriatesinfor>();
//            创建Result结果集的集合
            List<String> Result = new ArrayList<String>();
//            用来接收前台传过来的文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            创建对象f，且为空
            File f = null;
//            创建临时文件
            f = File.createTempFile("temp", null);
//            上传文件
            file.transferTo(f);
//            使用Excel读文件
            ExcelReader reader = ExcelUtil.getReader(f);
//            创建集合存入读的文件
            List<List<Object>> list = reader.read();
//            创建集合存入标准模板
            List<Object> model = new ArrayList<Object>();
//            标准模板
            model.add("姓名");
            model.add("性别");
            model.add("供应商名称");
            model.add("毕业院校");
            model.add("学历");
            model.add("技术分类");
            model.add("Rn");
            model.add("作業形態");
            model.add("作業分類");
            List<Object> key = list.get(0);
//           上传模板与标准模板 校验
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    expatriatesinfor.setExpname(value.get(0).toString());
                    expatriatesinfor.setSex(value.get(1).toString());
                    expatriatesinfor.setSuppliername(value.get(2).toString());
                    expatriatesinfor.setGraduateschool(value.get(3).toString());
                    expatriatesinfor.setEducation(value.get(4).toString());
                    expatriatesinfor.setTechnology(value.get(5).toString());
                    expatriatesinfor.setRn(value.get(6).toString());
                    expatriatesinfor.setOperationform(value.get(7).toString());
                    expatriatesinfor.setJobclassification(value.get(8).toString());
                }
                expatriatesinfor.preInsert();
                expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
                expatriatesinforMapper.insert(expatriatesinfor);
                listVo.add(expatriatesinfor);
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



