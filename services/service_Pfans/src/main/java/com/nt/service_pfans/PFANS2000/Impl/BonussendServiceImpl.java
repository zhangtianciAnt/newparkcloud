package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.service_pfans.PFANS2000.BonussendService;
import com.nt.service_pfans.PFANS2000.mapper.BonussendMapper;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor=Exception.class)
public class BonussendServiceImpl implements BonussendService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BonussendMapper bonussendMapper;

    @Override
    public List<Bonussend> getListType(Bonussend bonussend ) {
        return bonussendMapper.select(bonussend);
    }

    @Override
    public List<Bonussend> List(Bonussend bonussend,TokenModel tokenModel) throws Exception {
        return bonussendMapper.select(bonussend);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("工号");
//            model.add("奖金总额（元）");
//            model.add("纳税方法（个人选择）");
//            model.add("综合收入应纳税金（元）");
//            model.add("工资已纳税金额（元）");
//            model.add("应补缴税额（元）");
//            model.add("一次性奖金应纳税所得额月平均额（元）");
//            model.add("一次性奖金税率（%）");
//            model.add("一次性奖金速算扣除数（元）");
//            model.add("一次性奖金税金（元）");
//            model.add("到手金额（元）");
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
            for (int i = 1; i <= list.size(); i++) {
                Bonussend bonussend = new Bonussend();
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
                        bonussend.setUser_id(customerInfo.getUserid());
                        bonussend.setUsername(customerInfo.getUserid());
                    }
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的工号字段没有找到，请输入正确的工号，导入失败");
                        continue;
                    }
                    bonussend.setRemarks(value.get(13).toString());
                }
                bonussend.preInsert(tokenModel);
                bonussend.setBonussend_id(UUID.randomUUID().toString());
                bonussendMapper.insert(bonussend);
                accesscount = accesscount + 1;
            }
            Bonussend bonussend = new Bonussend();
//            for (Bonussend punchcard : bonussendlist) {
//
//                Bonussend del = new Bonussend();
//                del.setUser_id(punchcard.getUser_id());
//                bonussendMapper.delete(del);
//
//                bonussend.setJobnumber(punchcard.getJobnumber());
//                bonussend.setUser_id(punchcard.getUser_id());
//                bonussend.setUsername(punchcard.getUsername());
//                bonussend.setTotalbonus1(punchcard.getTotalbonus1());
//                bonussend.setMethod(punchcard.getMethod());
//                bonussend.setBonussend_id(UUID.randomUUID().toString());
//                bonussend.setTaxable(punchcard.getTaxable());
//                bonussend.setAmount(punchcard.getAmount());
//                bonussend.setPayable(punchcard.getPayable());
//                bonussend.setIncome(punchcard.getIncome());
//                bonussend.setTaxrate(punchcard.getTaxrate());
//                bonussend.setDeductions(punchcard.getDeductions());
//                bonussend.setBonustax(punchcard.getBonustax());
//                bonussend.setReceived(punchcard.getReceived());
//                bonussend.setRemarks(punchcard.getRemarks());
//                bonussend.setSent(punchcard.getSent());
//                bonussend.preInsert(tokenModel);
//                bonussendMapper.insert(bonussend);
//            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
