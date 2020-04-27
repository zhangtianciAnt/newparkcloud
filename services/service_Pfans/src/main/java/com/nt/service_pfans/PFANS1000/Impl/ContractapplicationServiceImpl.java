package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractcompoundMapper;
import com.nt.service_pfans.PFANS1000.mapper.QuotationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractMapper;
import com.nt.service_pfans.PFANS1000.mapper.NonJudgmentMapper;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractapplicationServiceImpl implements ContractapplicationService {

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;
    @Autowired
    private ContractcompoundMapper contractcompoundMapper;
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
    @Autowired
    private TokenService tokenService;

    @Override
    public ContractapplicationVo get(Contractapplication contractapplication) {
        ContractapplicationVo vo = new ContractapplicationVo();
        //契约番号申请
        List<Contractapplication> coList = contractapplicationMapper.select(contractapplication);
        vo.setContractapplication(coList);
        //契约番号回数
        Contractnumbercount number = new Contractnumbercount();
        number.setContractnumber(contractapplication.getContractnumber());
        List<Contractnumbercount> numberList = contractnumbercountMapper.select(number);
        if (numberList != null && numberList.size() > 1) {
            numberList = numberList.stream().sorted(Comparator.comparing(Contractnumbercount::getRowindex)).collect(Collectors.toList());
        }
        vo.setContractnumbercount(numberList);
        //契约番号回数
        Contractcompound compound = new Contractcompound();
        compound.setContractnumber(contractapplication.getContractnumber());
        List<Contractcompound> compoundList = contractcompoundMapper.select(compound);
        if (compoundList != null && compoundList.size() > 1) {
            compoundList = compoundList.stream().sorted(Comparator.comparing(Contractcompound::getRowindex)).collect(Collectors.toList());
        }
        vo.setContractcompound(compoundList);
        return vo;
    }

    @Override
    public List<ContractapplicationVo> getList(List<Contractapplication> contractapplicationlist) {
        List<ContractapplicationVo> listvo = new ArrayList<ContractapplicationVo>();
        for (Contractapplication listcontract : contractapplicationlist) {
            ContractapplicationVo vo = new ContractapplicationVo();
            Contractapplication contractapplication = new Contractapplication();
            contractapplication.setContractnumber(listcontract.getContractnumber());
            //契约番号申请
            List<Contractapplication> coList = contractapplicationMapper.select(contractapplication);
            vo.setContractapplication(coList);
            //契约番号回数
            Contractnumbercount number = new Contractnumbercount();
            number.setContractnumber(contractapplication.getContractnumber());
            List<Contractnumbercount> numberList = contractnumbercountMapper.select(number);
            if (numberList != null && numberList.size() > 1) {
                numberList = numberList.stream().sorted(Comparator.comparing(Contractnumbercount::getRowindex)).collect(Collectors.toList());
            }
            vo.setContractnumbercount(numberList);
            //契约番号回数
            Contractcompound compound = new Contractcompound();
            compound.setContractnumber(contractapplication.getContractnumber());
            List<Contractcompound> compoundList = contractcompoundMapper.select(compound);
            if (compoundList != null && compoundList.size() > 1) {
                compoundList = compoundList.stream().sorted(Comparator.comparing(Contractcompound::getRowindex)).collect(Collectors.toList());
            }
            vo.setContractcompound(compoundList);
            listvo.add(vo);
        }
        return listvo;
    }

    @Override
    public void update(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception {
        //契约番号申请
        List<Contractapplication> cnList = contractapplication.getContractapplication();
        if (cnList != null) {
            for (Contractapplication citation : cnList) {
                if (!StringUtils.isNullOrEmpty(citation.getContractapplication_id())) {
                    citation.preUpdate(tokenModel);
                    contractapplicationMapper.updateByPrimaryKeySelective(citation);
                } else {
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
                number.setRowindex(rowindex);
                if (!StringUtils.isNullOrEmpty(number.getContractnumbercount_id())) {
                    number.preUpdate(tokenModel);
                    contractnumbercountMapper.updateByPrimaryKeySelective(number);
                } else {
                    number.preInsert(tokenModel);
                    number.setContractnumbercount_id(UUID.randomUUID().toString());
                    contractnumbercountMapper.insert(number);
                }
            }
        }
        //复合合同金额分配
        List<Contractcompound> compoundList = contractapplication.getContractcompound();
        if (cnList != null) {
            int rowindex = 0;
            if (compoundList != null) {
                Contractcompound Co = new Contractcompound();
                Co.setContractnumber(cnList.get(cnList.size() - 1).getContractnumber());
                contractcompoundMapper.delete(Co);
                for (Contractcompound compound : compoundList) {
                    rowindex = rowindex + 1;
                    compound.setRowindex(rowindex);
                    compound.preInsert(tokenModel);
                    compound.setContractnumber(cnList.get(cnList.size() - 1).getContractnumber());
                    compound.setContractcompound_id(UUID.randomUUID().toString());
                    contractcompoundMapper.insert(compound);
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
                if (rowindex.equals("1")) {
                    //add-ws-获取出荷判定実施者
                    String Loadingjudge = "";
                    for (Contractnumbercount number : countList) {
                        Loadingjudge = number.getLoadingjudge();
                        break;
                    }
                    //add-ws-获取出荷判定実施者
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
                    quotation.setLoadingjudge(Loadingjudge);

                    if (org.springframework.util.StringUtils.hasLength(contractapp.getClaimdatetime())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String[] startAndEnd = contractapp.getClaimdatetime().split(" ~ ");
                        quotation.setStartdate(sdf.parse(startAndEnd[0]));
                        quotation.setEnddate(sdf.parse(startAndEnd[1]));
                    }

                    Quotation quotation2 = new Quotation();
                    quotation2.setContractnumber(contractapp.getContractnumber());
//                        quotation2.setOwner(tokenModel.getUserId());
                    quotationMapper.delete(quotation2);
                    quotationMapper.insert(quotation);

                }
                //該非判定書作成
                else if (rowindex.equals("2")) {
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
                    nonJudgment.setJaname(contractapp.getConchinese());
                    nonJudgment.setCareer(contractapp.getBusinesscode());
                    nonJudgment.setProductnumber(contractapp.getProductnumber());

                    NonJudgment nonJudgment2 = new NonJudgment();
                    nonJudgment2.setContractnumber(contractapp.getContractnumber());
//                    nonJudgment2.setOwner(tokenModel.getUserId());
                    nonJudgmentMapper.delete(nonJudgment2);


                    nonJudgmentMapper.insert(nonJudgment);
                }
                //契約書作成
                else if (rowindex.equals("3")) {
                    int numberSum = 0;
                    for (Contractnumbercount number : countList) {
                        numberSum += Integer.valueOf(number.getClaimamount());
                    }
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
                    contract.setClaimamount(String.valueOf(numberSum));
                    contract.setConjapanese(contractapp.getConjapanese());//契約概要（/開発タイトル）和文
                    if (org.springframework.util.StringUtils.hasLength(contractapp.getClaimdatetime())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String[] startAndEnd = contractapp.getClaimdatetime().split(" ~ ");
                        contract.setOpeningdate(sdf.parse(startAndEnd[0]));
                        contract.setEnddate(sdf.parse(startAndEnd[1]));
                    }

                    Contract contract2 = new Contract();
                    contract2.setContractnumber(contractnumber);
//                    contract2.setOwner(tokenModel.getUserId());
                    contractMapper.delete(contract2);

                    contractMapper.insert(contract);
                }
                //決裁書作成(受託)
                else if (rowindex.equals("4")) {
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
                    award.setGroup_id(contractapp.getGroup_id());
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
                    award.setConjapanese(contractapp.getConjapanese());//契約概要（/開発タイトル）和文

                    Award award2 = new Award();
                    award2.setContractnumber(contractnumber);
//                    award2.setOwner(tokenModel.getUserId());
                    AwardMapper.delete(award2);

                    AwardMapper.insert(award);
                }
                //納品書作成
                else if (rowindex.equals("5")) {
                    Napalm napalm2 = new Napalm();
                    napalm2.setContractnumber(contractnumber);
//                    napalm2.setOwner(tokenModel.getUserId());
                    napalmMapper.delete(napalm2);

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
                        //del_fjl
//                        napalm.setDeliveryfinshdate(number.getDeliverydate());
                        //del_fjl
                        napalm.setDeliverydate(number.getDeliverydate());//納品予定日
                        //add-ws-添加纳品做成日和出荷判定实施者
                        napalm.setDeliveryfinshdate(number.getDeliveryfinshdate());
                        napalm.setLoadingjudge(number.getLoadingjudge());
                        //add-ws-添加纳品做成日和出荷判定实施者
                        //add_fjl
                        napalm.setClaimdate(number.getClaimdate()); //请求日
                        //add_fjl
                        napalm.setCompletiondate(number.getCompletiondate());//検収完了日
                        napalm.setClaimamount(number.getClaimamount());//請求金額
                        napalm.setClaimnumber(number.getClaimnumber());//請求番号
                        napalm.setCurrencyformat(contractapp.getCurrencyposition());
                        napalm.setContracttype(contractapp.getContracttype());
                        napalm.setToto(contractapp.getVarto());
                        napalm.setConjapanese(contractapp.getConjapanese());//契約概要（/開発タイトル）和文

                        if (org.springframework.util.StringUtils.hasLength(contractapp.getClaimdatetime())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String[] startAndEnd = contractapp.getClaimdatetime().split(" ~ ");
                            napalm.setOpeningdate(sdf.parse(startAndEnd[0]));
                            napalm.setEnddate(sdf.parse(startAndEnd[1]));
                        }

                        napalmMapper.insert(napalm);
                        //更新纳品进步状况=纳品完了
                        contractapp.preUpdate(tokenModel);
                        contractapp.setDeliverycondition("HT009001");
                        contractapplicationMapper.updateByPrimaryKeySelective(contractapp);
                    }
                }
                //請求書作成
                else if (rowindex.equals("6")) {
                    Petition petition2 = new Petition();
                    petition2.setContractnumber(contractnumber);
//                    petition2.setOwner(tokenModel.getUserId());
                    PetitionMapper.delete(petition2);

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
                        //add-ws-添加纳品做成日
                        petition.setDeliveryfinshdate(number.getDeliveryfinshdate());
                        //add-ws-添加纳品做成日
                        petition.setResponphone(contractapp.getResponphone());
                        petition.setClaimtype(contractapp.getClaimtype());
                        petition.setCurrencyposition(contractapp.getCurrencyposition());
                        petition.setPjnamechinese(contractapp.getConchinese());
                        petition.setPjnamejapanese(contractapp.getConjapanese());
                        petition.setClaimamount(number.getClaimamount());//請求金額
                        petition.setClaimdate(number.getClaimdate());//請求日
                        petition.setClaimnumber(number.getClaimnumber());//請求番号
                        petition.setRemarks(contractapp.getQingremarks());//备注
                        petition.setConjapanese(contractapp.getConjapanese());//契約概要（/開発タイトル）和文

                        PetitionMapper.insert(petition);
                        //更新请求进步状况=請求完了
                        contractapp.preUpdate(tokenModel);
                        contractapp.setClaimcondition("HT011001");
                        contractapplicationMapper.updateByPrimaryKeySelective(contractapp);
                    }
                }
                //決裁書作成(委託)
                else if (rowindex.equals("7")) {
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
                    award.setClaimdatetime(contractapp.getContractdate());
                    award.setDeliverydate(contractapp.getDeliverydate());
                    award.setCurrencyposition(contractapp.getCurrencyposition());
                    award.setClaimamount(contractapp.getClaimamount());
                    award.setUser_id(contractapp.getUser_id());
                    award.setRemarks(contractapp.getRemarks());
                    award.setMaketype(rowindex);
                    award.setConjapanese(contractapp.getConjapanese());//契約概要（/開発タイトル）和文

                    Award award2 = new Award();
                    award2.setContractnumber(contractnumber);
//                    award2.setOwner(tokenModel.getUserId());
                    AwardMapper.delete(award2);

                    AwardMapper.insert(award);
                }
                contractapp.preUpdate(tokenModel);
                contractapplicationMapper.updateByPrimaryKeySelective(contractapp);
            }
        }
        return "1";
    }

    @Override
    public Map<String, Object> insert(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String newcontractnumber = "";
        //契约番号申请
        List<Contractapplication> cnList = contractapplication.getContractapplication();
        if (cnList != null) {
            for (Contractapplication citation : cnList) {
                if (!StringUtils.isNullOrEmpty(citation.getContractapplication_id())) {
                    citation.preUpdate(tokenModel);
                    contractapplicationMapper.updateByPrimaryKeySelective(citation);
                } else {
                    citation.preInsert(tokenModel);
                    citation.setContractapplication_id(UUID.randomUUID().toString());
                    String contractnumber = citation.getContractnumber();
                    String[] str = contractnumber.split("-");
                    if (str.length == 1) {
                        //年、契約种类、部门、契约类型、
                        Contractapplication co = new Contractapplication();
                        co.setCareeryear(citation.getCareeryear());
                        co.setContracttype(citation.getContracttype());
                        co.setGroup_id(citation.getGroup_id());
                        co.setType(citation.getType());
                        if (citation.getType().equals("0")) {
                            //取引先会社
                            co.setCustojapanese(citation.getCustojapanese());
                            List<Contractapplication> coList = contractapplicationMapper.select(co);
                            coList = coList.stream().filter(coi -> (!coi.getContractnumber().contains("覚"))).collect(Collectors.toList());
                            String number = "01";
                            String coListcount = String.valueOf(coList.size() + 1);
                            if (coListcount.length() == 1) {
                                number = "0" + coListcount;
                            } else if (coListcount.length() == 2) {
                                number = coListcount;
                            }
                            newcontractnumber = contractnumber + number;
                        } else if (citation.getType().equals("1")) {
                            List<Contractapplication> coList = contractapplicationMapper.select(co);
                            coList = coList.stream().filter(coi -> (!coi.getContractnumber().contains("覚"))).collect(Collectors.toList());
                            String number = "0001";
                            String coListcount = String.valueOf(coList.size() + 1);
                            if (coListcount.length() == 1) {
                                number = "000" + coListcount;
                            } else if (coListcount.length() == 2) {
                                number = "00" + coListcount;
                            } else if (coListcount.length() == 3) {
                                number = "0" + coListcount;
                            }
                            newcontractnumber = contractnumber + number;
                        } else {
                            //co.setCustojapanese(citation.getCustojapanese());
                            List<Contractapplication> coList = contractapplicationMapper.select(co);
                            coList = coList.stream().filter(coi -> (!coi.getContractnumber().contains("覚"))).collect(Collectors.toList());
                            String number = "01";
                            String coListcount = String.valueOf(coList.size() + 1);
                            if (coListcount.length() == 1) {
                                number = "0" + coListcount;
                            } else if (coListcount.length() == 2) {
                                number = coListcount;
                            }
                            newcontractnumber = contractnumber + number;
                        }
                        citation.setContractnumber(newcontractnumber);
                    }
                    contractapplicationMapper.insert(citation);
                }
            }
        }
        result.put("contractapplication", cnList);

        //契约番号回数
        List<Contractnumbercount> numberList = contractapplication.getContractnumbercount();
        if (cnList != null) {
            int rowindex = 0;
            for (Contractnumbercount number : numberList) {
                rowindex = rowindex + 1;
                number.setRowindex(rowindex);
                if (!StringUtils.isNullOrEmpty(number.getContractnumbercount_id())) {
                    number.preUpdate(tokenModel);
                    number.setContractnumber(cnList.get(0).getContractnumber());
                    contractnumbercountMapper.updateByPrimaryKeySelective(number);
                } else {
                    number.preInsert(tokenModel);
                    number.setContractnumber(cnList.get(0).getContractnumber());
                    number.setContractnumbercount_id(UUID.randomUUID().toString());
                    contractnumbercountMapper.insert(number);
                }
            }
        }
        //复合合同金额分配
        List<Contractcompound> compoundList = contractapplication.getContractcompound();
        if (cnList != null) {
            int rowindex = 0;
            if (compoundList != null) {
                Contractcompound Co = new Contractcompound();
                Co.setContractnumber(cnList.get(0).getContractnumber());
                contractcompoundMapper.delete(Co);
                for (Contractcompound compound : compoundList) {
                    rowindex = rowindex + 1;
                    compound.setRowindex(rowindex);
                    compound.preInsert(tokenModel);
                    compound.setContractnumber(cnList.get(0).getContractnumber());
                    compound.setContractcompound_id(UUID.randomUUID().toString());
                    contractcompoundMapper.insert(compound);
                }
            }
        }
        result.put("contractnumbercount", numberList);
        return result;
    }

    @Override
    public ExistVo existCheck(String contractNumber) throws Exception {
        ExistVo existVo = new ExistVo();
        existVo = contractapplicationMapper.existCheck(contractNumber);
        return existVo;
    }

}
