package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.dao_Pfans.PFANS2000.OtherTwo2;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
import com.nt.service_pfans.PFANS2000.mapper.GivingMapper;
import com.nt.service_pfans.PFANS2000.mapper.OtherTwo2Mapper;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OtherTwoServiceImpl implements OtherTwoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Autowired
    private GivingMapper givingMapper;

    @Autowired
    private OtherTwo2Mapper othertwo2Mapper;

    @Override
    public void deleteteothertwo(OtherTwo othertwo, TokenModel tokenModel) throws Exception {
        othertwoMapper.delete(othertwo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUserothertwo(String Givingid, HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            OtherTwo two = new OtherTwo();
            two.setGiving_id(Givingid);
            two.setType("1");//只删除导入的部分
            othertwoMapper.delete(two);
            List<CustomerInfo> customerinfoAll = mongoTemplate.findAll(CustomerInfo.class);
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
            //model.add("工号");
            model.add("姓名");
            model.add("金額");
            model.add("根拠");
            List<Object> key = list.get(0);
            for (int i = 0; i <= key.size()-1; i++) {
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
                    //卡号 upd gbb 0727 start
                    //upd gbb 0904 添加 value.get(2).toString().equals("")
                    if (value.get(1).toString().equals("") || value.get(2).toString().equals("")) {
                        continue;
                    }
                    String strCustomername = value.get(1).toString();
                    List<CustomerInfo> customerinfo = customerinfoAll.stream().filter(item -> (item.getUserinfo().getCustomername().equals(strCustomername))).collect(Collectors.toList());
                    if(customerinfo.size() == 0){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的姓名没有找到，请输入正确的姓名，导入失败");
                        continue;
                    }
                    else{
                        othertwo.setUser_id(customerinfo.get(0).getUserid());
                        othertwo.setJobnumber(customerinfo.get(0).getUserinfo().getJobnumber());
                    }
                    //卡号 upd gbb 0727 end
                    String click = "^(-?[0-9]*)+(.[0-9]{1,2})?$";
                    if(!value.get(2).toString().equals("")){
                        if(!value.get(2).toString().equals("0")){
                            value.set(2,new BigDecimal(Double.parseDouble(value.get(2).toString())).setScale(2, RoundingMode.HALF_UP).toPlainString());
                            if (!Pattern.matches(click, value.get(2).toString())) {
                                error = error + 1;
                                Result.add("模板第" + (k - 1) + "行的金额不符合规范，请输入正确的金额，导入失败");
                                continue;
                            }
                        }
                    }
                    if (value.size() > 3) {
                        if (value.get(2).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 4) {
                        if (value.get(3).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的根拠长度超出范围，请输入长度为20位之内的根拠，导入失败");
                            continue;
                        }
                    }
                    othertwo.setGiving_id(Givingid);
                    othertwo.setMoneys(value.get(2).toString());
                    othertwo.setRootknot(value.get(3).toString());
                }
                int rowundex = accesscount + 1;
                othertwo.setRowindex(rowundex);
                othertwo.setType("1");
                othertwo.preInsert(tokenModel);
                othertwo.setOthertwo_id(UUID.randomUUID().toString());
                othertwoMapper.insert(othertwo);
//                List<OtherTwo2> otherTwo2List = givingMapper.selectOthertwo(Givingid);
//                if(otherTwo2List.size()>0){
//                    for(OtherTwo2 otherTwo2 :otherTwo2List){
//                        otherTwo2.preInsert(tokenModel);
//                        otherTwo2.setUser_id(otherTwo2.getUser_id());
//                        otherTwo2.setMoneys(otherTwo2.getMoneys());
//                        otherTwo2.setOthertwo2_id(UUID.randomUUID().toString());
//                        othertwo2Mapper.insert(otherTwo2);
//                    }
//                }
                //listVo.add(othertwo);
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
