package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN5000.AcctgRul;
import com.nt.dao_AOCHUAN.AOCHUAN5000.AuxAcctg;
import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAcctgRulMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAuxAcctgMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinCrdlInfoMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.List;

@Service
public class FinCdrlInfoServiceImpl implements FinCrdlInfoService {

    @Autowired
    private FinCrdlInfoMapper finCrdlInfoMapper;

    @Autowired
    private FinAcctgRulMapper finAcctgRulMapper;

    @Autowired
    private FinAuxAcctgMapper finAuxAcctgMapper;

    //获取凭证信息
    @Override
    public List<CredentialInformation> getCrdlInfo(CredentialInformation credentialInformation) throws Exception {
        return finCrdlInfoMapper.select(credentialInformation);
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

        //辅助核算项目
        for (AccountingRule item : accountingRuleList) {

            if (existCheckAcc(item.getAuxacctg_id())) {

                item.preUpdate(tokenModel);
                finAcctgRulMapper.delAuxAcctgByFid(item.getModifyby(), item.getAcctgrul_fid());
            } else {
                return false;
            }
        }

        //分录
        if (existCheckAux(accountingRuleList.get(0).getAcctgrul_id())) {

            accountingRuleList.get(0).preUpdate(tokenModel);
            finAcctgRulMapper.delAcctgRulByFid(accountingRuleList.get(0).getModifyby(), accountingRuleList.get(0).getCrdlinfo_fid());
        } else {
            return false;
        }

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
        if(accountingRuleList!= null){

            for (AccountingRule item: accountingRuleList) {

                //分录insert
                AcctgRul acctgRul = new AcctgRul();
                acctgRul.setAcctgrul_id(UUID.randomUUID().toString());
                acctgRul.setCrdlinfo_fid(crdlId);
                acctgRul.setRemarks(item.getRemarks());
                acctgRul.setDebit(item.getDebit());
                acctgRul.setCredit(item.getCredit());
                acctgRul.setTaxrate(item.getTaxrate());
                acctgRul.setAmount(item.getAmount());
                acctgRul.preInsert(tokenModel);
                finAcctgRulMapper.insertSelective(acctgRul);

                //辅助核算项目insert
                AuxAcctg auxAcctg = new AuxAcctg();
                auxAcctg.setAuxacctg_id(UUID.randomUUID().toString());
                auxAcctg.setAcctgrul_fid(acctgRul.getAcctgrul_id());
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
