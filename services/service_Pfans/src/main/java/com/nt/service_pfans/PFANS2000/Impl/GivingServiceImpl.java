package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.*;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class GivingServiceImpl implements GivingService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private GivingMapper givingMapper;

    @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private RetireMapper retireMapper;

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
    private LackattendanceMapper lackattendanceMapper;

    @Autowired
    private ResidualMapper residualMapper;

    @Autowired
    private OtherTwo2Mapper othertwo2Mapper;

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


    @Autowired
    private AdditionalMapper additionalMapper;

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

        Additional additional = new Additional();
        additional.setGiving_id(giving_id);
        List<Additional> additionallist = additionalMapper.select(additional);
        additionallist = additionallist.stream().sorted(Comparator.comparing(Additional::getRowindex)).collect(Collectors.toList());
        givingVo.setAddiTional(additionallist);

        Lackattendance lackattendance = new Lackattendance();
        lackattendance.setGiving_id(giving_id);
        List<Lackattendance> lackattendancellist = lackattendanceMapper.select(lackattendance);
        lackattendancellist = lackattendancellist.stream().sorted(Comparator.comparing(Lackattendance::getRowindex)).collect(Collectors.toList());
        givingVo.setLackattendance(lackattendancellist);

        Residual residua = new Residual();
        residua.setGiving_id(giving_id);
        List<Residual> residualllist = residualMapper.select(residua);
        residualllist = residualllist.stream().sorted(Comparator.comparing(Residual::getRowindex)).collect(Collectors.toList());
        givingVo.setResidual(residualllist);

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
                        otherOne.setDepartment_id(customerInfo.getUserinfo().getCenterid());
                        otherOne.setSex(customerInfo.getUserinfo().getSex());
                        otherOne.setWorkdate(customerInfo.getUserinfo().getEnterday());
                    }
                    if (abNor.getErrortype().equals("PR013012")) {
                        otherOne.setReststart(abNor.getOccurrencedate());
                        otherOne.setRestend(abNor.getFinisheddate());
                        otherOne.setAttendance("-1");
                        otherOne.setOther1("-1");
                        otherOne.setBasedata("2");
                        otherOne.setType("1");
                    } else if (abNor.getErrortype().equals("PR013013")) {
                        otherOne.setStartdate(abNor.getOccurrencedate());
                        otherOne.setEnddate(abNor.getFinisheddate());
                        int intLengthtime = Integer.parseInt(abNor.getLengthtime()) / 8;
                        String strLengthtime = String.valueOf(intLengthtime);
                        otherOne.setVacation(strLengthtime);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String beginTime = customerInfo.getUserinfo().getEnterday();
                        Date date1 = format.parse(beginTime);
                        Date date2 = format.parse("2012-08-31");
                        long beginMillisecond = date1.getTime();
                        long endMillisecond = date2.getTime();
                        if (beginMillisecond >= endMillisecond) {
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

        Dictionary dictionary = new Dictionary();
        dictionary.setPcode("PR042");
        List<Dictionary> dictionarylist = dictionaryMapper.select(dictionary);
        String R9 = null;
        String R8 = null;
        String R7 = null;
        for (Dictionary diction : dictionarylist) {
            if (diction.getCode().equals("PR042006")) {
                R9 = diction.getValue2();
            } else if (diction.getCode().equals("PR042007")) {
                R8 = diction.getValue2();
            } else if (diction.getCode().equals("PR042008")) {
                R7 = diction.getValue2();
            }
        }
        Base base = new Base();
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            int rowindex = 0;
            for (CustomerInfo customer : customerinfo) {
                rowindex = rowindex + 1;
                String baseid = UUID.randomUUID().toString();
                base.preInsert(tokenModel);
                base.setBase_id(baseid);
                base.setGiving_id(givingid);
                base.setUser_id(customer.getUserid());  //名字
//                base.setOwner(customer.getUserid());
                base.setDepartment_id(customer.getUserinfo().getCenterid());
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
                if (customer.getUserinfo().getWorkday() != null && customer.getUserinfo().getWorkday().length() > 0) {
                    String strWorkday = customer.getUserinfo().getWorkday().substring(0, 4);
                    if (Integer.parseInt(strWorkday) > 1999) {
                        base.setSociology("1");
                    } else {
                        base.setSociology("2");
                    }
                }
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
                if (customer.getUserinfo().getRank() != null && customer.getUserinfo().getRank().length() > 0) {
                    String strRank = customer.getUserinfo().getRank().substring(2);
                    int rank = Integer.parseInt(strRank);
                    if (rank >= 21009) {
                        base.setHeating(R9);
                    } else if (customer.getUserinfo().getRank() == "PR021008") {
                        base.setHeating(R8);
                    } else if (rank <= 21007) {
                        base.setHeating(R7);
                    }
                }
                //入社日
                base.setWorkdate(customer.getUserinfo().getEnterday());
                base.setRowindex(rowindex);
                baseMapper.insertSelective(base);
            }
        }
    }

    @Override
    public void insertResidual(String givingid, TokenModel tokenModel) throws Exception {
        Residual residual = new Residual();
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baselist = baseMapper.select(base);
        for (Base b : baselist) {
            Calendar cal = Calendar.getInstance();
            String years = String.valueOf(cal.get(cal.YEAR));
            String months = String.valueOf(cal.get(cal.MONTH));
            String user_id = b.getUser_id();
            List<Attendance> AttendanceList = givingMapper.selectAttendance(user_id, years, months);
            int rowundex = 0;
            for (Attendance attendance : AttendanceList) {
                double one = 0d;
                double two = 0d;
                double three = 0d;
                double four = 0d;
                double Lasttotaly = 0d;
                double Thistotaly = 0d;
                rowundex = rowundex + 1;
                DecimalFormat df = new DecimalFormat(".00");
                residual.setRowindex(rowundex);
                residual.setResidual_id(UUID.randomUUID().toString());
                residual.setGiving_id(givingid);
                residual.setUser_id(attendance.getUser_id());
                residual.setRn(b.getRn());
                Retire retire = new Retire();
                retire.setUser_id(attendance.getUser_id());
                List<Retire> retirelist = retireMapper.select(retire);
                if (retirelist.size() != 0) {
                    residual.setRemarks("退职");
                    residual.setThisweekdays(attendance.getOrdinaryindustry());
                    residual.setThisrestDay(attendance.getWeekendindustry());
                    residual.setThislegal(attendance.getStatutoryresidue());
                    residual.setThislatenight(attendance.getOrdinaryindustrynight());
                    residual.setThisrestlatenight(attendance.getWeekendindustrynight());
                    residual.setThislegallatenight(attendance.getStatutoryresiduenight());
                    int i = 0;
                    int o = 0;
                    int m = 0;
                    int n = 0;
                    int z = 0;
                    int k = 0;
                    int Daixiu1 = 0;
                    int Daixiu2 = 0;
                    int Daixiu3 = 0;
                    int Daixiu4 = 0;
                    int Daixiu5 = 0;
                    int Daixiu6 = 0;
                    int Daixiu7 = 0;
                    String months1;
                    String years1;
                    if (cal.get(cal.MONTH) == 1) {
                        months1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        years1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 2) {
                        months1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        years1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 3) {
                        months1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        years1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 4) {
                        months1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        years1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        months1 = String.valueOf(cal.get(cal.MONTH) - 4);
                        years1 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance atten = new Attendance();
                    atten.setUser_id(attendance.getUser_id());
                    atten.setYears(years1);
                    atten.setMonths(months1);
                    List<Attendance> Attendancelist = attendanceMapper.select(atten);
                    for (Attendance attendancelist : Attendancelist) {
                        if (Integer.parseInt(attendancelist.getWeekendindustry()) >= 8) {
                            i = i + 1;
                        }
                    }
                    String months2;
                    String years2;
                    if (cal.get(cal.MONTH) == 1) {
                        months2 = String.valueOf(cal.get(cal.MONTH) + 11);
                        years2 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        months2 = String.valueOf(cal.get(cal.MONTH) - 1);
                        years2 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance a = new Attendance();
                    a.setUser_id(attendance.getUser_id());
                    a.setYears(years2);
                    a.setMonths(months2);
                    List<Attendance> Attendancelist1 = attendanceMapper.select(a);
                    for (Attendance attendancelist1 : Attendancelist1) {
                        if (Integer.parseInt(attendancelist1.getWeekendindustry()) >= 8) {
                            o = o + 1;
                        }
                    }
                    List<Attendance> attendanceList = givingMapper.selectAttendance(user_id, years, months2);
                    for (Attendance A : attendanceList) {
                        if (A.getDaixiu() != null) {
                            Daixiu1 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu1 = 0;
                        }
                    }
                    String months3;
                    String years3;
                    if (cal.get(cal.MONTH) == 1) {
                        months3 = String.valueOf(cal.get(cal.MONTH) + 10);
                        years3 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 2) {
                        months3 = String.valueOf(cal.get(cal.MONTH) + 10);
                        years3 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        months3 = String.valueOf(cal.get(cal.MONTH) - 2);
                        years3 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance a1 = new Attendance();
                    a1.setUser_id(attendance.getUser_id());
                    a1.setYears(years3);
                    a1.setMonths(months3);
                    List<Attendance> Attendancelist2 = attendanceMapper.select(a1);
                    for (Attendance attendancelist2 : Attendancelist2) {
                        if (Integer.parseInt(attendancelist2.getWeekendindustry()) >= 8) {
                            m = m + 1;
                        }
                    }
                    List<Attendance> attendanceList1 = givingMapper.selectAttendance(user_id, years3, months3);
                    for (Attendance A : attendanceList1) {
                        if (A.getDaixiu() != null) {
                            Daixiu2 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu2 = 0;
                        }
                    }
                    String months4;
                    String years4;
                    if (cal.get(cal.MONTH) == 1) {
                        months4 = String.valueOf(cal.get(cal.MONTH) + 9);
                        years4 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 2) {
                        months4 = String.valueOf(cal.get(cal.MONTH) + 9);
                        years4 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 3) {
                        months4 = String.valueOf(cal.get(cal.MONTH) + 9);
                        years4 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        months4 = String.valueOf(cal.get(cal.MONTH) - 3);
                        years4 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance a2 = new Attendance();
                    a2.setUser_id(attendance.getUser_id());
                    a2.setYears(years4);
                    a2.setMonths(months4);
                    List<Attendance> Attendancelist3 = attendanceMapper.select(a2);
                    for (Attendance attendancelist3 : Attendancelist3) {
                        if (Integer.parseInt(attendancelist3.getWeekendindustry()) >= 8) {
                            n = n + 1;
                        }
                    }
                    List<Attendance> attendanceList2 = givingMapper.selectAttendance(user_id, years4, months4);
                    for (Attendance A : attendanceList2) {
                        if (A.getDaixiu() != null) {
                            Daixiu3 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu3 = 0;
                        }
                    }
                    String months5 = String.valueOf(cal.get(cal.MONTH));
                    Attendance a3 = new Attendance();
                    a3.setUser_id(attendance.getUser_id());
                    a3.setYears(years);
                    a3.setMonths(months5);
                    List<Attendance> Attendancelist4 = attendanceMapper.select(a3);
                    for (Attendance attendancelist4 : Attendancelist4) {
                        if (Integer.parseInt(attendancelist4.getWeekendindustry()) >= 8) {
                            z = z + 1;
                        }
                    }
                    List<Attendance> attendanceList5 = givingMapper.selectAttendance(user_id, years, months5);
                    for (Attendance A : attendanceList5) {
                        if (A.getDaixiu() != null) {
                            Daixiu4 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu4 = 0;
                        }
                    }
                    String months6;
                    String years6;
                    if (cal.get(cal.MONTH) == 12) {
                        months6 = String.valueOf(cal.get(cal.MONTH) - 11);
                        years6 = String.valueOf(cal.get(cal.YEAR) + 1);
                    } else {
                        months6 = String.valueOf(cal.get(cal.MONTH) + 1);
                        years6 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance a4 = new Attendance();
                    a4.setUser_id(attendance.getUser_id());
                    a4.setYears(years6);
                    a4.setMonths(months6);
                    List<Attendance> Attendancelist5 = attendanceMapper.select(a4);
                    for (Attendance attendancelist5 : Attendancelist5) {
                        if (Integer.parseInt(attendancelist5.getWeekendindustry()) >= 8) {
                            k = k + 1;
                        }
                    }
                    List<Attendance> attendanceList6 = givingMapper.selectAttendance(user_id, years6, months6);
                    for (Attendance A : attendanceList6) {
                        if (A.getDaixiu() != null) {
                            Daixiu5 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu5 = 0;
                        }
                    }
                    String months7;
                    String years7;
                    if (cal.get(cal.MONTH) == 12) {
                        months7 = String.valueOf(cal.get(cal.MONTH) - 10);
                        years7 = String.valueOf(cal.get(cal.YEAR) + 1);
                    } else if (cal.get(cal.MONTH) == 11) {
                        months7 = String.valueOf(cal.get(cal.MONTH) - 10);
                        years7 = String.valueOf(cal.get(cal.YEAR) + 1);
                    } else {
                        months7 = String.valueOf(cal.get(cal.MONTH) + 2);
                        years7 = String.valueOf(cal.get(cal.YEAR));
                    }
                    List<Attendance> attendanceList7 = givingMapper.selectAttendance(user_id, years7, months7);
                    for (Attendance A : attendanceList7) {
                        if (A.getDaixiu() != null) {
                            Daixiu6 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu6 = 0;
                        }
                    }
                    String months8;
                    String years8;
                    if (cal.get(cal.MONTH) == 12) {
                        months8 = String.valueOf(cal.get(cal.MONTH) - 9);
                        years8 = String.valueOf(cal.get(cal.YEAR) + 1);
                    } else if (cal.get(cal.MONTH) == 11) {
                        months8 = String.valueOf(cal.get(cal.MONTH) - 9);
                        years8 = String.valueOf(cal.get(cal.YEAR) + 1);
                    } else if (cal.get(cal.MONTH) == 10) {
                        months8 = String.valueOf(cal.get(cal.MONTH) - 9);
                        years8 = String.valueOf(cal.get(cal.YEAR) + 1);
                    } else {
                        months8 = String.valueOf(cal.get(cal.MONTH) + 3);
                        years8 = String.valueOf(cal.get(cal.YEAR));
                    }
                    List<Attendance> attendanceList8 = givingMapper.selectAttendance(user_id, years8, months8);
                    for (Attendance A : attendanceList8) {
                        if (A.getDaixiu() != null) {
                            Daixiu7 = Integer.parseInt(A.getDaixiu());
                        } else {
                            Daixiu7 = 0;
                        }
                    }
                    int Thisreplace3 = (z * 8 - (Daixiu6 + Daixiu5 + Daixiu7)) +
                            (n * 8 - (Daixiu1 + Daixiu2 + Daixiu4)) +
                            (m * 8 - (Daixiu1 + Daixiu5 + Daixiu4)) +
                            (o * 8 - (Daixiu6 + Daixiu5 + Daixiu4));
                    residual.setThisreplace(String.valueOf(i * 8 - (Daixiu1 + Daixiu2 + Daixiu3)));
                    residual.setThisreplace3(String.valueOf(Thisreplace3));
                    int Ordinaryindustry = 0;
                    int Weekendindustry = 0;
                    int Statutoryresidue = 0;
                    int Ordinaryindustrynight = 0;
                    int Weekendindustrynight = 0;
                    int Statutoryresiduenight = 0;
                    if (attendance.getOrdinaryindustry() == null) {
                        Ordinaryindustry = 0;
                    } else {
                        Ordinaryindustry = Integer.parseInt(attendance.getOrdinaryindustry());
                    }
                    if (attendance.getWeekendindustry() == null) {
                        Weekendindustry = 0;
                    } else {
                        Weekendindustry = Integer.parseInt(attendance.getWeekendindustry());
                    }
                    if (attendance.getStatutoryresidue() == null) {
                        Statutoryresidue = 0;
                    } else {
                        Statutoryresidue = Integer.parseInt(attendance.getStatutoryresidue());
                    }
                    if (attendance.getOrdinaryindustrynight() == null) {
                        Ordinaryindustrynight = 0;
                    } else {
                        Ordinaryindustrynight = Integer.parseInt(attendance.getOrdinaryindustrynight());
                    }
                    if (attendance.getWeekendindustrynight() == null) {
                        Weekendindustrynight = 0;
                    } else {
                        Weekendindustrynight = Integer.parseInt(attendance.getWeekendindustrynight());
                    }
                    if (attendance.getStatutoryresiduenight() == null) {
                        Statutoryresiduenight = 0;
                    } else {
                        Statutoryresiduenight = Integer.parseInt(attendance.getStatutoryresiduenight());
                    }
                    int Thistotalh = Ordinaryindustry + Weekendindustry + Statutoryresidue + Ordinaryindustrynight + Weekendindustrynight + Statutoryresiduenight + (i * 8 - (Daixiu1 + Daixiu2 + Daixiu3)) + Thisreplace3;
                    residual.setThistotalh(String.valueOf(Thistotalh));
                    int after = 0;
                    int after3 = 0;
                    Query query = new Query();
                    String XDate;
                    String XMonths;
                    String XYears;
                    if (cal.get(cal.MONTH) == 1) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 2) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 3) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 4) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else {
                        XMonths = String.valueOf(cal.get(cal.MONTH) - 4);
                        XYears  = String.valueOf(cal.get(cal.YEAR));
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    }
                    String XData;
                    if(Integer.parseInt(XMonths)<10 && Integer.parseInt(XDate)<10 ){
                        XData = XYears + "-0" + XMonths + "-0" + XDate;
                    }else if(Integer.parseInt(XMonths)<10){
                        XData = XYears + "-0" + XMonths + "-" + XDate;
                    }else if(Integer.parseInt(XDate)<10){
                        XData = XYears + "-" + XMonths + "-0" + XDate;
                    }else{
                        XData = XYears + "-" + XMonths + "-" + XDate;
                    }
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    String Date = sf.format(new Date());
                    query.addCriteria(Criteria.where("userid").is(attendance.getUser_id()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        if(customerInfo.getUserinfo().getGridData()!=null){
                            List<CustomerInfo.Personal> customerInfo1 = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                            for (CustomerInfo.Personal personal : customerInfo1) {
                                if (Integer.parseInt(personal.getDate().replace("-","")) <= Integer.parseInt(Date.replace("-",""))) {
                                    after = Integer.parseInt(personal.getAfter());
                                    break;
                                }
                            }
                            for (CustomerInfo.Personal personal : customerInfo1) {
                                if (Integer.parseInt(personal.getDate().replace("-","")) <= Integer.parseInt(XData.replace("-",""))) {
                                    after3 = Integer.parseInt(personal.getAfter());
                                    break;
                                }
                            }
                        }else{
                            after = 0;
                            after3= 0;
                        }
                    }
                    one = Double.valueOf(Ordinaryindustry * 1.5 + Ordinaryindustrynight * 1.5 * 1.25);
                    two = Double.valueOf(Weekendindustry * 2 + Weekendindustrynight * 2 * 1.25);
                    three = Double.valueOf(Statutoryresidue * 3 + Statutoryresiduenight * 3 * 1.25)*(after/21.75/8);
                    four = Double.valueOf((i * 8 - (Daixiu1 + Daixiu2 + Daixiu3)) * 2)*(after3/21.75/8);
                    Thistotaly = one + two + three + four;
                    residual.setThistotaly(df.format(Thistotaly));
                    String months10 = String.valueOf(cal.get(cal.MONTH) - 1);
                    List<Attendance> AttendanceList1 = givingMapper.selectAttendance(user_id, years, months10);
                    for (Attendance Attendance1 : AttendanceList1) {
                        residual.setLastweekdays(Attendance1.getOrdinaryindustry());
                        residual.setLastrestDay(Attendance1.getWeekendindustry());
                        residual.setLastlegal(Attendance1.getStatutoryresidue());
                        residual.setLastlatenight(Attendance1.getOrdinaryindustrynight());
                        residual.setLastrestlatenight(Attendance1.getWeekendindustrynight());
                        residual.setLastlegallatenight(Attendance1.getStatutoryresiduenight());
                        int Xi = 0;
                        int Xo = 0;
                        int Xm = 0;
                        int Xn = 0;
                        int XDaixiu1 = 0;
                        int XDaixiu2 = 0;
                        int XDaixiu3 = 0;
                        String Xmonths1;
                        String Xyears1;
                        if (cal.get(cal.MONTH) == 1) {
                            Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else if (cal.get(cal.MONTH) == 2) {
                            Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else if (cal.get(cal.MONTH) == 3) {
                            Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else if (cal.get(cal.MONTH) == 4) {
                            Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else {
                            Xmonths1 = String.valueOf(cal.get(cal.MONTH) - 4);
                            Xyears1 = String.valueOf(cal.get(cal.YEAR));
                        }
                        Attendance atten1 = new Attendance();
                        atten1.setUser_id(Attendance1.getUser_id());
                        atten1.setYears(Xyears1);
                        atten1.setMonths(Xmonths1);
                        List<Attendance> Attendancelist11 = attendanceMapper.select(atten1);
                        for (Attendance attendancelist : Attendancelist11) {
                            if (Integer.parseInt(attendancelist.getWeekendindustry()) >= 8) {
                                Xi = Xi + 1;
                            }
                        }
                        String Xmonths2;
                        String Xyears2;
                        if (cal.get(cal.MONTH) == 1) {
                            Xmonths2 = String.valueOf(cal.get(cal.MONTH) + 11);
                            Xyears2 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else {
                            Xmonths2 = String.valueOf(cal.get(cal.MONTH) - 1);
                            Xyears2 = String.valueOf(cal.get(cal.YEAR));
                        }
                        Attendance Xa = new Attendance();
                        Xa.setUser_id(Attendance1.getUser_id());
                        Xa.setYears(Xyears2);
                        Xa.setMonths(Xmonths2);
                        List<Attendance> XAttendancelist1 = attendanceMapper.select(Xa);
                        for (Attendance attendancelist1 : XAttendancelist1) {
                            if (Integer.parseInt(attendancelist1.getWeekendindustry()) >= 8) {
                                Xo = Xo + 1;
                            }
                        }
                        List<Attendance> XattendanceList = givingMapper.selectAttendance(user_id, Xyears2, Xmonths2);
                        for (Attendance A : XattendanceList) {
                            if (A.getDaixiu() != null) {
                                XDaixiu1 = Integer.parseInt(A.getDaixiu());
                            } else {
                                XDaixiu1 = 0;
                            }
                        }
                        String Xmonths3;
                        String Xyears3;
                        if (cal.get(cal.MONTH) == 1) {
                            Xmonths3 = String.valueOf(cal.get(cal.MONTH) + 10);
                            Xyears3 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else if (cal.get(cal.MONTH) == 2) {
                            Xmonths3 = String.valueOf(cal.get(cal.MONTH) + 10);
                            Xyears3 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else {
                            Xmonths3 = String.valueOf(cal.get(cal.MONTH) - 2);
                            Xyears3 = String.valueOf(cal.get(cal.YEAR));
                        }
                        Attendance Xa1 = new Attendance();
                        Xa1.setUser_id(Attendance1.getUser_id());
                        Xa1.setYears(Xyears3);
                        Xa1.setMonths(Xmonths3);
                        List<Attendance> XAttendancelist2 = attendanceMapper.select(Xa1);
                        for (Attendance attendancelist2 : XAttendancelist2) {
                            if (Integer.parseInt(attendancelist2.getWeekendindustry()) >= 8) {
                                Xm = Xm + 1;
                            }
                        }
                        List<Attendance> XattendanceList1 = givingMapper.selectAttendance(user_id, Xyears3, Xmonths3);
                        for (Attendance A : XattendanceList1) {
                            if (A.getDaixiu() != null) {
                                XDaixiu2 = Integer.parseInt(A.getDaixiu());
                            } else {
                                XDaixiu2 = 0;
                            }
                        }
                        String Xmonths4;
                        String Xyears4;
                        if (cal.get(cal.MONTH) == 1) {
                            Xmonths4 = String.valueOf(cal.get(cal.MONTH) + 9);
                            Xyears4 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else if (cal.get(cal.MONTH) == 2) {
                            Xmonths4 = String.valueOf(cal.get(cal.MONTH) + 9);
                            Xyears4 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else if (cal.get(cal.MONTH) == 3) {
                            Xmonths4 = String.valueOf(cal.get(cal.MONTH) + 9);
                            Xyears4 = String.valueOf(cal.get(cal.YEAR) - 1);
                        } else {
                            Xmonths4 = String.valueOf(cal.get(cal.MONTH) - 3);
                            Xyears4 = String.valueOf(cal.get(cal.YEAR));
                        }
                        Attendance Xa2 = new Attendance();
                        Xa2.setUser_id(Attendance1.getUser_id());
                        Xa2.setYears(Xyears4);
                        Xa2.setMonths(Xmonths4);
                        List<Attendance> XAttendancelist3 = attendanceMapper.select(Xa2);
                        for (Attendance attendancelist3 : XAttendancelist3) {
                            if (Integer.parseInt(attendancelist3.getWeekendindustry()) >= 8) {
                                Xn = Xn + 1;
                            }
                        }
                        List<Attendance> XattendanceList2 = givingMapper.selectAttendance(user_id, Xyears4, Xmonths4);
                        for (Attendance A : XattendanceList2) {
                            if (A.getDaixiu() != null) {
                                XDaixiu3 = Integer.parseInt(A.getDaixiu());
                            } else {
                                XDaixiu3 = 0;
                            }
                        }
                        residual.setLastreplace(String.valueOf(Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)));
                        int XOrdinaryindustry = 0;
                        int XWeekendindustry = 0;
                        int XStatutoryresidue = 0;
                        int XOrdinaryindustrynight = 0;
                        int XWeekendindustrynight = 0;
                        int XStatutoryresiduenight = 0;
                        if (Attendance1.getOrdinaryindustry() == null) {
                            XOrdinaryindustry = 0;
                        } else {
                            XOrdinaryindustry = Integer.parseInt(Attendance1.getOrdinaryindustry());
                        }
                        if (Attendance1.getWeekendindustry() == null) {
                            XWeekendindustry = 0;
                        } else {
                            XWeekendindustry = Integer.parseInt(Attendance1.getWeekendindustry());
                        }
                        if (Attendance1.getStatutoryresidue() == null) {
                            XStatutoryresidue = 0;
                        } else {
                            XStatutoryresidue = Integer.parseInt(Attendance1.getStatutoryresidue());
                        }
                        if (Attendance1.getOrdinaryindustrynight() == null) {
                            XOrdinaryindustrynight = 0;
                        } else {
                            XOrdinaryindustrynight = Integer.parseInt(Attendance1.getOrdinaryindustrynight());
                        }
                        if (Attendance1.getWeekendindustrynight() == null) {
                            XWeekendindustrynight = 0;
                        } else {
                            XWeekendindustrynight = Integer.parseInt(Attendance1.getWeekendindustrynight());
                        }
                        if (Attendance1.getStatutoryresiduenight() == null) {
                            XStatutoryresiduenight = 0;
                        } else {
                            XStatutoryresiduenight = Integer.parseInt(Attendance1.getStatutoryresiduenight());
                        }
                        int XLasttotalh = XOrdinaryindustry + XWeekendindustry + XStatutoryresidue + XOrdinaryindustrynight + XWeekendindustrynight + XStatutoryresiduenight + (Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3));
                        residual.setLasttotalh(String.valueOf(XLasttotalh));


                        int After = 0;
                        int After3 = 0;
                        Query query1 = new Query();
                        String XDate1;
                        String XMonths1;
                        String XYears1;
                        if (cal.get(cal.MONTH) == 1) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1  = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 2) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1  = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 3) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1  = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 4) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1  = String.valueOf(cal.get(cal.DATE));
                        } else {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) - 4);
                            XYears1  = String.valueOf(cal.get(cal.YEAR));
                            XDate1  = String.valueOf(cal.get(cal.DATE));
                        }
                        String XData1;
                        if(Integer.parseInt(XMonths)<10 && Integer.parseInt(XDate)<10 ){
                            XData1 = XYears + "-0" + XMonths + "-0" + XDate;
                        }else if(Integer.parseInt(XMonths)<10){
                            XData1 = XYears + "-0" + XMonths + "-" + XDate;
                        }else if(Integer.parseInt(XDate)<10){
                            XData1 = XYears + "-" + XMonths + "-0" + XDate;
                        }else{
                            XData1 = XYears + "-" + XMonths + "-" + XDate;
                        }
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String Date1 = sf1.format(new Date());
                        query1.addCriteria(Criteria.where("userid").is(attendance.getUser_id()));
                        CustomerInfo customerInfo1 = mongoTemplate.findOne(query1, CustomerInfo.class);
                        if (customerInfo1 != null) {
                            if(customerInfo.getUserinfo().getGridData()!=null) {
                                List<CustomerInfo.Personal> customerInfoList = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                                for (CustomerInfo.Personal personal : customerInfoList) {
                                    if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Date1.replace("-", ""))) {
                                        After = Integer.parseInt(personal.getAfter());
                                        break;
                                    }
                                }
                                for (CustomerInfo.Personal personal : customerInfoList) {
                                    if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(XData1.replace("-", ""))) {
                                        After3 = Integer.parseInt(personal.getAfter());
                                        break;
                                    }
                                }
                            }else{
                                After = 0;
                                After3= 0;
                            }
                        }
                        one = Double.valueOf(XOrdinaryindustry * 1.5 + XOrdinaryindustrynight * 1.5 * 1.25);
                        two = Double.valueOf(XWeekendindustry * 2 + XWeekendindustrynight * 2 * 1.25);
                        three = Double.valueOf(XStatutoryresidue * 3 + XStatutoryresiduenight * 3 * 1.25)*(After/21.75/8);;
                        four = Double.valueOf((Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)) * 2)*(After3/21.75/8);
                        Lasttotaly = one + two + three + four;
                        residual.setLasttotaly(df.format(Lasttotaly));
                    }
                    if (Lasttotaly + Thistotaly == 0.0) {
                        residual.setSubsidy(String.valueOf(Lasttotaly + Thistotaly));
                    } else {
                        residual.setSubsidy(df.format(Lasttotaly + Thistotaly));
                    }
                } else {
                    residual.setRemarks("-");
                    residual.setThistotaly("");
                    residual.setThistotalh("");
                    residual.setThisreplace3("");
                    residual.setThisreplace("");
                    residual.setThislegallatenight("");
                    residual.setThislegal("");
                    residual.setThislatenight("");
                    residual.setThisrestlatenight("");
                    residual.setThisrestDay("");
                    residual.setThisweekdays("");
                    residual.setLastweekdays(attendance.getOrdinaryindustry());
                    residual.setLastrestDay(attendance.getWeekendindustry());
                    residual.setLastlegal(attendance.getStatutoryresidue());
                    residual.setLastlatenight(attendance.getOrdinaryindustrynight());
                    residual.setLastrestlatenight(attendance.getWeekendindustrynight());
                    residual.setLastlegallatenight(attendance.getStatutoryresiduenight());
                    int i = 0;
                    int Xi = 0;
                    int Xo = 0;
                    int Xm = 0;
                    int Xn = 0;
                    int XDaixiu1 = 0;
                    int XDaixiu2 = 0;
                    int XDaixiu3 = 0;
                    String Xmonths1;
                    String Xyears1;
                    if (cal.get(cal.MONTH) == 1) {
                        Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 2) {
                        Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 3) {
                        Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 4) {
                        Xmonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                        Xyears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        Xmonths1 = String.valueOf(cal.get(cal.MONTH) - 4);
                        Xyears1 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance atten1 = new Attendance();
                    atten1.setUser_id(attendance.getUser_id());
                    atten1.setYears(Xyears1);
                    atten1.setMonths(Xmonths1);
                    List<Attendance> Attendancelist11 = attendanceMapper.select(atten1);
                    for (Attendance attendancelist : Attendancelist11) {
                        if (Integer.parseInt(attendancelist.getWeekendindustry()) >= 8) {
                            Xi = Xi + 1;
                        }
                    }
                    String Xmonths2 ;
                    String Xyears2 ;
                    if (cal.get(cal.MONTH) == 1) {
                        Xmonths2 = String.valueOf(cal.get(cal.MONTH) + 11);
                        Xyears2 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        Xmonths2 = String.valueOf(cal.get(cal.MONTH) - 1);
                        Xyears2 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance Xa = new Attendance();
                    Xa.setUser_id(attendance.getUser_id());
                    Xa.setYears(Xyears2);
                    Xa.setMonths(Xmonths2);
                    List<Attendance> XAttendancelist1 = attendanceMapper.select(Xa);
                    for (Attendance attendancelist1 : XAttendancelist1) {
                        if (Integer.parseInt(attendancelist1.getWeekendindustry()) >= 8) {
                            Xo = Xo + 1;
                        }
                    }
                    List<Attendance> XattendanceList = givingMapper.selectAttendance(user_id, Xyears2, Xmonths2);
                    for (Attendance A : XattendanceList) {
                        if (A.getDaixiu() != null) {
                            XDaixiu1 = Integer.parseInt(A.getDaixiu());
                        } else {
                            XDaixiu1 = 0;
                        }
                    }
                    String Xmonths3;
                    String Xyears3;
                    if (cal.get(cal.MONTH) == 1) {
                        Xmonths3 = String.valueOf(cal.get(cal.MONTH) + 10);
                        Xyears3 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 2) {
                        Xmonths3 = String.valueOf(cal.get(cal.MONTH) + 10);
                        Xyears3 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        Xmonths3 = String.valueOf(cal.get(cal.MONTH) - 2);
                        Xyears3 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance Xa1 = new Attendance();
                    Xa1.setUser_id(attendance.getUser_id());
                    Xa1.setYears(Xyears3);
                    Xa1.setMonths(Xmonths3);
                    List<Attendance> XAttendancelist2 = attendanceMapper.select(Xa1);
                    for (Attendance attendancelist2 : XAttendancelist2) {
                        if (Integer.parseInt(attendancelist2.getWeekendindustry()) >= 8) {
                            Xm = Xm + 1;
                        }
                    }
                    List<Attendance> XattendanceList1 = givingMapper.selectAttendance(user_id, Xyears3, Xmonths3);
                    for (Attendance A : XattendanceList1) {
                        if (A.getDaixiu() != null) {
                            XDaixiu2 = Integer.parseInt(A.getDaixiu());
                        } else {
                            XDaixiu2 = 0;
                        }
                    }
                    String Xmonths4;
                    String Xyears4;
                    if (cal.get(cal.MONTH) == 1) {
                        Xmonths4 = String.valueOf(cal.get(cal.MONTH) + 9);
                        Xyears4 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 2) {
                        Xmonths4 = String.valueOf(cal.get(cal.MONTH) + 9);
                        Xyears4 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else if (cal.get(cal.MONTH) == 3) {
                        Xmonths4 = String.valueOf(cal.get(cal.MONTH) + 9);
                        Xyears4 = String.valueOf(cal.get(cal.YEAR) - 1);
                    } else {
                        Xmonths4 = String.valueOf(cal.get(cal.MONTH) - 3);
                        Xyears4 = String.valueOf(cal.get(cal.YEAR));
                    }
                    Attendance Xa2 = new Attendance();
                    Xa2.setUser_id(attendance.getUser_id());
                    Xa2.setYears(Xyears4);
                    Xa2.setMonths(Xmonths4);
                    List<Attendance> XAttendancelist3 = attendanceMapper.select(Xa2);
                    for (Attendance attendancelist3 : XAttendancelist3) {
                        if (Integer.parseInt(attendancelist3.getWeekendindustry()) >= 8) {
                            Xn = Xn + 1;
                        }
                    }
                    List<Attendance> XattendanceList2 = givingMapper.selectAttendance(user_id, Xyears4, Xmonths4);
                    for (Attendance A : XattendanceList2) {
                        if (A.getDaixiu() != null) {
                            XDaixiu3 = Integer.parseInt(A.getDaixiu());
                        } else {
                            XDaixiu3 = 0;
                        }
                    }
                    residual.setLastreplace(String.valueOf(Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)));
                    int XOrdinaryindustry = 0;
                    int XWeekendindustry = 0;
                    int XStatutoryresidue = 0;
                    int XOrdinaryindustrynight = 0;
                    int XWeekendindustrynight = 0;
                    int XStatutoryresiduenight = 0;
                    if (attendance.getOrdinaryindustry() == null) {
                        XOrdinaryindustry = 0;
                    } else {
                        XOrdinaryindustry = Integer.parseInt(attendance.getOrdinaryindustry());
                    }
                    if (attendance.getWeekendindustry() == null) {
                        XWeekendindustry = 0;
                    } else {
                        XWeekendindustry = Integer.parseInt(attendance.getWeekendindustry());
                    }
                    if (attendance.getStatutoryresidue() == null) {
                        XStatutoryresidue = 0;
                    } else {
                        XStatutoryresidue = Integer.parseInt(attendance.getStatutoryresidue());
                    }
                    if (attendance.getOrdinaryindustrynight() == null) {
                        XOrdinaryindustrynight = 0;
                    } else {
                        XOrdinaryindustrynight = Integer.parseInt(attendance.getOrdinaryindustrynight());
                    }
                    if (attendance.getWeekendindustrynight() == null) {
                        XWeekendindustrynight = 0;
                    } else {
                        XWeekendindustrynight = Integer.parseInt(attendance.getWeekendindustrynight());
                    }
                    if (attendance.getStatutoryresiduenight() == null) {
                        XStatutoryresiduenight = 0;
                    } else {
                        XStatutoryresiduenight = Integer.parseInt(attendance.getStatutoryresiduenight());
                    }
                    int XLasttotalh = XOrdinaryindustry + XWeekendindustry + XStatutoryresidue + XOrdinaryindustrynight + XWeekendindustrynight + XStatutoryresiduenight + (Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3));
                    residual.setLasttotalh(String.valueOf(XLasttotalh));

                    int after = 0;
                    int after3 = 0;
                    Query query = new Query();
                    String XDate;
                    String XMonths;
                    String XYears;
                    if (cal.get(cal.MONTH) == 1) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 2) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 3) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 4) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    } else {
                        XMonths = String.valueOf(cal.get(cal.MONTH) - 4);
                        XYears  = String.valueOf(cal.get(cal.YEAR));
                        XDate  = String.valueOf(cal.get(cal.DATE));
                    }
                    String XData;
                    if(Integer.parseInt(XMonths)<10 && Integer.parseInt(XDate)<10 ){
                        XData = XYears + "-0" + XMonths + "-0" + XDate;
                    }else if(Integer.parseInt(XMonths)<10){
                        XData = XYears + "-0" + XMonths + "-" + XDate;
                    }else if(Integer.parseInt(XDate)<10){
                        XData = XYears + "-" + XMonths + "-0" + XDate;
                    }else{
                        XData = XYears + "-" + XMonths + "-" + XDate;
                    }
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    String Date = sf.format(new Date());
                    query.addCriteria(Criteria.where("userid").is(attendance.getUser_id()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        if(customerInfo.getUserinfo().getGridData()!=null) {
                            List<CustomerInfo.Personal> customerInfo1 = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                            for (CustomerInfo.Personal personal : customerInfo1) {
                                if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Date.replace("-", ""))) {
                                    after = Integer.parseInt(personal.getAfter());
                                    break;
                                }
                            }
                            for (CustomerInfo.Personal personal : customerInfo1) {
                                if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(XData.replace("-", ""))) {
                                    after3 = Integer.parseInt(personal.getAfter());
                                    break;
                                }
                            }
                        }else{
                            after = 0 ;
                            after3 = 0;
                        }
                    }
                    one = Double.valueOf(XOrdinaryindustry * 1.5 + XOrdinaryindustrynight * 1.5 * 1.25);
                    two = Double.valueOf(XWeekendindustry * 2 + XWeekendindustrynight * 2 * 1.25);
                    three = Double.valueOf(XStatutoryresidue * 3 + XStatutoryresiduenight * 3 * 1.25)*(after/21.75/8);
                    four = Double.valueOf((Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)) * 2)*(after3/21.75/8);
                    Lasttotaly = one + two + three + four;
                    residual.setLasttotaly(df.format(Lasttotaly));
                    if (Lasttotaly + Thistotaly == 0.0) {
                        residual.setSubsidy(String.valueOf(Lasttotaly + Thistotaly));
                    } else {
                        residual.setSubsidy(df.format(Lasttotaly + Thistotaly));
                    }
                }
                residual.preInsert(tokenModel);
                Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(user_id));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    residual.setJobnumber(customerInfo.getUserinfo().getJobnumber());
                }
                residualMapper.insert(residual);
            }
        }

    }

    @Override
    public void insertAttendance(String givingid, TokenModel tokenModel) throws Exception {
        Lackattendance lackattendance = new Lackattendance();
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baselist = baseMapper.select(base);
        for (Base b : baselist) {
            Calendar cal = Calendar.getInstance();
            String years = String.valueOf(cal.get(cal.YEAR));
            String months = String.valueOf(cal.get(cal.MONTH));
            String user_id = b.getUser_id();
            List<Attendance> AttendanceList = givingMapper.selectAttendance(user_id, years, months);
            int rowundex = 0;
            double one = 0d;
            double two = 0d;
            double three = 0d;
            double Lasttotal = 0d;
            double thistotal = 0d;
            for (Attendance attendance : AttendanceList) {
                rowundex = rowundex + 1;
                lackattendance.setRowindex(rowundex);
                lackattendance.setLackattendance_id(UUID.randomUUID().toString());
                lackattendance.setGiving_id(givingid);
                lackattendance.setUser_id(attendance.getUser_id());
                Retire retire = new Retire();
                retire.setUser_id(attendance.getUser_id());
                List<Retire> retirelist = retireMapper.select(retire);
                DecimalFormat df = new DecimalFormat(".00");
                if (retirelist.size() != 0) {
                    lackattendance.setRemarks("退职");
                    lackattendance.setThisshortdeficiency(attendance.getShortsickleave());
                    lackattendance.setThischronicdeficiency(attendance.getLongsickleave());
                    Base ba = new Base();
                    ba.setUser_id(attendance.getUser_id());
                    List<Base> Baselist = baseMapper.select(ba);
                    for (Base B : Baselist) {
                        int Late = 0;
                        int Leaveearly = 0;
                        int Absenteeism = 0;
                        int Thismonth = 0;
                        if (B.getThismonth() == null) {
                            Thismonth = 0;
                        } else {
                            Thismonth = Integer.parseInt(B.getThismonth());
                        }
                        if (attendance.getLate() == null) {
                            Late = 0;
                        } else {
                            Late = Integer.parseInt(attendance.getLate());
                        }
                        if (attendance.getLeaveearly() == null) {
                            Leaveearly = 0;
                        } else {
                            Leaveearly = Integer.parseInt(attendance.getLeaveearly());
                        }
                        if (attendance.getAbsenteeism() == null) {
                            Absenteeism = 0;
                        } else {
                            Absenteeism = Integer.parseInt(attendance.getAbsenteeism());
                        }
                        if (attendance.getShortsickleave() == null) {
                            two = 0;
                        } else {
                            two = Double.valueOf(Thismonth / 21.75 / 8 * 0.4 * Integer.parseInt(attendance.getShortsickleave()));
                        }
                        if (attendance.getLongsickleave() == null) {
                            three = 0;
                        } else {
                            three = Double.valueOf(1620 / 21.75 / 8 * Integer.parseInt(attendance.getLongsickleave()));
                        }

                        int Thisdiligence = Late + Leaveearly + Absenteeism;
                        lackattendance.setThisdiligence(String.valueOf(Thisdiligence));
                        one = Double.valueOf(Thisdiligence * Thismonth / 21.75 / 8);
                        thistotal = one - two - three;
                        lackattendance.setThistotal(df.format(thistotal));
                    }
                } else {
                    lackattendance.setRemarks("-");
                    lackattendance.setThistotal("");
                    lackattendance.setThischronicdeficiency("");
                    lackattendance.setThisshortdeficiency("");
                    lackattendance.setThisdiligence("");
                    lackattendance.setLastshortdeficiency(attendance.getShortsickleave());
                    lackattendance.setLastchronicdeficiency(attendance.getLongsickleave());
                    Base ba = new Base();
                    ba.setUser_id(attendance.getUser_id());
                    List<Base> Baselist = baseMapper.select(ba);
                    for (Base B : Baselist) {
                        int Late = 0;
                        int Leaveearly = 0;
                        int Absenteeism = 0;
                        int Thismonth = 0;
                        if (B.getThismonth() == null) {
                            Thismonth = 0;
                        } else {
                            Thismonth = Integer.parseInt(B.getThismonth());
                        }
                        if (attendance.getLate() == null) {
                            Late = 0;
                        } else {
                            Late = Integer.parseInt(attendance.getLate());
                        }
                        if (attendance.getLeaveearly() == null) {
                            Leaveearly = 0;
                        } else {
                            Leaveearly = Integer.parseInt(attendance.getLeaveearly());
                        }
                        if (attendance.getAbsenteeism() == null) {
                            Absenteeism = 0;
                        } else {
                            Absenteeism = Integer.parseInt(attendance.getAbsenteeism());
                        }
                        if (attendance.getShortsickleave() == null) {
                            two = 0;
                        } else {
                            two = Double.valueOf(Thismonth / 21.75 / 8 * 0.4 * Integer.parseInt(attendance.getShortsickleave()));
                        }
                        if (attendance.getLongsickleave() == null) {
                            three = 0;
                        } else {
                            three = Double.valueOf(1620 / 21.75 / 8 * Integer.parseInt(attendance.getLongsickleave()));
                        }

                        int Lastdiligence = Late + Leaveearly + Absenteeism;
                        lackattendance.setLastdiligence(String.valueOf(Lastdiligence));
                        one = Double.valueOf(Lastdiligence * Thismonth / 21.75 / 8);
                        Lasttotal = one - two - three;
                        lackattendance.setLasttotal(df.format(Lasttotal));
                    }
                }
                if (thistotal + Lasttotal == 0.0) {
                    lackattendance.setGive(String.valueOf(thistotal + Lasttotal));
                } else {
                    lackattendance.setGive(df.format(thistotal + Lasttotal));
                }
                lackattendance.preInsert(tokenModel);
                Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(user_id));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    lackattendance.setJobnumber(customerInfo.getUserinfo().getJobnumber());
                }
                lackattendanceMapper.insert(lackattendance);
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
        List<OtherTwo2> otherTwo2List = givingMapper.selectOthertwo(givingid);
        if (otherTwo2List.size() > 0) {
            for (OtherTwo2 otherTwo2 : otherTwo2List) {
                otherTwo2.preInsert(tokenModel);
                otherTwo2.setUser_id(otherTwo2.getUser_id());
                otherTwo2.setMoneys(otherTwo2.getMoneys());
                otherTwo2.setOthertwo2_id(UUID.randomUUID().toString());
                othertwo2Mapper.insert(otherTwo2);
            }
        }
    }

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public void insertContrast(String givingid, TokenModel tokenModel) throws Exception {
        Contrast contrast = new Contrast();
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baselist = baseMapper.select(base);
        if (baselist != null) {
            int rowindex = 0;
            for (Base base1 : baselist) {
                rowindex = rowindex + 1;
                String consrastid = UUID.randomUUID().toString();
                contrast.preInsert(tokenModel);
                contrast.setGiving_id(base1.getGiving_id());
                contrast.setContrast_id(consrastid);
                contrast.setUser_id(base1.getUser_id());
                contrast.setOwner(base1.getUser_id());
                contrast.setRowindex(rowindex);
                contrast.setDepartment_id(base1.getDepartment_id());

                Wages wages = new Wages();
                wages.setUser_id(base1.getUser_id());
                List<Wages> wageslist = wagesMapper.select(wages);
                if (wageslist != null) {
                    for (Wages wa : wageslist) {

                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM");
                        String strTemp = sf1.format(new Date());
                        String strTemp1 = sf1.format(wa.getCreateon());

                        Date delDate = sf1.parse(strTemp);
                        Calendar c = Calendar.getInstance();
                        c.setTime(delDate);
                        c.add(Calendar.MONTH, -1);

                        String year1 = String.valueOf(c.get(Calendar.YEAR));    //获取年
                        String month1 = String.valueOf(c.get(Calendar.MONTH) + 1);
                        String aa = year1 + "-" + month1;
                        if (strTemp.equals(strTemp1)) {
                            contrast.setThismonth(wa.getRealwages());
                        } else if (strTemp1.equals(aa)) {
                            contrast.setLastmonth(wa.getRealwages());
                        }
                    }
                }
                contrastMapper.insertSelective(contrast);
            }
        }
    }

    @Override
    public void insert(String generation, TokenModel tokenModel) throws Exception {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");

        Giving giving = new Giving();
        String strTemp = sf1.format(new Date());
        giving.setMonths(strTemp);
        List<Giving> givinglist = givingMapper.select(giving);
        Base base = new Base();
        Contrast contrast = new Contrast();
        OtherOne otherOne = new OtherOne();
        if (givinglist.size() != 0) {
            base.setGiving_id(givinglist.get(0).getGiving_id());
            baseMapper.delete(base);
            contrast.setGiving_id(givinglist.get(0).getGiving_id());
            contrastMapper.delete(contrast);

            otherOne.setGiving_id(givinglist.get(0).getGiving_id());
            otherOneMapper.delete(otherOne);
        }
        giving = new Giving();
        giving.setMonths(strTemp);
        givingMapper.delete(giving);

        String givingid = UUID.randomUUID().toString();
        giving = new Giving();
        giving.preInsert(tokenModel);
        giving.setGiving_id(givingid);
        giving.setGeneration(generation);
        giving.setGenerationdate(new Date());
        giving.setMonths(sf1.format(new Date()));

        givingMapper.insert(giving);
        insertBase(givingid, tokenModel);
        insertContrast(givingid, tokenModel);
        insertOtherTwo(givingid, tokenModel);
        insertOtherOne(givingid, tokenModel);
        insertAttendance(givingid, tokenModel);
        insertResidual(givingid, tokenModel);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }

    @Override
    public void save(GivingVo givingvo, TokenModel tokenModel) throws Exception {
        if (givingvo.getStrFlg().equals("16")) {
            List<Contrast> contrastlist = givingvo.getContrast();
            if (contrastlist != null) {
                for (Contrast contrast : contrastlist) {
                    contrast.preUpdate(tokenModel);
                    contrastMapper.updateByPrimaryKeySelective(contrast);
                }
            }
        } else if (givingvo.getStrFlg().equals("2")) {
            List<OtherOne> otheronelist = givingvo.getOtherOne();
            if (otheronelist != null) {
                for (OtherOne otherOne : otheronelist) {
                    otherOne.preUpdate(tokenModel);
                    otherOneMapper.updateByPrimaryKeySelective(otherOne);
                }
            }
        } else if (givingvo.getStrFlg().equals("3")) {
            List<OtherTwo> otherTwolist = givingvo.getOtherTwo();
            if (otherTwolist != null) {
                for (OtherTwo othertwo : otherTwolist) {
                    othertwo.preUpdate(tokenModel);
                    othertwoMapper.updateByPrimaryKeySelective(othertwo);
                    List<OtherTwo2> otherTwo2List = givingMapper.selectOthertwo(othertwo.getGiving_id());
                    if (otherTwo2List.size() > 0) {
                        for (OtherTwo2 otherTwo2 : otherTwo2List) {
                            otherTwo2.preInsert(tokenModel);
                            otherTwo2.setUser_id(otherTwo2.getUser_id());
                            otherTwo2.setMoneys(otherTwo2.getMoneys());
                            otherTwo2.setOthertwo2_id(UUID.randomUUID().toString());
                            othertwo2Mapper.insert(otherTwo2);
                        }
                    }
                }
            }
        }
    }
}
