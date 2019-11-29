package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.*;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class GivingServiceImpl implements GivingService {

    @Autowired
    private GivingMapper givingMapper;

    @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private WagesMapper wagesMapper;

    @Autowired
    private DutyfreeMapper dutyfreeMapper;

    @Autowired
    private ComprehensiveMapper comprehensiveMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ContrastMapper contrastMapper;

    @Autowired
    private AbNormalMapper abNormalMapper;

    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Autowired
    private OtherOneMapper otherOneMapper;

    @Autowired
    private OtherFiveMapper otherfiveMapper;

    @Autowired
    private OtherFourMapper otherfourMapper;

    @Autowired
    private AppreciationMapper appreciationMapper;

    @Autowired
    private AccumulatedTaxMapper accumulatedTaxMapper;

    @Autowired
    private DisciplinaryMapper disciplinaryMapper;


    /**
     * 生成基数表
     * FJL
     */
    @Override
    public GivingVo List(String giving_id) throws Exception {
        GivingVo givingVo = new GivingVo();
        Giving giving = new Giving();
        giving.setGiving_id(giving_id);
        givingVo.setGiving(giving);

        List<DisciplinaryVo> disciplinary = disciplinaryMapper.getdisciplinary();
        givingVo.setDisciplinaryVo(disciplinary);

        OtherOne otherOne = new OtherOne();
        otherOne.setGiving_id(giving_id);
        List<OtherOne> otherOnelist = otherOneMapper.select(otherOne);
        otherOnelist = otherOnelist.stream().sorted(Comparator.comparing(OtherOne::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherOne(otherOnelist);

        OtherTwo othertwo = new OtherTwo();
        othertwo.setGiving_id(giving_id);
        List<OtherTwo> othertwolist = othertwoMapper.select(othertwo);
        othertwolist = othertwolist.stream().sorted(Comparator.comparing(OtherTwo::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherTwo(othertwolist);

        Appreciation appreciation = new Appreciation();
        appreciation.setGiving_id(giving_id);
        List<Appreciation> appreciationlist = appreciationMapper.select(appreciation);
        appreciationlist = appreciationlist.stream().sorted(Comparator.comparing(Appreciation::getRowindex)).collect(Collectors.toList());
        givingVo.setAppreciation(appreciationlist);

        OtherFive otherfive = new OtherFive();
        otherfive.setGiving_id(giving_id);
        List<OtherFive> otherfivelist = otherfiveMapper.select(otherfive);
        otherfivelist = otherfivelist.stream().sorted(Comparator.comparing(OtherFive::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherFive(otherfivelist);

        OtherFour otherfour = new OtherFour();
        otherfour.setGiving_id(giving_id);
        List<OtherFour> otherfourlist = otherfourMapper.select(otherfour);
        otherfourlist = otherfourlist.stream().sorted(Comparator.comparing(OtherFour::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherFour(otherfourlist);

        Base base = new Base();
        base.setGiving_id(giving_id);
        List<Base> baselist = baseMapper.select(base);
        baselist = baselist.stream().sorted(Comparator.comparing(Base::getRowindex)).collect(Collectors.toList());
        givingVo.setBase(baselist);

        Contrast contrast = new Contrast();
        contrast.setGiving_id(giving_id);
        List<Contrast> contrastList = contrastMapper.select(contrast);
        contrastList = contrastList.stream().sorted(Comparator.comparing(Contrast::getRowindex)).collect(Collectors.toList());
        givingVo.setContrast(contrastList);

        List<AccumulatedTaxVo> accumulatedTaxVolist = accumulatedTaxMapper.getaccumulatedTax();
        givingVo.setAccumulatedTaxVo(accumulatedTaxVolist);

        List<DutyfreeVo> dutyfreeVolist = dutyfreeMapper.getdutyfree();
        givingVo.setDutyfreeVo(dutyfreeVolist);

        List<ComprehensiveVo> comprehensiveVolist = comprehensiveMapper.getcomprehensive();
        givingVo.setComprehensiveVo(comprehensiveVolist);

        return givingVo;
    }

    @Override
    public void insertOtherOne(String givingid, TokenModel tokenModel) throws Exception {
        OtherOne otherOne = new OtherOne();
        AbNormal abNormal = new AbNormal();
        abNormal.setStatus("4");
        List<AbNormal> abNormalinfo = abNormalMapper.select(abNormal);
        if (abNormalinfo != null) {
            int rowindex = 0;
            for (AbNormal abNor : abNormalinfo) {
                if (abNor.getErrortype().equals("PR013012") || abNor.getErrortype().equals("PR013013")) {
                    rowindex = rowindex + 1;
                    String otherOneid = UUID.randomUUID().toString();
                    otherOne.preInsert(tokenModel);
                    otherOne.setOtherone_id(otherOneid);
                    otherOne.setGiving_id(givingid);
                    otherOne.setUser_id(abNor.getUser_id());
                    Query query = new Query();
                    String User_id = abNor.getUser_id();
                    query.addCriteria(Criteria.where("userid").is(User_id));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        String departmentid = customerInfo.getUserinfo().getDepartmentid().toString();
                        String name = departmentid.replace("[", "").replace("]", "");
                        otherOne.setDepartment_id(name);
                        otherOne.setSex(customerInfo.getUserinfo().getSex());
                        otherOne.setWorkdate(customerInfo.getUserinfo().getEnterday());
                    }
                    if (abNor.getErrortype().equals("PR013012")) {
                        otherOne.setReststart(abNor.getOccurrencedate());
                        otherOne.setRestend(abNor.getFinisheddate());
                        otherOne.setAttendance("-1");
                        otherOne.setOther1("-1");
                        otherOne.setType("1");
                    } else if (abNor.getErrortype().equals("PR013013")) {
                        otherOne.setStartdate(abNor.getOccurrencedate());
                        otherOne.setEnddate(abNor.getFinisheddate());
                        int intLengthtime = Integer.parseInt(abNor.getLengthtime())/8;
                        String strLengthtime = String.valueOf(intLengthtime);
                        otherOne.setVacation(strLengthtime);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String beginTime  = customerInfo.getUserinfo().getEnterday();
                        Date date1 = format.parse(beginTime );
                        Date date2 = format.parse("2012-08-31");
                        long beginMillisecond = date1.getTime();
                        long endMillisecond = date2.getTime();
                        if(beginMillisecond >= endMillisecond){
                            otherOne.setHandsupport(strLengthtime);
                        } else {
                            otherOne.setHandsupport("0");
                        }
                        otherOne.setType("2");
                    }
                    otherOne.setRowindex(rowindex);
                    otherOneMapper.insertSelective(otherOne);
                }
            }
        }
    }

    @Override
    public void insertBase(String givingid, TokenModel tokenModel) throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            int rowindex = 0;
            for (CustomerInfo customer : customerinfo) {
                rowindex = rowindex + 1;
                Base base = new Base();
                String baseid = UUID.randomUUID().toString();
                base.preInsert(tokenModel);
                base.setBase_id(baseid);
                base.setGiving_id(givingid);
                base.setUser_id(customer.getUserid());  //名字
//                base.setOwner(customer.getUserid());
                String departmentid = customer.getUserinfo().getDepartmentid().toString();
                String name = departmentid.replace("[", "").replace("]", "");
                base.setDepartment_id(name);  //部门
                base.setJobnumber(customer.getUserinfo().getJobnumber());  //工号
                base.setRn(customer.getUserinfo().getRank());  //RN
                base.setSex(customer.getUserinfo().getSex());  //性别
                if (customer.getUserinfo().getChildren() != null) {
                    base.setOnlychild("1");  //独生子女
                } else {
                    base.setOnlychild("2");  //独生子女
                }
                //入/退職/産休
                //奨金計上
                base.setBonus(customer.getUserinfo().getDifference());
                //1999年前社会人
//                if(customer.getUserinfo().getWorkday() != null){
//                    String strWorkday = customer.getUserinfo().getWorkday().substring(0,4);
//                    if (Integer.parseInt(strWorkday) > 1999) {
//                        base.setSociology("1");
//                    } else {
//                        base.setSociology("2");
//                    }
//                }
                //大連戸籍
                if (customer.getUserinfo().getRegister() == "大连") {
                    base.setRegistered("1");
                } else {
                    base.setRegistered("2");
                }
                //2019年6月
                //2019年7月
                base.setPension(customer.getUserinfo().getOldageinsurance()); //養老・失業・工傷基数
                base.setMedical(customer.getUserinfo().getMedicalinsurance()); //医療・生育基数

                base.setAccumulation(customer.getUserinfo().getHousefund());  //公积金基数
                //采暖费
//                if (customer.getUserinfo().getRank() != null) {
//                    String strRank = customer.getUserinfo().getRank().substring(2);
//                    int rank = Integer.parseInt(strRank);
//                    if (rank >= 21009) {
//                        base.setHeating("229");
//                    } else if (customer.getUserinfo().getRank() == "PR021008") {
//                        base.setHeating("172");
//                    } else if (rank <= 21007) {
//                        base.setHeating("139");
//                    }
//                }
                //入社日
                base.setWorkdate(customer.getUserinfo().getEnterday());
                base.setRowindex(rowindex);
                baseMapper.insertSelective(base);
            }
        }
    }


    @Override
    public void insertOtherTwo(String givingid, TokenModel tokenModel) throws Exception {
        OtherTwo othertwo = new OtherTwo();
        CasgiftApply casgiftapply = new CasgiftApply();
        casgiftapply.setPayment("0");
        casgiftapply.setStatus("4");
        List<CasgiftApply> casgiftapplylist = casgiftapplyMapper.select(casgiftapply);
        othertwo.setType("0");
        othertwoMapper.delete(othertwo);
        int rowundex = 0;
        for (CasgiftApply casgift : casgiftapplylist) {
            rowundex = rowundex + 1;
            String othertwoid = UUID.randomUUID().toString();
            othertwo.preInsert(tokenModel);
            othertwo.setOthertwo_id(othertwoid);
            othertwo.setGiving_id(givingid);
            othertwo.setUser_id(casgift.getUser_id());
            Query query = new Query();
            String User_id = casgift.getUser_id();
            query.addCriteria(Criteria.where("userid").is(User_id));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            othertwo.setJobnumber(customerInfo.getUserinfo().getJobnumber());
            othertwo.setType("0");
            othertwo.setRowindex(rowundex);
            othertwo.setRootknot(casgift.getTwoclass());
            othertwo.setMoneys(casgift.getAmoutmoney());
            othertwoMapper.insertSelective(othertwo);
        }
    }

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public void insertContrast(String givingid, TokenModel tokenModel) throws Exception {
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baselist = baseMapper.select(base);
        if (baselist != null) {
            int rowindex = 0;
            for (Base base1 : baselist) {
                rowindex = rowindex + 1;
                Contrast contrast = new Contrast();
                String consrastid = UUID.randomUUID().toString();
                contrast.preInsert(tokenModel);
                contrast.setGiving_id(base1.getGiving_id());
                contrast.setContrast_id(consrastid);
                contrast.setUser_id(base1.getUser_id());
                contrast.setOwner(base1.getUser_id());
                contrast.setRowindex(rowindex);
                contrast.setDepartment_id(base1.getDepartment_id());
//
//                Wages wages = new Wages();
//                wages.setGiving_id(givingid);
//                List<Wages> wageslist = wagesMapper.select(wages);
//                if(wageslist != null){
//                    contrast.setThismonth(wageslist.getRealwages());
//
//                    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
//                    String strTemp = sf1.format(new Date());
//                    Date delDate = sf1.parse(strTemp);
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(delDate);
//                    c.add(Calendar.MONTH, -1);
//
//                    int year1 = c.get(Calendar.YEAR);    //获取年
//                    int month1 = c.get(Calendar.MONTH) + 1;
//
//
//                    contrast.setLastmonth();
//                }

                contrastMapper.insertSelective(contrast);
            }
        }
    }

    @Override
    public void insert(String generation, TokenModel tokenModel) throws Exception {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        String givingid = UUID.randomUUID().toString();
        Giving giving = new Giving();
        giving.preInsert(tokenModel);
        giving.setGiving_id(givingid);
        giving.setGeneration(generation);
        giving.setGenerationdate(new Date());
        giving.setMonths(sf1.format(new Date()));

        Giving giving1 = new Giving();
        String strTemp = sf1.format(new Date());
        giving1.setMonths(strTemp);
        givingMapper.delete(giving1);
        givingMapper.insert(giving);
        insertBase(givingid, tokenModel);
        insertContrast(givingid, tokenModel);
        insertOtherTwo(givingid, tokenModel);
        insertOtherOne(givingid, tokenModel);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }
}
