package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS1000.mapper.QuotationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractMapper;
import com.nt.service_pfans.PFANS1000.mapper.NonJudgmentMapper;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ContractapplicationServiceImpl implements ContractapplicationService {

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;
    @Autowired
    private QuotationMapper quotationMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private NonJudgmentMapper nonJudgmentMapper;
    @Autowired
    private AwardMapper AwardMapper;
    @Autowired
    private NapalmMapper napalmMapper;
    @Autowired
    private PetitionMapper PetitionMapper;

    @Override
    public ContractapplicationVo get(Contractapplication contractapplication ) {
        ContractapplicationVo vo = new ContractapplicationVo();
        //契约番号申请
        List<Contractapplication> coList = contractapplicationMapper.select(contractapplication);
        vo.setContractapplication(coList);
        //契约番号回数
        Contractnumbercount number = new Contractnumbercount();
        number.setContractnumber(contractapplication.getContractnumber());
        List<Contractnumbercount> numberList = contractnumbercountMapper.select(number);
        vo.setContractnumbercount(numberList);
        return vo;
    }

    @Override
    public void update(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception {
        //契约番号申请
        List<Contractapplication> cnList = contractapplication.getContractapplication();
        if (cnList != null) {
            for (Contractapplication citation : cnList) {
                if(!StringUtils.isNullOrEmpty(citation.getContractapplication_id())){
                    citation.preUpdate(tokenModel);
                    contractapplicationMapper.updateByPrimaryKeySelective(citation);
                }
                else{
                    citation.preInsert(tokenModel);
                    citation.setContractapplication_id(UUID.randomUUID().toString());
                    contractapplicationMapper.insert(citation);
                }
            }
        }
        //契约番号回数
        List<Contractnumbercount> numberList = contractapplication.getContractnumbercount();
        if (cnList != null) {
            int rowindex = 0;
            for (Contractnumbercount number : numberList) {
                rowindex = rowindex + 1;
                number.setRowindex(String.valueOf(rowindex));
                if(!StringUtils.isNullOrEmpty(number.getContractnumbercount_id())){
                    number.preUpdate(tokenModel);
                    contractnumbercountMapper.updateByPrimaryKeySelective(number);
                }
                else{
                    number.preInsert(tokenModel);
                    number.setContractnumbercount_id(UUID.randomUUID().toString());
                    contractnumbercountMapper.insert(number);
                }
            }
        }
    }

    @Override
    public String insertBook(Contractapplication contractapplication, TokenModel tokenModel) throws Exception {
        String contractnumber = contractapplication.getContractnumber();
        String rowindex = contractapplication.getRowindex();
        Contractapplication co = new Contractapplication();
        co.setContractnumber(contractnumber);
        List<Contractapplication> coList = contractapplicationMapper.select(co);
        //根据契约书番号，查找纳品回数
        Contractnumbercount contractnumbercount = new Contractnumbercount();
        contractnumbercount.setContractnumber(contractnumber);
        List<Contractnumbercount> countList = contractnumbercountMapper.select(contractnumbercount);
        if (coList != null) {
            for (Contractapplication contractapp : coList) {
                //見積書作成
                if(rowindex.equals("1")){
                    Quotation quotation = new Quotation();
                    quotation.preInsert(tokenModel);
                    quotation.setQuotationid(UUID.randomUUID().toString());
                    quotation.setContractnumber(contractnumber);

                    //6
                    quotation.setContracttype(contractapp.getContracttype());
                    quotation.setContractnumber(contractapp.getContractnumber());
                    quotation.setTrusteejapanese(contractapp.getCustojapanese());
                    quotation.setTrusteechinese(contractapp.getCustochinese());
                    quotation.setEntrustedjapanese(contractapp.getPlacejapanese());
                    quotation.setEntrustedchinese(contractapp.getPlacechinese());
                    quotation.setDeployment(contractapp.getDeployment());
                    quotation.setPjchinese(contractapp.getConchinese());
                    quotation.setPjjapanese(contractapp.getConjapanese());
                    quotation.setCurrencyposition(contractapp.getCurrencyposition());
                    quotation.setClaimamount(contractapp.getClaimamount());
                    quotation.setLoadingjudge(contractapp.getLoadingjudge());

                    if (org.springframework.util.StringUtils.hasLength(contractapp.getClaimdatetime())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String[] startAndEnd = contractapp.getClaimdatetime().split(" ~ ");
                        try {
                            quotation.setStartdate(sdf.parse(startAndEnd[0]));
                        } catch (Exception e) {}
                        try {
                            quotation.setEnddate(sdf.parse(startAndEnd[1]));
                        } catch (Exception e) {}
                    }

                    quotationMapper.insert(quotation);
                }
                //該非判定書作成
                else if(rowindex.equals("2")){
                    NonJudgment nonJudgment = new NonJudgment();
                    nonJudgment.preInsert(tokenModel);
                    nonJudgment.setNonjudgment_id(UUID.randomUUID().toString());
                    nonJudgment.setContractnumber(contractnumber);

                    //8
                    nonJudgment.setDecisionnumber(contractapp.getDecisionnumber());
                    nonJudgment.setDeployment(contractapp.getDeployment());
                    nonJudgment.setContractnumber(contractapp.getContractnumber());
                    nonJudgment.setCustoenglish(contractapp.getCustoenglish());
                    nonJudgment.setVarto(contractapp.getVarto());
                    nonJudgment.setClaimdatetime(contractapp.getClaimdatetime());
                    nonJudgment.setOutnumber(contractapp.getOutnumber());
                    nonJudgment.setProductnumber(contractapp.getProductnumber());


                    nonJudgmentMapper.insert(nonJudgment);
                }
                //契約書作成
                else if(rowindex.equals("3")){
                    Contract contract = new Contract();
                    contract.preInsert(tokenModel);
                    contract.setContract_id(UUID.randomUUID().toString());
                    contract.setContractnumber(contractnumber);

                    //4
                    contract.setContracttype(contractapp.getContracttype());
                    contract.setDepositjapanese(contractapp.getCustojapanese());
                    contract.setDepositchinese(contractapp.getCustochinese());
                    contract.setPrplacejapanese(contractapp.getPlacejapanese());
                    contract.setPrplacechinese(contractapp.getPlacechinese());
                    contract.setDeployment(contractapp.getDeployment());
                    contract.setPjnamechinese(contractapp.getConchinese());
                    contract.setPjnamejapanese(contractapp.getConjapanese());
                    contract.setCurrencyposition(contractapp.getCurrencyposition());
                    contract.setClaimamount(contractapp.getClaimamount());
                    if (org.springframework.util.StringUtils.hasLength(contractapp.getClaimdatetime())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String[] startAndEnd = contractapp.getClaimdatetime().split(" ~ ");
                        try {
                            contract.setOpeningdate(sdf.parse(startAndEnd[0]));
                        } catch (Exception e) {}
                        try {
                            contract.setEnddate(sdf.parse(startAndEnd[1]));
                        } catch (Exception e) {}
                    }


                    contractMapper.insert(contract);
                }
                //決裁書作成(受託)
                else if(rowindex.equals("4")){
                    Award award = new Award();
                    award.preInsert(tokenModel);
                    award.setAward_id(UUID.randomUUID().toString());
                    award.setContractnumber(contractnumber);

                    //13
                    award.setContracttype(contractapp.getContracttype());
                    award.setCustojapanese(contractapp.getCustojapanese());
                    award.setCustochinese(contractapp.getCustochinese());
                    award.setPlacejapanese(contractapp.getPlacejapanese());
                    award.setPlacechinese(contractapp.getPlacechinese());
                    award.setDeployment(contractapp.getDeployment());
                    award.setPjnamechinese(contractapp.getConchinese());
                    award.setPjnamejapanese(contractapp.getConjapanese());
                    award.setClaimdatetime(contractapp.getClaimdatetime());
                    award.setDeliverydate(contractapp.getDeliverydate());
                    award.setCurrencyposition(contractapp.getCurrencyposition());
                    award.setClaimamount(contractapp.getClaimamount());
                    award.setUser_id(contractapp.getUser_id());
                    award.setRemarks(contractapp.getRemarks());
                    award.setMaketype(rowindex);

                    AwardMapper.insert(award);
                }
                //納品書作成
                else if(rowindex.equals("5")){
                    for (Contractnumbercount number : countList) {
                        Napalm napalm = new Napalm();
                        napalm.preInsert(tokenModel);
                        napalm.setNapalm_id(UUID.randomUUID().toString());
                        napalm.setContractnumber(contractnumber);

                        //7
                        napalm.setDepositjapanese(contractapp.getCustojapanese());
                        napalm.setDepositenglish(contractapp.getCustoenglish());
                        napalm.setEntrustment(contractapp.getCustoabbreviation());
                        napalm.setDeployment(contractapp.getDeployment());
                        napalm.setPjnamechinese(contractapp.getConchinese());
                        napalm.setPjnamejapanese(contractapp.getConjapanese());
                        napalm.setClaimtype(contractapp.getClaimtype());
                        napalm.setDeliveryfinshdate(contractapp.getDeliveryfinshdate());
                        napalm.setDeliverydate(number.getDeliverydate());//納品予定日
                        napalm.setCompletiondate(number.getCompletiondate());//検収完了日
                        napalm.setClaimamount(number.getClaimamount());//請求金額
                        napalm.setClaimnumber(number.getClaimnumber());//請求番号
                        napalm.setLoadingjudge(contractapp.getLoadingjudge());
                        napalm.setCurrencyformat(contractapp.getCurrencyposition());
                        napalm.setContracttype(contractapp.getContracttype());
                        napalm.setToto(contractapp.getVarto());

                        if (org.springframework.util.StringUtils.hasLength(contractapp.getClaimdatetime())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String[] startAndEnd = contractapp.getClaimdatetime().split(" ~ ");
                            try {
                                napalm.setOpeningdate(sdf.parse(startAndEnd[0]));
                            } catch (Exception e) {}
                            try {
                                napalm.setEnddate(sdf.parse(startAndEnd[1]));
                            } catch (Exception e) {}
                        }

                        napalmMapper.insert(napalm);
                    }
                }
                //請求書作成
                else if(rowindex.equals("6")){
                    for (Contractnumbercount number : countList) {
                        Petition petition = new Petition();
                        petition.preInsert(tokenModel);
                        petition.setPetition_id(UUID.randomUUID().toString());
                        petition.setContractnumber(contractnumber);

                        //12
                        petition.setContracttype(contractapp.getContracttype());
                        petition.setCustoenglish(contractapp.getCustoenglish());
                        petition.setCustochinese(contractapp.getCustochinese());
                        petition.setResponerglish(contractapp.getResponerglish());
                        petition.setPlaceenglish(contractapp.getPlaceenglish());
                        petition.setPlacechinese(contractapp.getPlacechinese());
                        petition.setClaimdatetime(contractapp.getClaimdatetime());
                        petition.setBusinesscode(contractapp.getBusinesscode());
                        petition.setDeliveryfinshdate(contractapp.getDeliveryfinshdate());
                        petition.setResponphone(contractapp.getResponphone());
                        petition.setClaimtype(contractapp.getClaimtype());
                        petition.setCurrencyposition(contractapp.getCurrencyposition());
                        petition.setPjnamechinese(contractapp.getConchinese());
                        petition.setPjnamejapanese(contractapp.getConjapanese());
                        petition.setClaimamount(number.getClaimamount());//請求金額
                        petition.setClaimnumber(number.getClaimnumber());//請求番号
                        PetitionMapper.insert(petition);
                    }
                }
                //決裁書作成(委託)
                else if(rowindex.equals("7")){
                    Award award = new Award();
                    award.preInsert(tokenModel);
                    award.setAward_id(UUID.randomUUID().toString());
                    award.setContractnumber(contractnumber);
                    //13
                    award.setContracttype(contractapp.getContracttype());
                    award.setCustojapanese(contractapp.getCustojapanese());
                    award.setCustochinese(contractapp.getCustochinese());
                    award.setPlacejapanese(contractapp.getPlacejapanese());
                    award.setPlacechinese(contractapp.getPlacechinese());
                    award.setDeployment(contractapp.getDeployment());
                    award.setPjnamechinese(contractapp.getConchinese());
                    award.setPjnamejapanese(contractapp.getConjapanese());
                    award.setClaimdatetime(contractapp.getClaimdatetime());
                    award.setDeliverydate(contractapp.getDeliverydate());
                    award.setCurrencyposition(contractapp.getCurrencyposition());
                    award.setClaimamount(contractapp.getClaimamount());
                    award.setUser_id(contractapp.getUser_id());
                    award.setRemarks(contractapp.getRemarks());
                    award.setMaketype(rowindex);

                    AwardMapper.insert(award);
                }
                contractapp.preUpdate(tokenModel);
                contractapplicationMapper.updateByPrimaryKeySelective(contractapp);
            }
        }
        return "1";
    }

    @Override
    public void insert(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception {
        //契约番号申请
        List<Contractapplication> cnList = contractapplication.getContractapplication();
        if (cnList != null) {
            for (Contractapplication citation : cnList) {
                if(!StringUtils.isNullOrEmpty(citation.getContractapplication_id())){
                    citation.preUpdate(tokenModel);
                    contractapplicationMapper.updateByPrimaryKeySelective(citation);
                }
                else{
                    citation.preInsert(tokenModel);
                    citation.setContractapplication_id(UUID.randomUUID().toString());
                    contractapplicationMapper.insert(citation);
                }
            }
        }
        //契约番号回数
        List<Contractnumbercount> numberList = contractapplication.getContractnumbercount();
        if (cnList != null) {
            int rowindex = 0;
            for (Contractnumbercount number : numberList) {
                rowindex = rowindex + 1;
                number.setRowindex(String.valueOf(rowindex));
                if(!StringUtils.isNullOrEmpty(number.getContractnumbercount_id())){
                    number.preUpdate(tokenModel);
                    contractnumbercountMapper.updateByPrimaryKeySelective(number);
                }
                else{
                    number.preInsert(tokenModel);
                    number.setContractnumbercount_id(UUID.randomUUID().toString());
                    contractnumbercountMapper.insert(number);
                }
            }
        }
    }

    @Override
    public ExistVo existCheck(String contractNumber) throws Exception{
        ExistVo existVo = new ExistVo();
        existVo = contractapplicationMapper.existCheck(contractNumber);
        return existVo;
    }

}
