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
import java.util.stream.Collectors;

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
            OtherFour Four = new OtherFour();
            Four.setGiving_id(Givingid);
            otherFourMapper.delete(Four);
            List<CustomerInfo> customerinfoAll = mongoTemplate.findAll(CustomerInfo.class);
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
            //model.add("工号");
            model.add("姓名");
            model.add("社保大病险");
            model.add("合計");
            model.add("备注");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size()-1; i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i <= list.size() - 1; i++) {
                OtherFour otherFour = new OtherFour();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    //卡号 upd gbb 0727 start
                    //卡号 upd gbb 0904 添加 value.get(3).toString().equals("")
                    if (value.get(2).toString().equals("") || value.get(3).toString().equals("")) {
                        continue;
                    }
                    String strCustomername = value.get(2).toString();
                    List<CustomerInfo> customerinfo = customerinfoAll.stream().filter(item -> (item.getUserinfo().getCustomername().equals(strCustomername))).collect(Collectors.toList());
                    if(customerinfo.size() == 0){
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的姓名没有找到，请输入正确的姓名，导入失败");
                        continue;
                    }
                    else{
                        otherFour.setUser_id(customerinfo.get(0).getUserid());
                        otherFour.setJobnumber(customerinfo.get(0).getUserinfo().getJobnumber());
                    }
                    //卡号 upd gbb 0727 end
                    String click="^(-?[0-9][0-9]*)+(.[0-9]{1,2})?$";
                    if(!value.get(3).toString().equals("0")){
                        if(!Pattern.matches(click, value.get(3).toString())){
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额不符合规范，请输入正确的金额，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 4) {
                        if (value.get(3).toString().length() > 20) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
                            continue;
                        }
                    }
                    otherFour.setGiving_id(Givingid);
                    otherFour.setDepartment_id(value.get(1).toString());
                    otherFour.setSocialsecurity(value.get(3).toString());
                    otherFour.setTotal(value.get(4).toString());
                    otherFour.setRemarks(value.get(5).toString());
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
