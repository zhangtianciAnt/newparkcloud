package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Appreciation;
import com.nt.service_pfans.PFANS2000.AppreciationService;
import com.nt.service_pfans.PFANS2000.mapper.AppreciationMapper;
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
public class AppreciationServiceImpl implements AppreciationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AppreciationMapper appreciationMapper;

    @Override
    public List<Appreciation> list(Appreciation appreciation) throws Exception {
        return appreciationMapper.select(appreciation);
    }

    @Override
    public void insert(Appreciation appreciation, TokenModel tokenModel) throws Exception {
        appreciation.preInsert(tokenModel);
        appreciation.setAppreciation_id(UUID.randomUUID().toString());
        int rowundex = 0;
        rowundex = rowundex + 1;
        appreciation.setRowindex(rowundex);
        appreciationMapper.insert(appreciation);
    }

    @Override
    public void deletete(Appreciation appreciation, TokenModel tokenModel) throws Exception {
        appreciationMapper.delete(appreciation);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Appreciation> listVo = new ArrayList<Appreciation>();
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
            model.add("氏名");
            model.add("評価");
            model.add("金額");
            model.add("備考");
            model.add("扩展1");
            model.add("扩展2");
            model.add("扩展3");
            model.add("扩展4");
            model.add("扩展5");
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
                Appreciation appreciation = new Appreciation();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    String click = "^([1-9][0-9]*)+(.[0-9]{1,2})?$";
                    if (!Pattern.matches(click, value.get(4).toString())) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的金额不符合规范，请输入正确的金额，导入失败");
                        continue;
                    }
                    if (value.size() > 3) {
                        if (value.get(3).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String jobnumber = value.get(1).toString();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    appreciation.setJobnumber(value.get(1).toString());
                    appreciation.setUser_id(customerInfo.getUserid());
                    appreciation.setCommentary(value.get(3).toString());
                    appreciation.setRemarks(value.get(5).toString());
                    appreciation.setOther1(value.get(6).toString());
                    appreciation.setOther2(value.get(7).toString());
                    appreciation.setOther3(value.get(8).toString());
                    appreciation.setOther4(value.get(9).toString());
                    appreciation.setOther5(value.get(10).toString());
                }
                int rowundex = i;
                appreciation.setRowindex(rowundex);
                appreciation.preInsert(tokenModel);
                appreciation.setAppreciation_id(UUID.randomUUID().toString());
                appreciationMapper.insert(appreciation);
                listVo.add(appreciation);
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
