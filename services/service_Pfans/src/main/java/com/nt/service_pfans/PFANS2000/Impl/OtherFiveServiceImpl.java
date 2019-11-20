package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.OtherFive;
import com.nt.service_pfans.PFANS2000.OtherFiveService;
import com.nt.service_pfans.PFANS2000.mapper.OtherFiveMapper;
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

@Service
@Transactional(rollbackFor = Exception.class)
public class OtherFiveServiceImpl implements OtherFiveService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OtherFiveMapper otherfiveMapper;

    @Override
    public List<OtherFive> list(OtherFive otherfive) throws Exception {
        return otherfiveMapper.select(otherfive);
    }

    @Override
    public void deletete(OtherFive otherfive, TokenModel tokenModel) throws Exception{
        otherfiveMapper.delete(otherfive);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<OtherFive> listVo = new ArrayList<OtherFive>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("No.");
            model.add("名字");
            model.add("金額");
            model.add("根拠");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size()-1; i++) {
                OtherFive otherfive = new OtherFive();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    if(value.size() > 2) {
                        if (value.get(2).toString().length()>20) {
                            error = error + 1;
                            Result.add(" 第" + (k-1) + "行金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if(value.size() > 3) {
                        if (value.get(3).toString().length()>20) {
                            error = error + 1;
                            Result.add(" 第" + (k-1) + "行根拠长度超出范围，请输入长度为20位之内的根拠，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String customername = value.get(1).toString();
                    query.addCriteria(Criteria.where("userinfo.customername").is(customername));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    otherfive.setUser_id(customerInfo.getUserid());
                }
                int rowundex = i;
                otherfive.setRowindex(rowundex);
                otherfive.preInsert(tokenModel);
                otherfiveMapper.insert(otherfive);
                listVo.add(otherfive);
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
