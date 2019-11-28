package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.OtherFour;
import com.nt.service_pfans.PFANS2000.OtherFourService;
import com.nt.service_pfans.PFANS2000.mapper.OtherFourMapper;
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
public class OtherFourServiceImpl implements OtherFourService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OtherFourMapper otherFourMapper;


    @Override
    public void deleteotherfour(OtherFour otherFour, TokenModel tokenModel) throws Exception {
        otherFourMapper.delete(otherFour);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUserotherfour(String Givingid,HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<OtherFour> listVo = new ArrayList<OtherFour>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("No.");
            model.add("部門");
            model.add("工号");
            model.add("名字");
            model.add("社保大病险");
            model.add("合計");
            model.add("备注");
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
                OtherFour otherFour = new OtherFour();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    String click="^([1-9][0-9]*)+(.[0-9]{1,2})?$";
                    if(!Pattern.matches(click, value.get(4).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if (value.size() > 4) {
                        if (value.get(4).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String jobnumber = value.get(2).toString();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        otherFour.setUser_id(customerInfo.getUserid());
                        otherFour.setJobnumber(value.get(2).toString());
                    }
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的工号字段没有找到，请输入正确的工号，导入失败");
                        continue;
                    }
                    otherFour.setGiving_id(Givingid);
                    otherFour.setDepartment_id(value.get(1).toString());
                    otherFour.setSocialsecurity(value.get(4).toString());
                    otherFour.setTotal(value.get(5).toString());
                    otherFour.setRemarks(value.get(6).toString());
                }
                int rowundex = accesscount+ 1;
                otherFour.setRowindex(rowundex);
                otherFour.preInsert(tokenModel);
                otherFour.setOtherfour_id(UUID.randomUUID().toString());
                otherFourMapper.insert(otherFour);
                listVo.add(otherFour);
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
