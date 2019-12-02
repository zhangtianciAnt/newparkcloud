package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Additional;
import com.nt.service_pfans.PFANS2000.AdditionalService;
import com.nt.service_pfans.PFANS2000.mapper.AdditionalMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor = Exception.class)
public class AdditionalServiceImpl implements AdditionalService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AdditionalMapper additionalMapper;


    @Override
    public void deleteadditional(Additional additional, TokenModel tokenModel) throws Exception {
        additionalMapper.delete(additional);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUseradditional(String Givingid,HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Additional> listVo = new ArrayList<Additional>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("No.");
            model.add("工号");
            model.add("名字");
            model.add("累计子女教育");
            model.add("累计住房贷款利息");
            model.add("累计住房租金");
            model.add("累计赡养老人");
            model.add("累计继续教育");
            model.add("合計");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size() - 1; i++) {
                Additional additional = new Additional();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    String click="^([1-9][0-9]*)+(.[0-9]{1,2})?$";
                    if(!Pattern.matches(click, value.get(3).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的累计子女教育金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if(!Pattern.matches(click, value.get(4).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的累计住房贷款利息金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if(!Pattern.matches(click, value.get(5).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的累计住房租金金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if(!Pattern.matches(click, value.get(6).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的累计赡养老人金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if(!Pattern.matches(click, value.get(7).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的累计继续教育金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if (value.size() > 3) {
                        if (value.get(3).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 4) {
                        if (value.get(4).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 5) {
                        if (value.get(5).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 6) {
                        if (value.get(6).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 7) {
                        if (value.get(7).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String jobnumber = value.get(1).toString();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        additional.setUser_id(customerInfo.getUserid());
                        additional.setJobnumber(value.get(1).toString());
                    }
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的工号字段没有找到，请输入正确的工号，导入失败");
                        continue;
                    }
                    additional.setChildreneducation(value.get(3).toString());
                    additional.setHousing(value.get(4).toString());
                    additional.setRent(value.get(5).toString());
                    additional.setSupport(value.get(6).toString());
                    additional.setEducation(value.get(7).toString());
                    additional.setTotal(value.get(8).toString());
                    additional.setGiving_id(Givingid);
                }
                int rowundex = accesscount+ 1;
                additional.setRowindex(rowundex);
                additional.preInsert(tokenModel);
                additional.setAdditional_id(UUID.randomUUID().toString());
                additionalMapper.insert(additional);
                listVo.add(additional);
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
