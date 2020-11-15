package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.IOUtils;
import com.nt.dao_AOCHUAN.AOCHUAN5000.AcctgRul;
import com.nt.dao_AOCHUAN.AOCHUAN5000.AuxAcctg;
import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.KisLogin;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAcctgRulMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAuxAcctgMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinCrdlInfoMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FinCdrlInfoServiceImpl implements FinCrdlInfoService {

    @Autowired
    private FinCrdlInfoMapper finCrdlInfoMapper;

    @Autowired
    private FinAcctgRulMapper finAcctgRulMapper;

    @Autowired
    private FinAuxAcctgMapper finAuxAcctgMapper;

    @Autowired
    private K3CloundConfig k3CloundConfig;

    //获取凭证信息
    @Override
    public List<CredentialInformation> getCrdlInfo(CredentialInformation credentialInformation) throws Exception {
        return finCrdlInfoMapper.select(credentialInformation);
    }
    //add_fjl_1021 推送KIS
    //金蝶批量保存
    public ResultVo batchSave(String url,String cookie, String content,TokenModel tokenModel,List<CredentialInformation> credentialInformationList) throws Exception {
        //保存
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookie);
        String result = HttpUtil.httpPost(url, header, content);
        JSONObject jsonObject = JSON.parseObject(result);
        Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
        Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");
        Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");

        if (isSuccess) {
            //获取返回值   kisid
            Object ob = responseStatus.get("SuccessEntitys");
            for(int i = 0; i < ((JSONArray) ob).size(); i++){
                Integer kisid = (Integer)((JSONObject) ((JSONArray) ob).get(i)).get("Id");
                String number = (String)((JSONObject) ((JSONArray) ob).get(i)).get("Number");
                System.out.println("=============================");
                System.out.println("kisid:"+kisid);
                System.out.println("number:"+number);
                System.out.println("=============================");
                updKisid(kisid,number,tokenModel,credentialInformationList.get(i));
            }
            return ResultUtil.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return ResultUtil.error(JSON.toJSONString(errors));
        }
    }

    @Override
    public void login1(List<CredentialInformation> credentialInformation,TokenModel tokenModel) throws Exception {
        //正式dbid：5f1533095ad35e
        //测试dbid：5f4f0eaa667840
//      String loginParam = buildLogin("5f4f0eaa667840", "Administrator", "888888", 2052);
        KisLogin kisLogin = finCrdlInfoMapper.selectKislogin();
        String loginParam = "";
        if(kisLogin != null){
            loginParam = BaseUtil.buildLogin(kisLogin.getKisloginid(), kisLogin.getUsername(), kisLogin.getUserpassword(), kisLogin.getLcid());
        }

        ResultVo login = BaseUtil.login(k3CloundConfig.url + k3CloundConfig.login, loginParam);

        if (login.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【登录金蝶系统失败】：{}", login.getMsg());
            throw new LogicalException("登录金蝶系统失败");
//            return;
        }

        Map<String, Object> map = (Map<String, Object>) login.getData();
        String cookie = map.get("cookie").toString();

        //获取凭证数据模板
        File file = null;
//        file = ResourceUtils.getFile("classpath:excel/voucher.json");
        ClassPathResource resource  = new ClassPathResource("excel/voucher.json");
        //判断是否存在
        if (resource.exists()) {
            //获取流
            InputStream inputStream = resource.getInputStream();
            Date now = new Date();
            //创建一个json格式的临时文件
            file = File.createTempFile(now.getTime() + "", ".json");
            try {
                //将流写入到你创建的新文件中
                byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
                FileCopyUtils.copy(bdata, file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
        String jsonData = BaseUtil.jsonRead(file);
        List<Map<String,Object>> listmap = new ArrayList();
        JSONObject basic = null;
        SimpleDateFormat stf = new SimpleDateFormat("yyyy/MM/dd");
        if(credentialInformation.size() > 0){
            for(CredentialInformation cinfo : credentialInformation){
                int kis = 0;
                CredentialInformation creinfo = finCrdlInfoMapper.selectByPrimaryKey(cinfo.getCrdlinfo_id());
                if(creinfo != null){
                    if(StringUtils.isNotEmpty(creinfo.getCrdlkis_num())){
                        kis = Integer.valueOf(creinfo.getCrdlkis_num());
                    }
                }
                List<Map<String,Object>> listmapFentity = new ArrayList();
                AccountingRule accountingRule = new AccountingRule();
                accountingRule.setCrdlinfo_fid(cinfo.getCrdlinfo_id());
                List<AccountingRule> accountingRuleList = finAcctgRulMapper.getAcctgEntrInfoByCrdl_id(cinfo.getCrdlinfo_id());
                basic = JSON.parseObject(jsonData);
                Map<String, Object> model = (Map<String, Object>)basic.get("Model");//单据头
                if(accountingRuleList.size() > 0){
                    for(AccountingRule ar : accountingRuleList){
                        //获取凭证数据模板
                        File file1 = null;
//                        file1 = ResourceUtils.getFile("classpath:excel/voucher.json");
                        ClassPathResource resource1  = new ClassPathResource("excel/voucher.json");
                        //判断是否存在
                        if (resource1.exists()) {
                            //获取流
                            InputStream inputStream = resource1.getInputStream();
                            Date now = new Date();
                            //创建一个json格式的临时文件
                            file1 = File.createTempFile(now.getTime() + "", ".json");
                            try {
                                //将流写入到你创建的新文件中
                                byte[] bdata = FileCopyUtils.copyToByteArray(resource1.getInputStream());
                                FileCopyUtils.copy(bdata, file1);
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        String jsonData1 = BaseUtil.jsonRead(file1);
                        JSONObject basic1 = null;
                        basic1 = JSON.parseObject(jsonData1);
                        Map<String, Object> fentity = (Map<String, Object>)((Map<String, Object>) basic1.get("Model")).get("FEntity");//单据体
                        Map<String, Object> faccountid = (Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FACCOUNTID");//科目编码
                        Map<String, Object> fcurrencyid = (Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FCURRENCYID");//币种
                        Map<String, Object> fdetailid = (Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID");//核算维度
                        Map<String, Object> fdetailid__fflex6 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX6");//客户
                        Map<String, Object> fdetailid__fflex7 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX7");//员工
                        Map<String, Object> fdetailid__fflex8 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX8");//产品
                        Map<String, Object> fdetailid__fflex9 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX9");//费用项目
                        Map<String, Object> fdetailid__fflex10 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX10");//资产类别
                        Map<String, Object> fdetailid__fflex11 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX11");//组织机构
                        Map<String, Object> fdetailid__fflex12 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX12");//产品分组
                        Map<String, Object> fdetailid__fflex13 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX13");//客户分组
                        Map<String, Object> fdetailid__fflex4 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX4");//供应商
                        Map<String, Object> fdetailid__fflex5 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FFLEX5");//部门
                        Map<String, Object> fdetailid__ff100002 = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FDetailID")).get("FDETAILID__FF100002");//国家
                        fentity.put("FEXPLANATION",ar.getRemarks());//摘要
                        faccountid.put("FNumber",ar.getAcct_code()); //科目编码
                        if(StringUtils.isNotEmpty(ar.getDimension())){
                            fdetailid__fflex4.put("FNumber",ar.getFdetailid__fflex4());//供应商
                            fdetailid__fflex5.put("FNumber",ar.getFdetailid__fflex5());//部门
                            fdetailid__fflex6.put("FNumber",ar.getFdetailid__fflex6());//客户
                            fdetailid__fflex7.put("FNumber",ar.getFdetailid__fflex7());//员工
                            fdetailid__fflex8.put("FNumber",ar.getFdetailid__fflex8());//产品
                            fdetailid__fflex9.put("FNumber",ar.getFdetailid__fflex9());//费用项目
                            fdetailid__fflex10.put("FNumber",ar.getFdetailid__fflex10());//资产类别
                            fdetailid__fflex11.put("FNumber",ar.getFdetailid__fflex11());//组织机构
                            fdetailid__fflex12.put("FNumber",ar.getFdetailid__fflex12());//产品分组
                            fdetailid__fflex13.put("FNumber",ar.getFdetailid__fflex13());//客户分组
                            fdetailid__ff100002.put("FNumber",ar.getFdetailid__ff100002());//国家
                        }
                        fdetailid.put("FDETAILID__FFLEX4",fdetailid__fflex4);
                        fdetailid.put("FDETAILID__FFLEX5",fdetailid__fflex5);
                        fdetailid.put("FDETAILID__FFLEX6",fdetailid__fflex6);
                        fdetailid.put("FDETAILID__FFLEX7",fdetailid__fflex7);
                        fdetailid.put("FDETAILID__FFLEX8",fdetailid__fflex8);
                        fdetailid.put("FDETAILID__FFLEX9",fdetailid__fflex9);
                        fdetailid.put("FDETAILID__FFLEX10",fdetailid__fflex10);
                        fdetailid.put("FDETAILID__FFLEX11",fdetailid__fflex11);
                        fdetailid.put("FDETAILID__FFLEX12",fdetailid__fflex12);
                        fdetailid.put("FDETAILID__FFLEX13",fdetailid__fflex13);
                        fdetailid.put("FDETAILID__FF100002",fdetailid__ff100002);
                        fentity.put("FDetailID",fdetailid);
                        if(StringUtils.isNotEmpty(ar.getCurrency())){
                            if(ar.getCurrency().equals("PY008001")){
                                ar.setCurrency("PRE001");//人民币
                            } else if(ar.getCurrency().equals("PY008002")){
                                ar.setCurrency("PRE007");//美元
                            } else if(ar.getCurrency().equals("PY008004")){
                                ar.setCurrency("PRE003");//欧元
                            }
                        }
                        fcurrencyid.put("FNumber",ar.getCurrency());//币别
                        fentity.put("FEXCHANGERATE",ar.getEx_rate());//汇率
                        fentity.put("FAMOUNTFOR",ar.getOricurrency_amount());//原币金额
                        if(StringUtils.isNotEmpty(ar.getDebit())){//借方科目
                            fentity.put("FDEBIT",ar.getAmount());//借方金额
                        } else {
                            fentity.put("FCREDIT",ar.getAmount());//贷方金额
                        }

                        listmapFentity.add(fentity);
                    }

                }
                model.put("FVOUCHERID",kis);//金蝶主键
                if(cinfo.getAcct_date() != null){
                    Date date = new Date(stf.format(cinfo.getAcct_date()));
                    model.put("FDate",stf.format(cinfo.getAcct_date()));//记账日期
                    model.put("FYEAR",String.format("%tY", date));//会计年度
                    model.put("FPERIOD",Integer.valueOf(String.format("%tm", date)));//期间
                }
                if(cinfo.getBus_date() != null){
                    model.put("FBUSDATE",stf.format(cinfo.getBus_date()));//业务日期
                }
                model.put("FATTACHMENTS",cinfo.getAttachments());//附件数
                model.put("FEntity",listmapFentity);//单据体
                listmap.add(model);
            }
        }
        basic.put("Model",listmap);

        //构造凭证接口数据
        String voucher = BaseUtil.buildMaterial(basic, KDFormIdEnum.VOUCHER.getFormid());

        ResultVo save = batchSave(k3CloundConfig.url + k3CloundConfig.batchSave, cookie, voucher,tokenModel,credentialInformation);
        if (save.getCode() != ResultEnum.SUCCESS.getCode()) {
            String mes = save.getMsg();
            String[] messageArr = mes.split("},");
            String[] messages = new String[messageArr.length];
            for(int a = 0 ; a < messageArr.length; a++){
                messages[a] = messageArr[a].split("\",\"FieldName\"")[0];
            }
            System.out.println(messages);
            String diamessage = "";
            for(int a = 0 ; a < messages.length; a ++){
                diamessage = diamessage + messages[a];
            }


//            log.error("【保存出错】：{}", save.getMsg());
            throw new LogicalException("数据推送出错 ：" +save.getMsg());
//            return;
        }

    }
    //返回kisid更新回去
    public void updKisid(Integer kisid, String number, TokenModel tokenModel,CredentialInformation credentialInformation) throws Exception {
        CredentialInformation sup = finCrdlInfoMapper.selectByPrimaryKey(credentialInformation.getCrdlinfo_id());
        if (sup != null) {
            sup.preUpdate(tokenModel);
            sup.setCrdlkis_num(kisid.toString());
            sup.setPush_status("PZ005002");//已推送
            finCrdlInfoMapper.updateByPrimaryKeySelective(sup);
        }
    }
    //获取分录-辅助项目数据
    @Override
    public List<AccountingRule> getAcctgEntrInfoByCrdl_id(CredentialInformation credentialInformation) throws Exception {

        return finAcctgRulMapper.getAcctgEntrInfoByCrdl_id(credentialInformation.getCrdlinfo_id());
    }

    //更新
    @Override
    public Boolean updateKisCrdlNo(CredentialInformation credentialInformation, TokenModel tokenModel) throws Exception {
        if (existCheckCdrl(credentialInformation)) {
            if (!uniqueCheckCdrl(credentialInformation)) {

                credentialInformation.preUpdate(tokenModel);
                String pushStatus = "'PZ0051002'";
                finCrdlInfoMapper.updateKisCrdlNo(credentialInformation.getModifyby(), credentialInformation.getCrdlkis_num(),pushStatus, credentialInformation.getCrdlinfo_id());
            }else{
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    //删除
    @Override
    public Boolean delete(CrdlInfo crdlInfo, TokenModel tokenModel) throws Exception {

        CredentialInformation credentialInformation = crdlInfo.getCredentialInformation();

        List<AccountingRule> accountingRuleList = crdlInfo.getAccountingRuleList();

        if(accountingRuleList.isEmpty()){
            return false;
        }else{
            //辅助核算项目
            for (AccountingRule item : accountingRuleList) {

                if (existCheckAux(item.getAuxacctg_id())) {

                    item.preUpdate(tokenModel);
                    finAcctgRulMapper.delAuxAcctgByFid(item.getModifyby(), item.getAcctgrul_fid());
                }
            }

            //分录
            if (existCheckAcc(accountingRuleList.get(0).getAcctgrul_id())) {
                accountingRuleList.get(0).preUpdate(tokenModel);
                finAcctgRulMapper.delAcctgRulByFid(accountingRuleList.get(0).getModifyby(), accountingRuleList.get(0).getCrdlinfo_fid());
            } else {
                return false;
            }
        }

        if(credentialInformation == null){
            return false;
        }else{
        //凭证信息
        if (existCheckCdrl(credentialInformation)) {
            if (!uniqueCheckCdrl(credentialInformation)) {
                credentialInformation.preUpdate(tokenModel);
                finCrdlInfoMapper.delCrdlInfoById(credentialInformation.getModifyby(), credentialInformation.getCrdlinfo_id());
            } else {
                return false;
            }
        } else {
            return false;
        }
        }

        return true;
    }

    //新建
    @Override
    public Boolean insert(CrdlInfo crdlInfo, TokenModel tokenModel) throws Exception {

        //凭证insert
        String crdlId= UUID.randomUUID().toString();

        CredentialInformation credentialInformation = crdlInfo.getCredentialInformation();
        credentialInformation.setCrdlinfo_id(crdlId);
        credentialInformation.preInsert(tokenModel);
        finCrdlInfoMapper.insertSelective(credentialInformation);

        List<AccountingRule> accountingRuleList = crdlInfo.getAccountingRuleList();
        if(!accountingRuleList.isEmpty()){

            for (AccountingRule item: accountingRuleList) {

                //分录insert
                AcctgRul acctgRul = new AcctgRul();
                acctgRul.setAcctgrul_id(UUID.randomUUID().toString());
                acctgRul.setCrdlinfo_fid(crdlId);
                acctgRul.setRemarks(item.getRemarks());
                acctgRul.setAcct_code(item.getAcct_code());
                acctgRul.setDebit(item.getDebit());
                acctgRul.setCredit(item.getCredit());
                acctgRul.setCurrency(item.getCurrency());//币种
                acctgRul.setEx_rate(item.getEx_rate());//汇率
                acctgRul.setTaxrate(item.getTaxrate());//税率
                acctgRul.setOricurrency_amount(item.getOricurrency_amount());//原币金额
                acctgRul.setUnit(item.getUnit());//单位
                acctgRul.setUnit_price(item.getUnit_price());//单价
                acctgRul.setQuantity(item.getQuantity());//数量
                acctgRul.setAmount(item.getAmount());
                acctgRul.setDimension(item.getDimension());
                acctgRul.setFdetailid__fflex4(item.getFdetailid__fflex4());
                acctgRul.setFdetailid__fflex5(item.getFdetailid__fflex5());
                acctgRul.setFdetailid__fflex6(item.getFdetailid__fflex6());
                acctgRul.setFdetailid__fflex7(item.getFdetailid__fflex7());
                acctgRul.setFdetailid__fflex8(item.getFdetailid__fflex8());
                acctgRul.setFdetailid__fflex9(item.getFdetailid__fflex9());
                acctgRul.setFdetailid__fflex10(item.getFdetailid__fflex10());
                acctgRul.setFdetailid__fflex11(item.getFdetailid__fflex11());
                acctgRul.setFdetailid__fflex12(item.getFdetailid__fflex12());
                acctgRul.setFdetailid__fflex13(item.getFdetailid__fflex13());
                acctgRul.setFdetailid__ff100002(item.getFdetailid__ff100002());
                acctgRul.setRowindex(item.getRowindex());
                acctgRul.preInsert(tokenModel);
                finAcctgRulMapper.insertSelective(acctgRul);

                //辅助核算项目insert
                AuxAcctg auxAcctg = new AuxAcctg();
                auxAcctg.setAuxacctg_id(UUID.randomUUID().toString());
                auxAcctg.setAcctgrul_fid(acctgRul.getAcctgrul_id());
                auxAcctg.setBankaccount_code(item.getBankaccount_code());
                auxAcctg.setDept_code(item.getDept_code());
                auxAcctg.setIae_contg_code(item.getIae_contg_code());
                auxAcctg.setAuxacctg_code(item.getAuxacctg_code());
                auxAcctg.setMaincash_code(item.getMaincash_code());
                auxAcctg.setAttachcash_code(item.getAttachcash_code());
                auxAcctg.setBankaccount(item.getBankaccount());
                auxAcctg.setDept(item.getDept());
                auxAcctg.setIae_contg(item.getIae_contg());
                auxAcctg.setAuxacctg(item.getAuxacctg());
                auxAcctg.setMaincashflow(item.getMaincashflow());
                auxAcctg.setAttachcashflow(item.getAttachcashflow());
                auxAcctg.setAuxacctg_amount(item.getAuxacctg_amount());
                auxAcctg.preInsert(tokenModel);
                finAuxAcctgMapper.insertSelective(auxAcctg);
            }
        }else{
            return false;
        }
        return true;
    }

    @Override
    public CredentialInformation getForm(String id) throws Exception {
        return finCrdlInfoMapper.selectByPrimaryKey(id);
    }

    //存在Check
    public Boolean existCheckCdrl(CredentialInformation credentialInformation) throws Exception {
        List<CredentialInformation> resultLst = finCrdlInfoMapper.existCheck(credentialInformation.getCrdlinfo_id());
        if (resultLst.isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean existCheckAcc(String id) throws Exception {
        int count = finAcctgRulMapper.existCheckAcc(id);
        if (count == 0) {
            return false;
        }
        return true;
    }

    public Boolean existCheckAux(String id) throws Exception {
        int count = finAcctgRulMapper.existCheckAux(id);
        if (count == 0) {
            return false;
        }
        return true;
    }

    //唯一性Check
    public Boolean uniqueCheckCdrl(CredentialInformation credentialInformation) throws Exception {
        List<CredentialInformation> resultLst = finCrdlInfoMapper.uniqueCheck(credentialInformation.getCrdlinfo_id(), credentialInformation.getCrdl_num());
        if (resultLst.isEmpty()) {
            return false;
        }
        return true;
    }
}
