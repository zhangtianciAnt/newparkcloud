package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
import com.nt.service_pfans.PFANS2000.mapper.OtherTwoMapper;
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
public class OtherTwoServiceImpl implements OtherTwoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Override
    public List<OtherTwo> list(OtherTwo othertwo) throws Exception {
        return othertwoMapper.select(othertwo);
    }

    @Override
    public void insert(OtherTwo othertwo, TokenModel tokenModel) throws Exception {
        othertwo.preInsert(tokenModel);
        othertwo.setOthertwo_id(UUID.randomUUID().toString());
        int rowundex = 0;
        rowundex = rowundex + 1;
        othertwo.setRowindex(rowundex);
        othertwoMapper.insert(othertwo);
    }

    @Override
    public void deletete(OtherTwo othertwo, TokenModel tokenModel) throws Exception {
        othertwoMapper.delete(othertwo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<OtherTwo> listVo = new ArrayList<OtherTwo>();
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
            for (int i = 1; i < list.size() - 1; i++) {
                OtherTwo othertwo = new OtherTwo();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    String click="^([1-9][0-9]*)+(.[0-9]{1,2})?$";
                    if(!Pattern.matches(click, value.get(2).toString())){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if (value.size() > 2) {
                        if (value.get(2).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 3) {
                        if (value.get(3).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的根拠长度超出范围，请输入长度为20位之内的根拠，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String customername = value.get(1).toString();
                    query.addCriteria(Criteria.where("userinfo.customername").is(customername));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    othertwo.setUser_id(customerInfo.getUserid());
                    othertwo.setMoneys(value.get(2).toString());
                    othertwo.setRootknot(value.get(3).toString());
                }
                int rowundex = i;
                othertwo.setRowindex(rowundex);
                othertwo.setType("1");
                othertwo.preInsert(tokenModel);
                othertwo.setOthertwo_id(UUID.randomUUID().toString());
                othertwoMapper.insert(othertwo);
                listVo.add(othertwo);
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
