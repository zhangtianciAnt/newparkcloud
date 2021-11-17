package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.dao_Pfans.PFANS6000.EntrustSupport;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS6000.mapper.EntrustSupportMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AwardServiceImpl implements AwardService {

    @Autowired
    private PolicyContractMapper policycontractmapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private AwardReuniteMapper awardReuniteMapper;

    @Autowired
    private AwardDetailMapper awardDetailMapper;

    @Autowired
    private StaffDetailMapper staffDetailMapper;

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    PeoplewareFeeMapper peoplewareFeeMapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private PersonalCostService personalCostService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ContractcompoundMapper contractcompoundMapper;
    @Autowired
    private BusinessplanService businessplanService;
    @Autowired
    private RulingMapper rulingMapper;
    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private OrgTreeService orgtreeService;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Autowired
    private EntrustSupportMapper entrustSupportMapper;

    @Override
    public List<Award> get(Award award) throws Exception {
        List<Award> awardlist = awardMapper.select(award);
        if (awardlist.size() > 0) {
            awardlist = awardlist.stream().sorted(Comparator.comparing(Award::getCreateon).reversed()).collect(Collectors.toList());
        }
        return awardlist;
    }

    // 禅道任务152
    @Override
    public List<Award> One(Award award) throws Exception {
        return awardMapper.select(award);
    }

    // 禅道任务152
    @Override
    public AwardVo selectById(String award_id) throws Exception {
        //region scc add 8/24 id--部门键值对 from
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> companyid = new HashMap<>();
        for(DepartmentVo vo : allDepartment){
            companyid.put(vo.getDepartmentId(),vo.getDepartmentEn());
        }
        //endregion scc add 8/24 id--部门键值对 to
        AwardVo awavo = new AwardVo();
        AwardDetail awadetail = new AwardDetail();
        awadetail.setAward_id(award_id);
        StaffDetail staffdetail = new StaffDetail();
        staffdetail.setAward_id(award_id);
        List<AwardDetail> awalist = awardDetailMapper.select(awadetail);
        List<StaffDetail> stafflist = staffDetailMapper.select(staffdetail);
        //region scc add 对应合同号 from
        Award compoundContractNo = awardMapper.selectByPrimaryKey(award_id);
        //endregion scc add 对应合同号 to
        awalist = awalist.stream().sorted(Comparator.comparing(AwardDetail::getRowindex)).collect(Collectors.toList());
        stafflist = stafflist.stream().sorted(Comparator.comparing(StaffDetail::getRowindex)).collect(Collectors.toList());
        Award awa = awardMapper.selectByPrimaryKey(award_id);
        Award award = awardMapper.selectByPrimaryKey(award_id);
        //region scc add 8/24 页面初始化时页面所需rank及成本 from
        //scc 所有rank from
        List<com.nt.dao_Org.Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        HashMap<String, String> dicList = new HashMap<>();

        for(Dictionary dic : dictionaryRank){
            dicList.put(dic.getCode(),dic.getValue1());
        }
        //scc 所有rank to
        for(StaffDetail sta : stafflist){
            String beginningDate = award.getClaimdatetime().split("~")[0].trim();//开始日
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseBegin = sdf.parse(beginningDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parseBegin);
            String beginningYear = String.valueOf(cal.get(Calendar.YEAR));//年
            //人件费 获取实际成本变更 ztc fr
            PeoplewareFee getFee = new PeoplewareFee();
            getFee.setGroupid(sta.getIncondepartment());
            getFee.setYear(beginningYear);
            getFee.setRanks(sta.getAttf());
            List<PeoplewareFee> peeList = peoplewareFeeMapper.select(getFee);
            //Map<String, PeoplewareFee> bmRanksInfo = personalCostService.getBmRanksInfo(beginningYear, sta.getIncondepartment());//部门所有rank成本


//            for(String key : bmRanksInfo.keySet()){
//                costOf.put(dicList.get(key),bmRanksInfo.get(key).getMonth4() + "~" + bmRanksInfo.get(key).getMonth7());
//            }
            sta.setBm(peeList);//页面初始化成本
            //人件费 获取实际成本变更 ztc to
            sta.setIncondepartment(companyid.get(sta.getIncondepartment()));//存部门
        }
        //endregion scc add 8/24 页面初始化时页面所需rank及成本 to
//        String name = "";
//        String [] companyProjectsid = award.getPjnamechinese().split(",");
//        if(companyProjectsid.length > 0){
//            for (int i = 0;i < companyProjectsid.length;i++){
//                CompanyProjects companyProjects = new CompanyProjects();
//                companyProjects.setCompanyprojects_id(companyProjectsid[i]);
//                List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
//                if(comList.size() > 0){
//                    name = name + comList.get(0).getProject_name() + ",";
//                }
//            }
//            if(!name.equals("")){
//                name = name.substring(0,name.length()-1);
//            }
//        }
//        award.setPjnamechinese(name);
        //region scc 判断合同是否是复合合同 from
        List<Dictionary> composites = new ArrayList<>();
        Dictionary WSM = new Dictionary();//海外複合受託 技術開発
        WSM.setCode("HT008002");
        composites.add(WSM);
        Dictionary WGM = new Dictionary();//海外複合受託 役務
        WGM.setCode("HT008004");
        composites.add(WGM);
        Dictionary NSM = new Dictionary();//国内複合受託 技術開発
        NSM.setCode("HT008006");
        composites.add(NSM);
        Dictionary NGM = new Dictionary();//国内複合受託 役務
        NGM.setCode("HT008008");
        composites.add(NGM);
        List<String> composite = new ArrayList<>();
        for(Dictionary dic : composites){
            composite.add(dictionaryService.getDictionaryList(dic).get(0).getValue2());
        }
        boolean flag = false;
        List<Contractcompound> isACompound = null;
        for(String item : composite) {
            if(compoundContractNo.getContractnumber().substring(0,3).equals(item)){
                flag = true;
                Contractcompound contractcompound = new Contractcompound();
                contractcompound.setContractnumber(compoundContractNo.getContractnumber());
                isACompound = contractcompoundMapper.select(contractcompound);
                isACompound = isACompound.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Contractcompound::getGroup_id))), ArrayList::new));
            }
        }
        if(flag && isACompound.size() > 0 && isACompound != null){
            awavo.setContractcompound(isACompound);
        }
        //endregion scc 判断合同是否是复合合同 to
        awavo.setAward(awa);
        awavo.setAwardDetail(awalist);
        awavo.setStaffDetail(stafflist);

        if (awa != null) {
            Contractnumbercount contractnumbercount = new Contractnumbercount();
            contractnumbercount.setContractnumber(awa.getContractnumber());
            List<Contractnumbercount> contractList = contractnumbercountMapper.select(contractnumbercount);
            if (contractList != null && contractList.size() > 1) {
                contractList = contractList.stream().sorted(Comparator.comparing(Contractnumbercount::getRowindex)).collect(Collectors.toList());
            }
            awavo.setNumbercounts(contractList);
        }
        //    PSDCD_PFANS_20210525_XQ_054 复合合同决裁书分配金额可修改 ztc fr
        if (awa != null) {
            AwardReunite awardReunite = new AwardReunite();
            awardReunite.setContractnumber(awa.getContractnumber());
            List<AwardReunite> awardReuniteList = awardReuniteMapper.select(awardReunite);
            if (awardReuniteList != null && awardReuniteList.size() > 1) {
                awardReuniteList = awardReuniteList.stream().sorted(Comparator.comparing(AwardReunite::getRowindex)).collect(Collectors.toList());
            }
            awavo.setAwardReunites(awardReuniteList);
        }
        //    PSDCD_PFANS_20210525_XQ_054 复合合同决裁书分配金额可修改 ztc to
        return awavo;
    }

    //合同决裁书增加事业计划金额功能 1103 ztc fr
    @Override
    public boolean updateAwardVo(AwardVo awardVo, TokenModel tokenModel) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        boolean businorout = false;
        Award award = new Award();
        BeanUtils.copyProperties(awardVo.getAward(), award);
        AwardDetail awardDetail = new AwardDetail();
        awardDetail.setAward_id(award.getAward_id());
        //region scc add 11/11 更新请负委托 from
        this.CommissionedByNegative(award,tokenModel);
        //endregion scc add 11/11 更新请负委托 to
        List<AwardDetail> awardDetail_Old = awardDetailMapper.select(awardDetail)
                .stream().filter(oldtail ->!com.mysql.jdbc.StringUtils.isNullOrEmpty(oldtail.getRulingid())).collect(Collectors.toList());
        if(awardDetail_Old.size() > 0){
            awardDetail_Old.forEach(oldList ->{
                AwardDetail awardDe = awardDetailMapper.selectByPrimaryKey(oldList.getAwarddetail_id());
                if(awardDe != null && !com.mysql.jdbc.StringUtils.isNullOrEmpty(awardDe.getRulingid())){
                    try {
                        businessplanService.cgTpReRulingInfo(awardDe.getRulingid(),awardDe.getAwardmoney(),tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        };
        List<AwardDetail> awardDetails = awardVo.getAwardDetail();
        awardDetails = awardDetails.stream().filter(filt ->!com.mysql.jdbc.StringUtils.isNullOrEmpty(filt.getRulingid())).collect(Collectors.toList());
        OrgTree newOrgInfo = orgTreeService.get(new OrgTree());
        Map<String,BigDecimal> decimalHashMap = new HashMap<>();
        if(awardDetails.size() > 0){
            if(award.getContracttype().contains("HT014") && award.getPlan().equals("0") && !com.mysql.jdbc.StringUtils.isNullOrEmpty(award.getClassificationtype())){
                //委托
                awardDetails.forEach(adls ->{
                    if(decimalHashMap.get(adls.getRulingid()) != null){
                        BigDecimal resultAnt = decimalHashMap.get(adls.getRulingid()).add(new BigDecimal(adls.getAwardmoney()));
                        decimalHashMap.put(adls.getRulingid(),resultAnt);
                    }else{
                        decimalHashMap.put(adls.getRulingid(),new BigDecimal(adls.getAwardmoney()));
                    }
                });
                if(decimalHashMap.size() > 0){
                    for (Map.Entry<String, BigDecimal> entry : decimalHashMap.entrySet()) {
                        String ruid = entry.getKey();
                        BigDecimal useMy = entry.getValue();
                        Ruling ruling = rulingMapper.selectByPrimaryKey(ruid);
                        if (ruling.getActualresidual().subtract(ruling.getApplioccution()).compareTo(useMy) == -1) {
                            businorout = true;
                        }
                    }
                }
                if(businorout){//余额不够
                    award.setPlan("1");
                    award.setClassificationtype("");
                    awardDetails.forEach(awwd ->{
                        awwd.setRulingid("");
                        awwd.setBusinessplanbalance("");
                    });
                }
                else{
                    awardDetails.forEach(awrd ->{
                        try {
                            businessplanService.upRulingInfo(awrd.getRulingid(), awrd.getAwardmoney(), tokenModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
        if(award.getContracttype().contains("HT008") && award.getPlan().equals("0")){
            boolean isconM = false;
            Dictionary dictionary = new Dictionary();
            dictionary.setCode(award.getContracttype());
            List<Dictionary> dicList = dictionaryMapper.select(dictionary);
            if(dicList.size() > 0){
                if(dicList.get(0).getValue2().contains("M")){
                    isconM = true;
                }
            }
            Map<String,String> dirwMap = new HashMap<>();
            Dictionary dicAnt = new Dictionary();
            dicAnt.setPcode("HT008");
            List<Dictionary> dirw = dictionaryService.getDictionaryList(dicAnt);
            if(dirw.size() > 0){
                dirw.forEach(diw ->{
                    dirwMap.put(diw.getCode(),diw.getValue5());
                });
            }
            //受托
            if(!award.getStatus().equals("4")){
                if(isconM){
//                        AwardReunite awardReunite = new AwardReunite();
//                        awardReunite.setContractnumber(award.getContractnumber());
                    List<AwardReunite> awardReuniteList = awardVo.getAwardReunites();
                    if(awardReuniteList.size() > 0)
                    {
                        for (AwardReunite art : awardReuniteList) {
                            String dateM = sf.format(art.getDeliverydate());
                            int yearM = Integer.parseInt(dateM.substring(0, 4));
                            if (Integer.parseInt(dateM.substring(5, 7)) < 4) {
                                yearM = yearM - 1;
                            }
                            String eff_idRunt= "";
                            OrgTree orginfoRunt = orgtreeService.getOrgInfoByComName(newOrgInfo, art.getDepartment());
                            if(orginfoRunt.getEffective()){//有效部门
                                eff_idRunt = orginfoRunt.get_id();
                            }else{
                                eff_idRunt = orginfoRunt.getParent_id();
                            }
                            Ruling rulingM = new Ruling();
                            rulingM.setYears(String.valueOf(yearM));
                            rulingM.setDepart(eff_idRunt);
                            rulingM.setCode(dirwMap.get(award.getContracttype()));
                            List<Ruling> rulingMList = rulingMapper.select(rulingM);
                            if (rulingMList.size() > 0) {
                                BigDecimal surMoney = rulingMList.get(0).getActualresidual().subtract(rulingMList.get(0).getApplioccution());
                                if (surMoney.compareTo(new BigDecimal(art.getDistriamount())) == -1) { //a < b 返回-1
                                    businorout = true;
                                }
                            }
                        }
                        if(businorout){//余额不够
                            award.setPlan("1");
                            award.setClassificationtype("");
                            awardDetails.forEach(awwd ->{
                                awwd.setRulingid("");
                                awwd.setBusinessplanbalance("");
                            });
                        }
                        else{
                            awardReuniteList.forEach(awrdre ->{
                                String dateM = sf.format(awrdre.getDeliverydate());
                                int yearM = Integer.parseInt(dateM.substring(0, 4));
                                if (Integer.parseInt(sf.format(awrdre.getDeliverydate()).substring(5, 7)) < 4) {
                                    yearM = yearM - 1;
                                }
                                try {
                                    AwardReunite awdd = awardReuniteMapper.selectByPrimaryKey(awrdre.getAwardreunite_id());
                                    String eff_id = "";
                                    OrgTree orginfo = orgtreeService.getOrgInfoByComName(newOrgInfo, awdd.getDepartment());
                                    if(orginfo.getEffective()){//有效部门
                                        eff_id = orginfo.get_id();
                                    }else{
                                        eff_id = orginfo.getParent_id();
                                    }
                                    if(awdd != null && !award.getTenantid().equals("first")){ //旧数据
                                        businessplanService.cgTpReRulingInfoAnt(awdd.getDistriamount()
                                                ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                                ,eff_id,tokenModel);
                                    }
                                    businessplanService.upRulingInfoAnt(awrdre.getDistriamount()
                                            ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                            ,eff_id,tokenModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }else{
                    Contractnumbercount cnnt = new Contractnumbercount();
                    cnnt.setContractnumber(award.getContractnumber());
                    List<Contractnumbercount> cnntList = contractnumbercountMapper.select(cnnt);
                    if(cnntList.size() > 0){
                        String dateM = sf.format(cnntList.get(0).getDeliverydate());
                        int yearM = Integer.parseInt(dateM.substring(0, 4));
                        if (Integer.parseInt(sf.format(cnntList.get(0).getDeliverydate()).substring(5, 7)) < 4) {
                            yearM = yearM - 1;
                        }
                        Ruling rulingM = new Ruling();
                        rulingM.setYears(String.valueOf(yearM));
                        rulingM.setDepart(award.getGroup_id());
                        rulingM.setCode(dirwMap.get(award.getContracttype()));
                        List<Ruling> rulingMList = rulingMapper.select(rulingM);
                        if (rulingMList.size() > 0) {
                            BigDecimal surMoney = rulingMList.get(0).getActualresidual().subtract(rulingMList.get(0).getApplioccution());
                            if (surMoney.compareTo(new BigDecimal(award.getSarmb())) == -1) { //a < b 返回-1
                                businorout = true;
                            }
                        }
                        if(businorout){//余额不够
                            award.setPlan("1");
                            award.setClassificationtype("");
                            awardDetails.forEach(awwd ->{
                                awwd.setRulingid("");
                                awwd.setBusinessplanbalance("");
                            });
                        }
                        else{
                            if(!award.getTenantid().equals("first")){ //旧数据
                                businessplanService.cgTpReRulingInfoAnt(award.getSarmb()
                                        ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                        ,award.getGroup_id(),tokenModel);
                            }
                            businessplanService.upRulingInfoAnt(award.getSarmb()
                                    ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                    ,award.getGroup_id(),tokenModel);
                        }
                    }
                }
            }
            else if(award.getStatus().equals("4")){
                if(isconM){//复合合同
                    AwardReunite awardReunite = new AwardReunite();
                    awardReunite.setContractnumber(award.getContractnumber());
                    List<AwardReunite> awardReuniteList = awardReuniteMapper.select(awardReunite);
                    if(awardReuniteList.size() > 0){
                        for (AwardReunite arrent : awardReuniteList) {
                            String dateM = sf.format(arrent.getDeliverydate());
                            int yearM = Integer.parseInt(dateM.substring(0, 4));
                            if (Integer.parseInt(sf.format(arrent.getDeliverydate()).substring(5, 7)) < 4) {
                                yearM = yearM - 1;
                            }
                            String eff_id = "";
                            OrgTree orginfo = orgtreeService.getOrgInfoByComName(newOrgInfo, arrent.getDepartment());
                            if(orginfo.getEffective()){//有效部门
                                eff_id = orginfo.get_id();
                            }else{
                                eff_id = orginfo.getParent_id();
                            }
                            businessplanService.cgTpReRulingInfoAnt(arrent.getDistriamount()
                                    ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                    ,eff_id,tokenModel);
                            businessplanService.woffRulingInfoAnt(arrent.getDistriamount()
                                    ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                    ,eff_id,tokenModel);
                        }
                    }
                }else{
                    Contractnumbercount cnnt = new Contractnumbercount();
                    cnnt.setContractnumber(award.getContractnumber());
                    List<Contractnumbercount> cnntList = contractnumbercountMapper.select(cnnt);
                    if(cnntList.size() > 0) {
                        String dateM = sf.format(cnntList.get(0).getDeliverydate());
                        int yearM = Integer.parseInt(dateM.substring(0, 4));
                        if (Integer.parseInt(sf.format(cnntList.get(0).getDeliverydate()).substring(5, 7)) < 4) {
                            yearM = yearM - 1;
                        }
                        businessplanService.cgTpReRulingInfoAnt(award.getSarmb()
                                ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                ,award.getGroup_id(),tokenModel);
                        businessplanService.woffRulingInfoAnt(award.getSarmb()
                                ,dirwMap.get(award.getContracttype()),String.valueOf(yearM)
                                ,award.getGroup_id(),tokenModel);
                    }
                }
            }
        }
        award.preUpdate(tokenModel);
        award.setTenantid("second");
        awardMapper.updateByPrimaryKey(award);
        String awardid = award.getAward_id();
        //add-ws-7/21-禅道任务341
        String contractnumber = award.getContractnumber();
        String status = award.getStatus();
        if (status.equals("4")) {
            //upd-ws-9/17-禅道任务530提交
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(award.getPolicycontract_id())) {
                int scale = 2;//设置位数
                int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                PolicyContract po = new PolicyContract();
                String policycontract_id = award.getPolicycontract_id();
                po.setPolicycontract_id(policycontract_id);
                PolicyContract policycontractlist = policycontractmapper.selectOne(po);
                PolicyContract policy = new PolicyContract();
                PolicyContract policy2 = new PolicyContract();
                BeanUtils.copyProperties(policycontractlist, policy);
                policy2.setPolicycontract_id(policy.getPolicycontract_id());
                policycontractmapper.delete(policy2);
                //region add_qhr_20210724 加入非空判断
                BigDecimal bd = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(policy.getModifiedamount())? "0" : policy.getModifiedamount());
                bd = bd.setScale(scale, roundingMode);
                BigDecimal bd1 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(policy.getNewamountcase())? "0" : policy.getNewamountcase());
                bd1 = bd1.setScale(scale, roundingMode);
                BigDecimal bd2 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(award.getClaimamount())? "0" : award.getClaimamount());
                bd2 = bd2.setScale(scale, roundingMode);
                BigDecimal bd3 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(policy.getAvbleamount())? "0" : policy.getAvbleamount());
                //endregion add_qhr_20210724 加入非空判断
                bd3 = bd3.setScale(scale, roundingMode);
                policy.preUpdate(tokenModel);
                policy.setModifiedamount(String.valueOf(bd.subtract(bd2)));
                policy.setAvbleamount(String.valueOf(bd3.subtract(bd2)));
                policy.setNewamountcase(String.valueOf(bd1.add(bd2)));
                policycontractmapper.insertSelective(policy);
            }
            //upd-ws-9/17-禅道任务530提交
            if (award.getMaketype().equals("9")) {
                //合同担当
                List<MembersVo> rolelist = roleService.getMembers("5e7862618f43163084351135");
                if (rolelist.size() > 0) {
                    for (MembersVo rt : rolelist) {
                        ToDoNotice toDoNotice3 = new ToDoNotice();
                        toDoNotice3.setTitle("【" + contractnumber + "】决裁流程结束，请申请印章");
                        toDoNotice3.setInitiator(award.getUser_id());
                        toDoNotice3.setContent("流程结束，请申请印章");
                        toDoNotice3.setDataid(contractnumber);
                        toDoNotice3.setUrl("/PFANS1047View");
                        toDoNotice3.setWorkflowurl("/PFANS1047View");
                        toDoNotice3.preInsert(tokenModel);
                        toDoNotice3.setOwner(rt.getUserid());
                        toDoNoticeService.save(toDoNotice3);
                    }
                }
            }
//            else
//            {
//                //合同担当
//                List<MembersVo> rolelist = roleService.getMembers("5e7862618f43163084351135");
//                if (rolelist.size() > 0) {
//                    for (MembersVo rt : rolelist) {
//                        ToDoNotice toDoNotice3 = new ToDoNotice();
//                        toDoNotice3.setTitle("【" + contractnumber + "】决裁流程结束，请申请印章");
//                        toDoNotice3.setInitiator(award.getUser_id());
//                        toDoNotice3.setContent("流程结束，请申请印章");
//                        toDoNotice3.setDataid(contractnumber);
//                        toDoNotice3.setUrl("/PFANS1025View");
//                        toDoNotice3.setWorkflowurl("/PFANS1025View");
//                        toDoNotice3.preInsert(tokenModel);
//                        toDoNotice3.setOwner(rt.getUserid());
//                        toDoNoticeService.save(toDoNotice3);
//                    }
//                }
//            }
        }
        //add-ws-7/21-禅道任务341
        AwardDetail award2 = new AwardDetail();
        award2.setAward_id(awardid);
        awardDetailMapper.delete(award2);



        StaffDetail sta = new StaffDetail();
        sta.setAward_id(awardid);
        staffDetailMapper.delete(sta);
        List<StaffDetail> stalist = awardVo.getStaffDetail();
        List<AwardDetail> awardDetailList = awardVo.getAwardDetail();


        if (awardDetailList != null) {
            int rowindex = 0;
            for (AwardDetail awarddetail : awardDetailList) {
                rowindex = rowindex + 1;
                awarddetail.preInsert(tokenModel);
                awarddetail.setAwarddetail_id(UUID.randomUUID().toString());
                awarddetail.setAward_id(awardid);
                awarddetail.setRowindex(rowindex);
                awardDetailMapper.insertSelective(awarddetail);
            }
        }

        if (stalist != null) {
            int rowindex = 0;
            //region scc add 8/24 根据部门简称存部门id from
            List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
            HashMap<String,String> companyid = new HashMap<>();
            for(DepartmentVo vo : allDepartment){
                companyid.put(vo.getDepartmentEn(),vo.getDepartmentId());
            }
            //endregion scc add 8/24 根据部门简称存部门id to
            for (StaffDetail staffDetail : stalist) {
                rowindex = rowindex + 1;
                staffDetail.preInsert(tokenModel);
                staffDetail.setStaffdetail_id(UUID.randomUUID().toString());
                staffDetail.setAward_id(awardid);
                //region scc 存部门id from
                staffDetail.setIncondepartment(companyid.get(staffDetail.getIncondepartment()));
                //endregion scc 存部门id to
                staffDetail.setRowindex(rowindex);
                staffDetailMapper.insertSelective(staffDetail);
            }
        }

        if(awardVo.getAwardReunites() != null){
            for(AwardReunite awardReunite : awardVo.getAwardReunites()){
                awardReunite.preUpdate(tokenModel);
                awardReuniteMapper.updateByPrimaryKey(awardReunite);
            }
        }
        return businorout;
    }
    //合同决裁书增加事业计划金额功能 1103 ztc to

    @Override
    public void dataCarryover(Award award,TokenModel tokenModel) throws Exception {
        award.preUpdate(tokenModel);
        awardMapper.updateByPrimaryKeySelective(award);
    }

    //region scc add 21/8/20 受托合同，详情，部门下拉框数据源 from
    public List<String> getCompanyen() throws Exception{
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        List<String> companyens = new ArrayList<>();
        for(DepartmentVo vo : allDepartment){
            companyens.add(vo.getDepartmentEn());
        }
        return companyens;
    }
    //endregion scc add 21/8/20 受托合同，详情，部门下拉框数据源 to

    //PSDCD_PFANS_20210723_XQ_086 委托决裁报销明细自动带出 ztc fr
    public List<AwardDetail> getAwardEntr(List<String> awardIdList) throws Exception{
        List<AwardDetail> awardDetails = awardDetailMapper.getAdInfoList(awardIdList);
        return awardDetails;
    }
    //PSDCD_PFANS_20210723_XQ_086 委托决裁报销明细自动带出 ztc to

    //region scc add 委托决裁在流程结束时，更新请负委托一览 from
    private void CommissionedByNegative(Award award,TokenModel tokenModel) throws Exception {
        if ("4".equals(award.getStatus()) && award != null) {//审批结束
            Contractapplication record = new Contractapplication();
            record.setContractnumber(award.getContractnumber());
            Contractapplication recordOne = contractapplicationMapper.selectOne(record);//根据合同号查询合同
            if("1".equals(recordOne.getCheckindivdual())){//去除月度费用总览创建的合同
                return;
            }
            Contractnumbercount backToNumberOf = new Contractnumbercount();
            backToNumberOf.setContractnumber(award.getContractnumber());
            List<Contractnumbercount> backToNumberOfList = contractnumbercountMapper.select(backToNumberOf);//合同号对应回数
            List<EntrustSupport> res = new ArrayList<>();//请负委托数据
            if(backToNumberOfList != null && backToNumberOfList.size() > 0){
                backToNumberOfList.forEach(item -> {
                    EntrustSupport temp = new EntrustSupport();
                    temp.setEntrustsupport_id(UUID.randomUUID().toString());//id
                    temp.setContractnumber(recordOne.getContractnumber());//合同号
                    temp.setGroup_id(recordOne.getGroup_id());//部门id
                    temp.setDepartment(recordOne.getDepartment());//部门简称
                    temp.setDeployment(recordOne.getDeployment());//部门名称
                    temp.setConjapanese(recordOne.getConjapanese());//项目名
                    temp.setCustojapanese(recordOne.getCustojapanese());//外注公司
                    temp.setClaimamount(item.getClaimamount());//请求金额
                    temp.setClaimdate(item.getClaimdate());//请求日期
                    temp.setDeliverydate(item.getDeliverydate());//纳品日期
                    temp.setCompletiondate(item.getCompletiondate());//验收日期
                    temp.setSupportdate(item.getSupportdate());//支付日期
                    temp.setProcessing("false");//处理状态
                    temp.setUndertaker(null);//担当着
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(item.getClaimdate());
                    String year = String.valueOf(cal.get(Calendar.YEAR));
                    String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                    temp.setDates(year + "-" + month);//年月
                    temp.preInsert();
                    res.add(temp);
                });
            }
            if(res != null && res.size() > 0){
                entrustSupportMapper.insetList(res);//插入请负委托一览
            }
            //region 合同担当发送代办 from
            List<MembersVo> rolelist = roleService.getMembers("5e7862618f43163084351135");//合同担当
            if (rolelist.size() > 0) {
                for (MembersVo rt : rolelist) {
                    ToDoNotice toDoNotice3 = new ToDoNotice();
                    toDoNotice3.setTitle("【" + award.getContractnumber() + "】决裁流程结束，请前往请负委托一览进行状态处理！");
                    toDoNotice3.setInitiator(award.getUser_id());
                    toDoNotice3.setDataid(award.getContractnumber());
                    toDoNotice3.setUrl("/PFANS6012View");
                    toDoNotice3.setWorkflowurl("/PFANS6012View");
                    toDoNotice3.preInsert(tokenModel);
                    toDoNotice3.setOwner(rt.getUserid());
                    toDoNoticeService.save(toDoNotice3);
                }
            }
            //endregion 合同担当发送代办 to
        }
    }
    //endregion scc add 委托决裁在流程结束时，更新请负委托一览 to

}
