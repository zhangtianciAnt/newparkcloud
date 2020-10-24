package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nt.dao_AOCHUAN.AOCHUAN5000.AcctgRul;
import com.nt.dao_AOCHUAN.AOCHUAN5000.AuxAcctg;
import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAcctgRulMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAuxAcctgMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinCrdlInfoMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
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
    //金蝶login
    public ResultVo login(String url,String content) {

        ResponseEntity<String> responseEntity = HttpUtil.httpPost(url, content);
        //获取登录cookie
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            String login_cookie = "";
            Set<String> keys = responseEntity.getHeaders().keySet();
            for(String key:keys){
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    List<String> cookies = responseEntity.getHeaders().get(key);
                    for(String cookie:cookies){
                        if(cookie.startsWith("kdservice-sessionid")){
                            login_cookie=cookie;
                            break;
                        }
                    }
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("cookie",login_cookie);
            return ResultUtil.success(map);
        }

        Map<String,Object> result = JSON.parseObject(responseEntity.getBody());
        return ResultUtil.error(result.get("Message").toString());
    }

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

        String loginParam = BaseUtil.buildLogin("5f4f0eaa667840", "Administrator", "888888", 2052);

        ResultVo login = login(k3CloundConfig.url + k3CloundConfig.login, loginParam);

        if (login.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【登录金蝶系统失败】：{}", login.getMsg());
            throw new LogicalException("登录金蝶系统失败");
//            return;
        }

        Map<String, Object> map = (Map<String, Object>) login.getData();
        String cookie = map.get("cookie").toString();

        //获取供应商数据模板
        File file = null;
        file = ResourceUtils.getFile("classpath:excel/voucher.json");
        String jsonData = BaseUtil.jsonRead(file);
        List<Map<String,Object>> listmap = new ArrayList();
        JSONObject basic = null;
        SimpleDateFormat stf = new SimpleDateFormat("yyyy/MM/dd");
        if(credentialInformation.size() > 0){
            for(CredentialInformation cinfo : credentialInformation){
                List<Map<String,Object>> listmapFentity = new ArrayList();
                AccountingRule accountingRule = new AccountingRule();
                accountingRule.setCrdlinfo_fid(cinfo.getCrdlinfo_id());
                List<AccountingRule> accountingRuleList = finAcctgRulMapper.getAcctgEntrInfoByCrdl_id(cinfo.getCrdlinfo_id());
                basic = JSON.parseObject(jsonData);
                Map<String, Object> model = (Map<String, Object>)basic.get("Model");//单据头
                if(accountingRuleList.size() > 0){
                    for(AccountingRule ar : accountingRuleList){

                        //获取供应商数据模板
                        File file1 = null;
                        file1 = ResourceUtils.getFile("classpath:excel/voucher.json");
                        String jsonData1 = BaseUtil.jsonRead(file1);
                        JSONObject basic1 = null;
                        basic1 = JSON.parseObject(jsonData1);
                        Map<String, Object> fentity = (Map<String, Object>)((Map<String, Object>) basic1.get("Model")).get("FEntity");//单据体
                        Map<String, Object> faccountid = (Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FACCOUNTID");//科目编码
                        Map<String, Object> fcurrencyid = (Map<String, Object>)((Map<String, Object>) ((Map<String, Object>) basic1.get("Model")).get("FEntity")).get("FCURRENCYID");//币种
                        fentity.put("FEXPLANATION",ar.getRemarks());//摘要
                        faccountid.put("FNumber",ar.getAcct_code()); //科目编码
//                        fcurrencyid.put("FNumber",ar.getCurrency());//币别
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
                if(cinfo.getAcct_date() != null){
                    Date date = new Date(stf.format(cinfo.getAcct_date()));
                    model.put("FDate",stf.format(cinfo.getAcct_date()));//记账日期
                    model.put("FYEAR",String.format("%tY", date));//会计年度
                    model.put("FPERIOD",Integer.valueOf(String.format("%tm", date)));//期间
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
            throw new LogicalException("数据推送出错 ：" + diamessage);
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
