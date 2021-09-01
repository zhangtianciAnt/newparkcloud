package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_Org.ToDoNoticeService;
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
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class BonussendServiceImpl implements BonussendService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BonussendMapper bonussendMapper;
    // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 start
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 send

    @Override
    public void update(List<Bonussend> bonussend, TokenModel tokenModel) throws Exception {
        for (Bonussend bo : bonussend){
            bo.preInsert(tokenModel);
            bonussendMapper.updateByPrimaryKey(bo);
        }
    }

    @Override
    // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 start
    //public void updateSend(String id) throws Exception {
    public void updateSend(List<Bonussend> bonussend, TokenModel tokenModel) throws Exception {
//        Bonussend bonussend = bonussendMapper.selectByPrimaryKey(id);
//        bonussend.setSent("1");
//        bonussendMapper.updateByPrimaryKeySelective(bonussend);
        for (Bonussend bo : bonussend){
            bo.preUpdate(tokenModel);
            bo.setSent("1");
            bonussendMapper.updateByPrimaryKey(bo);
            // 创建代办
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setNoticeid(UUID.randomUUID().toString());
            toDoNotice.setTitle("您的奖金已发送");
            toDoNotice.setInitiator(bo.getUser_id());
            toDoNotice.setContent("您的奖金已发送");
            toDoNotice.setDataid(bo.getBonussend_id());
            toDoNotice.setUrl("/PFANS2028View");
            toDoNotice.setWorkflowurl("/PFANS2028View");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(bo.getUser_id());
            toDoNoticeService.save(toDoNotice);
        }
        // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 end
    }

    @Override
    public List<Bonussend> inserttodo(Bonussend bonussend) {
        return bonussendMapper.select(bonussend);
    }

    @Override
    public List<Bonussend> List(Bonussend bonussend, TokenModel tokenModel) throws Exception {

        return bonussendMapper.select(bonussend);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
//            创建listVo集合方便存储导入信息
            List<Bonussend> listVo = new ArrayList<Bonussend>();
//            创建Result结果集的集合
            List<String> Result = new ArrayList<String>();
//            用来接收前台传过来的文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            创建对象f，且为空
            File f = null;
//            创建临时文件
            f = File.createTempFile("tmp", null);
//            上传文件
            file.transferTo(f);
//            使用Excel读文件
            ExcelReader reader = ExcelUtil.getReader(f);
//            创建集合存入读的文件
            List<List<Object>> list = reader.read();
//            创建集合存入标准模板
            List<Object> model = new ArrayList<Object>();
//            标准模板
            model.add("年度");
            model.add("姓名");  //update_qhr_20210830 修改模板列名
            model.add("奖金总额（元）");
            model.add("纳税方法（个人选择）");
            model.add("综合收入应纳税金（元）");
            model.add("工资已纳税金额（元）");
            model.add("应补缴税额（元）");
            model.add("一次性奖金应纳税所得额月平均额（元）");
            model.add("一次性奖金税率（%）");
            model.add("一次性奖金速算扣除数（元）");
            model.add("一次性奖金税金（元）");
            model.add("到手金额（元）");
            model.add("备注");
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
                Bonussend bonussend = new Bonussend();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 start
//                    String click = "^([1-9][0-9]*)+(.[0-9]{1,2})?$";
//                    if (!Pattern.matches(click, value.get(4).toString())) {
//                        error = error + 1;
//                        Result.add("模板第" + (k - 1) + "行的金额不符合规范，请输入正确的金额，导入失败");
//                        continue;
//                    }
//                    if (value.size() > 4) {
//                        if (value.get(4).toString().length() > 20) {
//                            error = error + 1;
//                            Result.add("模板第" + (k - 1) + "行的金额长度超出范围，请输入长度为20位之内的金额，导入失败");
//                            continue;
//                        }
//                    }
                    Query query = new Query();
//                    region update_qhr_20210830  修改检查项为名字
                    String customername = value.get(1).toString();
                    query.addCriteria(Criteria.where("userinfo.customername").is(customername));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的姓名字段没有找到，请输入正确的姓名，导入失败");
                        continue;
                    }
//                    endregion update_qhr_20210830  修改检查项为名字
                    if (customerInfo != null) {
                        bonussend.setUser_id(customerInfo.getUserid());
                        bonussend.setUsername(customerInfo.getUserinfo().getCustomername());
                    }
                    if(value.size() > 0){
                        bonussend.setYears(value.get(0).toString());
                    }
                    if(value.size() > 1){
                        bonussend.setJobnumber(customerInfo.getUserinfo().getJobnumber());  //update_qhr_20210830 修改保存项
                    }
                    if(value.size() > 2){
                        bonussend.setTotalbonus1(value.get(2).toString());
                    }
                    if(value.size() > 3){
                        bonussend.setMethod(value.get(3).toString());
                    }
                    if(value.size() > 4){
                        bonussend.setTaxable(value.get(4).toString());
                    }
                    if(value.size() > 5){
                        bonussend.setAmount(value.get(5).toString());
                    }
                    if(value.size() > 6){
                        bonussend.setPayable(value.get(6).toString());
                    }
                    if(value.size() > 7){
                        bonussend.setIncome(value.get(7).toString());
                    }
                    if(value.size() > 8){
                        bonussend.setTaxrate(value.get(8).toString());
                    }
                    if(value.size() > 9){
                        bonussend.setDeductions(value.get(9).toString());
                    }
                    if(value.size() > 10){
                        bonussend.setBonustax(value.get(10).toString());
                    }
                    if(value.size() > 11){
                        bonussend.setReceived(value.get(11).toString());
                    }
                    if(value.size() > 12){
                        bonussend.setRemarks(value.get(12).toString());
                    }
                }
                // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 start
                bonussend.preInsert();
                bonussend.setBonussend_id(UUID.randomUUID().toString());
//                bonussendMapper.insert(bonussend);  //update_qhr_20210830 在下方进行保存
                listVo.add(bonussend);
//                accesscount = accesscount + 1;  //update_qhr_20210830 在下方进行保存
            }
            //region add_qhr_20210830 添加导入数据的去重，保证每年每人有一条数据
            listVo = listVo.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Bonussend::getUsername))), ArrayList::new));
            for (Bonussend bonussend : listVo) {
                Bonussend bonus = new Bonussend();
                bonus.setUsername(bonussend.getUsername());
                bonus.setYears(bonussend.getYears());
                bonus.setCalculation("0");
                List<Bonussend> bonussends = bonussendMapper.select(bonus);
                if (bonussends.size() > 0) {
                    bonussend.setBonussend_id(bonussends.get(0).getBonussend_id());
                    bonussendMapper.updateByPrimaryKey(bonussend);
                    accesscount = accesscount + 1;
                } else {
                    bonussendMapper.insert(bonussend);
                    accesscount = accesscount + 1;
                }
            }
//            endregion add_qhr_20210830 添加导入数据的去重，保证每年每人有一条数据
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
