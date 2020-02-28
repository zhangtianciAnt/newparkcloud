package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Quotation;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.QuotationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractMapper;
import com.nt.service_pfans.PFANS1000.mapper.NonJudgmentMapper;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ContractapplicationServiceImpl implements ContractapplicationService {

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
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
    public List<Contractapplication> get(Contractapplication contractapplication ) {
        return contractapplicationMapper.select(contractapplication);
    }

    @Override
    public void update(List<Contractapplication> contractapplication, TokenModel tokenModel) throws Exception {
        for(int i = 0; i < contractapplication.size(); i ++){
            Contractapplication co = contractapplication.get(i);
            if(co.getContractapplication_id().equals("")){
                co.preInsert(tokenModel);
                co.setContractapplication_id(UUID.randomUUID().toString());
                contractapplicationMapper.insert(co);
            }
            else{
                co.preUpdate(tokenModel);
                contractapplicationMapper.updateByPrimaryKeySelective(co);
            }
        }
    }

    @Override
    public void insert(List<Contractapplication> contractapplication, TokenModel tokenModel) throws Exception {
        for(int i = 0; i < contractapplication.size(); i ++){
            Contractapplication co = contractapplication.get(i);
            co.preInsert(tokenModel);
            co.setContractapplication_id(UUID.randomUUID().toString());
            contractapplicationMapper.insert(co);
        }
    }

    @Override
    public void insertBook(Contractapplication contractapplication, TokenModel tokenModel) throws Exception {
        String contractnumber = contractapplication.getContractnumber();
        String rowindex = contractapplication.getRowindex();
        Contractapplication co = new Contractapplication();
        co.setContractnumber(contractnumber);
        List<Contractapplication> coList = contractapplicationMapper.select(co);
        if (coList != null) {
            for (Contractapplication contractapp : coList) {
                //見積書作成
                if(rowindex.equals("1")){
                    Quotation quotation = new Quotation();
                    quotation.preInsert(tokenModel);
                    quotation.setQuotationid(UUID.randomUUID().toString());
                    quotation.setContractnumber(contractnumber);
                    quotationMapper.insert(quotation);
                }
                //該非判定書作成
                else if(rowindex.equals("2")){
                    NonJudgment nonJudgment = new NonJudgment();
                    nonJudgment.preInsert(tokenModel);
                    nonJudgment.setContract_id(UUID.randomUUID().toString());
                    nonJudgment.setContractnumber(contractnumber);
                    nonJudgmentMapper.insert(nonJudgment);
                }
                //契約書作成
                else if(rowindex.equals("3")){
                    Contract contract = new Contract();
                    contract.preInsert(tokenModel);
                    contract.setContract_id(UUID.randomUUID().toString());
                    contract.setContractnumber(contractnumber);
                    contractMapper.insert(contract);
                }
                //決裁書作成
                else if(rowindex.equals("4")){
                    Award award = new Award();
                    award.preInsert(tokenModel);
                    award.setAward_id(UUID.randomUUID().toString());
                    award.setContractnumber(contractnumber);
                    AwardMapper.insert(award);
                }
                //納品書作成
                else if(rowindex.equals("5")){
                    Napalm napalm = new Napalm();
                    napalm.preInsert(tokenModel);
                    napalm.setNapalm_id(UUID.randomUUID().toString());
                    napalm.setContractnumber(contractnumber);
                    napalmMapper.insert(napalm);
                }
                //請求書作成
                else if(rowindex.equals("6")){
                    Petition petition = new Petition();
                    petition.preInsert(tokenModel);
                    petition.setPetition_id(UUID.randomUUID().toString());
                    petition.setContractnumber(contractnumber);
                    petition.setContracttype(contract.getContracttype());
                    petition.setDepositenglish("");
                    petition.setDepositchinese("");
                    petition.setDereenglish("");
                    petition.setPrplaceenglish("");
                    petition.setPrplacechinese("");
                    petition.setPjnamejapanese("");
                    petition.setPjnamechinese("");
                    petition.setDevelopdate("");
                    petition.setBusinesscode("");
//                    petition.setD1eliverydate(new Date());
                    petition.setClaimoney("");
                    petition.setDepositphone("");
                    petition.setClaimnumber("");
                    petition.setClaimtype("");
                    petition.setCurrencyformat("");
                    PetitionMapper.insert(petition);
                }
                contractapp.preUpdate(tokenModel);
                contractapplicationMapper.updateByPrimaryKeySelective(contractapp);
            }
        }
    }
}
