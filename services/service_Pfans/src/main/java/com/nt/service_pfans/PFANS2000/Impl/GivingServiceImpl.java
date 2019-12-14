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
import java.text.ParseException;
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

    @Autowired
    private InductionMapper inductionMapper;

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
                if (customer.getUserinfo().getEnddate() == null) {
                    base.setType("0");
                }
                if (customer.getUserinfo().getEnddate() != null) {
                    String date = customer.getUserinfo().getEnddate().substring(5, 7);
                    Calendar cal = Calendar.getInstance();
                    String months = String.valueOf(cal.get(cal.MONTH));
                    if (Integer.parseInt(date) >= Integer.parseInt(months)) {
                        base.setType("0");
                    }
                }
                if (customer.getUserinfo().getResignation_date() != null && customer.getUserinfo().getResignation_date().length() > 0) {
                    String date = customer.getUserinfo().getResignation_date().substring(5, 7);
                    Calendar cal = Calendar.getInstance();
                    String months = String.valueOf(cal.get(cal.MONTH));
                    if (Integer.parseInt(date) == Integer.parseInt(months)) {
                        base.setType("1");
                    }
                }
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String months = String.valueOf(cal.get(cal.MONTH));
                String years = String.valueOf(cal.get(cal.YEAR));
                String date = String.valueOf(cal.get(cal.DATE));
                String Data;
                if (Integer.parseInt(months) < 10 && Integer.parseInt(date) < 10) {
                    Data = years + "-0" + months + "-0" + date;
                } else if (Integer.parseInt(months) < 10) {
                    Data = years + "-0" + months + "-" + date;
                } else if (Integer.parseInt(date) < 10) {
                    Data = years + "-" + months + "-0" + date;
                } else {
                    Data = years + "-" + months + "-" + date;
                }

                AbNormal abNormal = new AbNormal();
                abNormal.setStatus("4");
                List<AbNormal> abNormalinfo = abNormalMapper.select(abNormal);
                if (abNormalinfo != null) {
                    for (AbNormal abNor : abNormalinfo) {
                        if (abNor.getErrortype().equals("PR013012")) {
                            String Occurrencedate = sf.format(abNor.getOccurrencedate());
                            String Finisheddate = sf.format(abNor.getFinisheddate());
                            if (Integer.parseInt(Occurrencedate.replace("-", "")) <= Integer.parseInt(Data.replace("-", "")) && Integer.parseInt(Data.replace("-", "")) <= Integer.parseInt(Finisheddate.replace("-", ""))) {
                                base.setType("2");
                            }
                        } else if (abNor.getErrortype().equals("PR013013")) {
                            String occurrencedate = sf.format(abNor.getOccurrencedate());
                            String finisheddate = sf.format(abNor.getFinisheddate());
                            if (Integer.parseInt(occurrencedate.replace("-", "")) <= Integer.parseInt(Data.replace("-", "")) && Integer.parseInt(Data.replace("-", "")) <= Integer.parseInt(finisheddate.replace("-", ""))) {
                                base.setType("3");
                            }
                        }
                    }
                }
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

                String Months;
                String Years;
                String Date;
                if (cal.get(cal.MONTH) == 12) {
                    Months = String.valueOf(cal.get(cal.MONTH) - 11);
                    Years = String.valueOf(cal.get(cal.YEAR) + 1);
                    Date = String.valueOf(cal.get(cal.DATE));
                } else {
                    Months = String.valueOf(cal.get(cal.MONTH) + 1);
                    Years = String.valueOf(cal.get(cal.YEAR));
                    Date = String.valueOf(cal.get(cal.DATE));
                }
                String Dat;
                if (Integer.parseInt(Months) < 10 && Integer.parseInt(Date) < 10) {
                    Dat = Years + "-0" + Months + "-0" + Date;
                } else if (Integer.parseInt(Months) < 10) {
                    Dat = Years + "-0" + Months + "-" + Date;
                } else if (Integer.parseInt(Date) < 10) {
                    Dat = Years + "-" + Months + "-0" + Date;
                } else {
                    Dat = Years + "-" + Months + "-" + Date;
                }

                double after6 = 0d;
                double after7 = 0d;
                Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(customer.getUserid()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    if (customerInfo.getUserinfo().getGridData() != null) {
                        List<CustomerInfo.Personal> customerInfo1 = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        for (CustomerInfo.Personal personal : customerInfo1) {
                            if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Data.replace("-", ""))) {
                                after6 = Double.valueOf(personal.getAfter());
                                break;
                            }
                        }
                        for (CustomerInfo.Personal personal : customerInfo1) {
                            if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Dat.replace("-", ""))) {
                                after7 = Double.valueOf(personal.getAfter());
                                break;
                            }
                        }
                    } else {
                        after6 = 0;
                        after7 = 0;
                    }
                }
                base.setLastmonth(String.valueOf(after6));
                base.setThismonth(String.valueOf(after7));
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
                baseMapper.insert(base);
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
            String months = String.valueOf(cal.get(cal.MONTH) + 1);
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
                    double Daixiu1 = 0d;
                    double Daixiu2 = 0d;
                    double Daixiu3 = 0d;
                    double Daixiu4 = 0d;
                    double Daixiu5 = 0d;
                    double Daixiu6 = 0d;
                    double Daixiu7 = 0d;
                    double Thisreplace3 = 0d;
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
                        if (Double.valueOf(attendancelist.getWeekendindustry()) >= 8.0) {
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
                        if (Double.valueOf(attendancelist1.getWeekendindustry()) >= 8.0) {
                            o = o + 1;
                        }
                    }
                    List<Attendance> attendanceList = givingMapper.selectAttendance(user_id, years, months2);
                    for (Attendance A : attendanceList) {
                        if (A.getDaixiu() != null) {
                            Daixiu1 = Double.valueOf(A.getDaixiu());
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
                        if (Double.valueOf(attendancelist2.getWeekendindustry()) >= 8.0) {
                            m = m + 1;
                        }
                    }
                    List<Attendance> attendanceList1 = givingMapper.selectAttendance(user_id, years3, months3);
                    for (Attendance A : attendanceList1) {
                        if (A.getDaixiu() != null) {
                            Daixiu2 = Double.valueOf(A.getDaixiu());
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
                        if (Double.valueOf(attendancelist3.getWeekendindustry()) >= 8.0) {
                            n = n + 1;
                        }
                    }
                    List<Attendance> attendanceList2 = givingMapper.selectAttendance(user_id, years4, months4);
                    for (Attendance A : attendanceList2) {
                        if (A.getDaixiu() != null) {
                            Daixiu3 = Double.valueOf(A.getDaixiu());
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
                        if (Double.valueOf(attendancelist4.getWeekendindustry()) >= 8.0) {
                            z = z + 1;
                        }
                    }
                    List<Attendance> attendanceList5 = givingMapper.selectAttendance(user_id, years, months5);
                    for (Attendance A : attendanceList5) {
                        if (A.getDaixiu() != null) {
                            Daixiu4 = Double.valueOf(A.getDaixiu());
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
                        if (Double.valueOf(attendancelist5.getWeekendindustry()) >= 8.0) {
                            k = k + 1;
                        }
                    }
                    List<Attendance> attendanceList6 = givingMapper.selectAttendance(user_id, years6, months6);
                    for (Attendance A : attendanceList6) {
                        if (A.getDaixiu() != null) {
                            Daixiu5 = Double.valueOf(A.getDaixiu());
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
                            Daixiu6 = Double.valueOf(A.getDaixiu());
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
                            Daixiu7 = Double.valueOf(A.getDaixiu());
                        } else {
                            Daixiu7 = 0;
                        }
                    }
                    Thisreplace3 = (z * 8 - (Daixiu6 + Daixiu5 + Daixiu7)) +
                            (n * 8 - (Daixiu1 + Daixiu2 + Daixiu4)) +
                            (m * 8 - (Daixiu1 + Daixiu5 + Daixiu4)) +
                            (o * 8 - (Daixiu6 + Daixiu5 + Daixiu4));
                    residual.setThisreplace(String.valueOf(i * 8 - (Daixiu1 + Daixiu2 + Daixiu3)));
                    residual.setThisreplace3(String.valueOf(Thisreplace3));
                    double Ordinaryindustry = 0d;
                    double Weekendindustry = 0d;
                    double Statutoryresidue = 0d;
                    double Ordinaryindustrynight = 0d;
                    double Weekendindustrynight = 0d;
                    double Statutoryresiduenight = 0d;
                    double Thistotalh = 0d;
                    if (attendance.getOrdinaryindustry() == null) {
                        Ordinaryindustry = 0;
                    } else {
                        Ordinaryindustry = Double.valueOf(attendance.getOrdinaryindustry());
                    }
                    if (attendance.getWeekendindustry() == null) {
                        Weekendindustry = 0;
                    } else {
                        Weekendindustry = Double.valueOf(attendance.getWeekendindustry());
                    }
                    if (attendance.getStatutoryresidue() == null) {
                        Statutoryresidue = 0;
                    } else {
                        Statutoryresidue = Double.valueOf(attendance.getStatutoryresidue());
                    }
                    if (attendance.getOrdinaryindustrynight() == null) {
                        Ordinaryindustrynight = 0;
                    } else {
                        Ordinaryindustrynight = Double.valueOf(attendance.getOrdinaryindustrynight());
                    }
                    if (attendance.getWeekendindustrynight() == null) {
                        Weekendindustrynight = 0;
                    } else {
                        Weekendindustrynight = Double.valueOf(attendance.getWeekendindustrynight());
                    }
                    if (attendance.getStatutoryresiduenight() == null) {
                        Statutoryresiduenight = 0;
                    } else {
                        Statutoryresiduenight = Double.valueOf(attendance.getStatutoryresiduenight());
                    }
                    Thistotalh = Ordinaryindustry + Weekendindustry + Statutoryresidue + Ordinaryindustrynight + Weekendindustrynight + Statutoryresiduenight + (i * 8 - (Daixiu1 + Daixiu2 + Daixiu3)) + Thisreplace3;
                    residual.setThistotalh(String.valueOf(Thistotalh));
                    double after = 0d;
                    double after3 = 0d;
                    Query query = new Query();
                    String XDate;
                    String XMonths;
                    String XYears;
                    if (cal.get(cal.MONTH) == 1) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 2) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 3) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate = String.valueOf(cal.get(cal.DATE));
                    } else if (cal.get(cal.MONTH) == 4) {
                        XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                        XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                        XDate = String.valueOf(cal.get(cal.DATE));
                    } else {
                        XMonths = String.valueOf(cal.get(cal.MONTH) - 4);
                        XYears = String.valueOf(cal.get(cal.YEAR));
                        XDate = String.valueOf(cal.get(cal.DATE));
                    }
                    String XData;
                    if (Integer.parseInt(XMonths) < 10 && Integer.parseInt(XDate) < 10) {
                        XData = XYears + "-0" + XMonths + "-0" + XDate;
                    } else if (Integer.parseInt(XMonths) < 10) {
                        XData = XYears + "-0" + XMonths + "-" + XDate;
                    } else if (Integer.parseInt(XDate) < 10) {
                        XData = XYears + "-" + XMonths + "-0" + XDate;
                    } else {
                        XData = XYears + "-" + XMonths + "-" + XDate;
                    }
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    String Date = sf.format(new Date());
                    query.addCriteria(Criteria.where("userid").is(attendance.getUser_id()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        if (customerInfo.getUserinfo().getGridData() != null) {
                            List<CustomerInfo.Personal> customerInfo1 = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                            for (CustomerInfo.Personal personal : customerInfo1) {
                                if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Date.replace("-", ""))) {
                                    after = Double.valueOf(personal.getAfter());
                                    break;
                                }
                            }
                            for (CustomerInfo.Personal personal : customerInfo1) {
                                if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(XData.replace("-", ""))) {
                                    after3 = Double.valueOf(personal.getAfter());
                                    break;
                                }
                            }
                        } else {
                            after = 0;
                            after3 = 0;
                        }
                    }
                    double weekendindustry = 0d;
                    double ordinaryindustry = 0d;
                    double statutoryresidue = 0d;
                    double SYJB = 0d;
                    Dictionary dictionary = new Dictionary();
                    List<Dictionary> dictionarylist = dictionaryMapper.select(dictionary);
                    for (Dictionary diction : dictionarylist) {
                        if (diction.getCode().equals("PR049006")) {
                            ordinaryindustry = Double.valueOf(diction.getValue2());
                        }
                        if (diction.getCode().equals("PR049007")) {
                            weekendindustry = Double.valueOf(diction.getValue2());
                        }
                        if (diction.getCode().equals("PR049008")) {
                            statutoryresidue = Double.valueOf(diction.getValue2());
                        }
                        if (diction.getCode().equals("PR049009")) {
                            SYJB = Double.valueOf(diction.getValue2());
                        }
                    }
                    one = Double.valueOf(Ordinaryindustry * ordinaryindustry + Ordinaryindustrynight * ordinaryindustry * SYJB);
                    two = Double.valueOf(Weekendindustry * weekendindustry + Weekendindustrynight * weekendindustry * SYJB);
                    three = Double.valueOf(Statutoryresidue * statutoryresidue + Statutoryresiduenight * statutoryresidue * SYJB) * (after / 21.75 / 8);
                    four = Double.valueOf((i * 8 - (Daixiu1 + Daixiu2 + Daixiu3)) * weekendindustry) * (after3 / 21.75 / 8);
                    Thistotaly = one + two + three + four;
                    residual.setThistotaly(df.format(Thistotaly));
                    String months10 = String.valueOf(cal.get(cal.MONTH));
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
                        double XDaixiu1 = 0d;
                        double XDaixiu2 = 0d;
                        double XDaixiu3 = 0d;
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
                            if (Double.valueOf(attendancelist.getWeekendindustry()) >= 8.0) {
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
                            if (Double.valueOf(attendancelist1.getWeekendindustry()) >= 8.0) {
                                Xo = Xo + 1;
                            }
                        }
                        List<Attendance> XattendanceList = givingMapper.selectAttendance(user_id, Xyears2, Xmonths2);
                        for (Attendance A : XattendanceList) {
                            if (A.getDaixiu() != null) {
                                XDaixiu1 = Double.valueOf(A.getDaixiu());
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
                            if (Double.valueOf(attendancelist2.getWeekendindustry()) >= 8.0) {
                                Xm = Xm + 1;
                            }
                        }
                        List<Attendance> XattendanceList1 = givingMapper.selectAttendance(user_id, Xyears3, Xmonths3);
                        for (Attendance A : XattendanceList1) {
                            if (A.getDaixiu() != null) {
                                XDaixiu2 = Double.valueOf(A.getDaixiu());
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
                            if (Double.valueOf(attendancelist3.getWeekendindustry()) >= 8.0) {
                                Xn = Xn + 1;
                            }
                        }
                        List<Attendance> XattendanceList2 = givingMapper.selectAttendance(user_id, Xyears4, Xmonths4);
                        for (Attendance A : XattendanceList2) {
                            if (A.getDaixiu() != null) {
                                XDaixiu3 = Double.valueOf(A.getDaixiu());
                            } else {
                                XDaixiu3 = 0;
                            }
                        }
                        residual.setLastreplace(String.valueOf(Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)));
                        double XOrdinaryindustry = 0d;
                        double XWeekendindustry = 0d;
                        double XStatutoryresidue = 0d;
                        double XOrdinaryindustrynight = 0d;
                        double XWeekendindustrynight = 0d;
                        double XStatutoryresiduenight = 0d;
                        double XLasttotalh = 0d;
                        if (Attendance1.getOrdinaryindustry() == null) {
                            XOrdinaryindustry = 0;
                        } else {
                            XOrdinaryindustry = Double.valueOf(Attendance1.getOrdinaryindustry());
                        }
                        if (Attendance1.getWeekendindustry() == null) {
                            XWeekendindustry = 0;
                        } else {
                            XWeekendindustry = Double.valueOf(Attendance1.getWeekendindustry());
                        }
                        if (Attendance1.getStatutoryresidue() == null) {
                            XStatutoryresidue = 0;
                        } else {
                            XStatutoryresidue = Double.valueOf(Attendance1.getStatutoryresidue());
                        }
                        if (Attendance1.getOrdinaryindustrynight() == null) {
                            XOrdinaryindustrynight = 0;
                        } else {
                            XOrdinaryindustrynight = Double.valueOf(Attendance1.getOrdinaryindustrynight());
                        }
                        if (Attendance1.getWeekendindustrynight() == null) {
                            XWeekendindustrynight = 0;
                        } else {
                            XWeekendindustrynight = Double.valueOf(Attendance1.getWeekendindustrynight());
                        }
                        if (Attendance1.getStatutoryresiduenight() == null) {
                            XStatutoryresiduenight = 0;
                        } else {
                            XStatutoryresiduenight = Double.valueOf(Attendance1.getStatutoryresiduenight());
                        }
                        XLasttotalh = XOrdinaryindustry + XWeekendindustry + XStatutoryresidue + XOrdinaryindustrynight + XWeekendindustrynight + XStatutoryresiduenight + (Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3));
                        residual.setLasttotalh(String.valueOf(XLasttotalh));
                        double After = 0d;
                        double After3 = 0d;
                        String XDate1;
                        String XMonths1;
                        String XYears1;
                        String XData1;
                        if (cal.get(cal.MONTH) == 1) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1 = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 2) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1 = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 3) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1 = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 4) {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears1 = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate1 = String.valueOf(cal.get(cal.DATE));
                        } else {
                            XMonths1 = String.valueOf(cal.get(cal.MONTH) - 4);
                            XYears1 = String.valueOf(cal.get(cal.YEAR));
                            XDate1 = String.valueOf(cal.get(cal.DATE));
                        }
                        if (Integer.parseInt(XMonths1) < 10 && Integer.parseInt(XDate1) < 10) {
                            XData1 = XYears1 + "-0" + XMonths1 + "-0" + XDate1;
                        } else if (Integer.parseInt(XMonths1) < 10) {
                            XData1 = XYears1 + "-0" + XMonths + "-" + XDate1;
                        } else if (Integer.parseInt(XDate1) < 10) {
                            XData1 = XYears1 + "-" + XMonths1 + "-0" + XDate1;
                        } else {
                            XData1 = XYears1 + "-" + XMonths1 + "-" + XDate1;
                        }
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String Date1 = sf1.format(new Date());
                        Query query1 = new Query();
                        query1.addCriteria(Criteria.where("userid").is(attendance.getUser_id()));
                        CustomerInfo customerInfo1 = mongoTemplate.findOne(query1, CustomerInfo.class);
                        if (customerInfo1 != null) {
                            if (customerInfo.getUserinfo().getGridData() != null) {
                                List<CustomerInfo.Personal> customerInfoList = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                                for (CustomerInfo.Personal personal : customerInfoList) {
                                    if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Date1.replace("-", ""))) {
                                        After = Double.valueOf(personal.getAfter());
                                        break;
                                    }
                                }
                                for (CustomerInfo.Personal personal : customerInfoList) {
                                    if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(XData1.replace("-", ""))) {
                                        After3 = Double.valueOf(personal.getAfter());
                                        break;
                                    }
                                }
                            } else {
                                After = 0;
                                After3 = 0;
                            }
                        }
                        double xweekendindustry = 0d;
                        double xordinaryindustry = 0d;
                        double xstatutoryresidue = 0d;
                        double xSYJB = 0d;
                        Dictionary xdictionary = new Dictionary();
                        List<Dictionary> xdictionarylist = dictionaryMapper.select(xdictionary);
                        for (Dictionary diction : xdictionarylist) {
                            if (diction.getCode().equals("PR049006")) {
                                xordinaryindustry = Double.valueOf(diction.getValue2());
                            }
                            if (diction.getCode().equals("PR049007")) {
                                xweekendindustry = Double.valueOf(diction.getValue2());
                            }
                            if (diction.getCode().equals("PR049008")) {
                                xstatutoryresidue = Double.valueOf(diction.getValue2());
                            }
                            if (diction.getCode().equals("PR049009")) {
                                xSYJB = Double.valueOf(diction.getValue2());
                            }
                        }
                        one = Double.valueOf(XOrdinaryindustry * xordinaryindustry + XOrdinaryindustrynight * xordinaryindustry * xSYJB);
                        two = Double.valueOf(XWeekendindustry * xweekendindustry + XWeekendindustrynight * xweekendindustry * xSYJB);
                        three = Double.valueOf(XStatutoryresidue * xstatutoryresidue + XStatutoryresiduenight * xstatutoryresidue * xSYJB) * (After / 21.75 / 8);
                        four = Double.valueOf((Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)) * xweekendindustry) * (After3 / 21.75 / 8);
                        Lasttotaly = one + two + three + four;
                        residual.setLasttotaly(df.format(Lasttotaly));
                    }
                    if (Lasttotaly + Thistotaly == 0.0) {
                        residual.setSubsidy(String.valueOf(Lasttotaly + Thistotaly));
                    } else {
                        residual.setSubsidy(df.format(Lasttotaly + Thistotaly));
                    }
                } else {
                    String months10 = String.valueOf(cal.get(cal.MONTH));
                    List<Attendance> AttendanceList1 = givingMapper.selectAttendance(user_id, years, months10);
                    for (Attendance attendance1 : AttendanceList1) {
                        residual.setRemarks("-");
                        residual.setLastweekdays(attendance1.getOrdinaryindustry());
                        residual.setLastrestDay(attendance1.getWeekendindustry());
                        residual.setLastlegal(attendance1.getStatutoryresidue());
                        residual.setLastlatenight(attendance1.getOrdinaryindustrynight());
                        residual.setLastrestlatenight(attendance1.getWeekendindustrynight());
                        residual.setLastlegallatenight(attendance1.getStatutoryresiduenight());
                        int Xi = 0;
                        int Xo = 0;
                        int Xm = 0;
                        int Xn = 0;
                        double XDaixiu1 = 0d;
                        double XDaixiu2 = 0d;
                        double XDaixiu3 = 0d;
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
                        atten1.setUser_id(attendance1.getUser_id());
                        atten1.setYears(Xyears1);
                        atten1.setMonths(Xmonths1);
                        List<Attendance> Attendancelist11 = attendanceMapper.select(atten1);
                        for (Attendance attendancelist : Attendancelist11) {
                            if (Double.valueOf(attendancelist.getWeekendindustry()) >= 8.0) {
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
                        Xa.setUser_id(attendance1.getUser_id());
                        Xa.setYears(Xyears2);
                        Xa.setMonths(Xmonths2);
                        List<Attendance> XAttendancelist1 = attendanceMapper.select(Xa);
                        for (Attendance attendancelist1 : XAttendancelist1) {
                            if (Double.valueOf(attendancelist1.getWeekendindustry()) >= 8.0) {
                                Xo = Xo + 1;
                            }
                        }
                        List<Attendance> XattendanceList = givingMapper.selectAttendance(user_id, Xyears2, Xmonths2);
                        for (Attendance A : XattendanceList) {
                            if (A.getDaixiu() != null) {
                                XDaixiu1 = Double.valueOf(A.getDaixiu());
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
                        Xa1.setUser_id(attendance1.getUser_id());
                        Xa1.setYears(Xyears3);
                        Xa1.setMonths(Xmonths3);
                        List<Attendance> XAttendancelist2 = attendanceMapper.select(Xa1);
                        for (Attendance attendancelist2 : XAttendancelist2) {
                            if (Double.valueOf(attendancelist2.getWeekendindustry()) >= 8.0) {
                                Xm = Xm + 1;
                            }
                        }
                        List<Attendance> XattendanceList1 = givingMapper.selectAttendance(user_id, Xyears3, Xmonths3);
                        for (Attendance A : XattendanceList1) {
                            if (A.getDaixiu() != null) {
                                XDaixiu2 = Double.valueOf(A.getDaixiu());
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
                        Xa2.setUser_id(attendance1.getUser_id());
                        Xa2.setYears(Xyears4);
                        Xa2.setMonths(Xmonths4);
                        List<Attendance> XAttendancelist3 = attendanceMapper.select(Xa2);
                        for (Attendance attendancelist3 : XAttendancelist3) {
                            if (Double.valueOf(attendancelist3.getWeekendindustry()) >= 8.0) {
                                Xn = Xn + 1;
                            }
                        }
                        List<Attendance> XattendanceList2 = givingMapper.selectAttendance(user_id, Xyears4, Xmonths4);
                        for (Attendance A : XattendanceList2) {
                            if (A.getDaixiu() != null) {
                                XDaixiu3 = Double.valueOf(A.getDaixiu());
                            } else {
                                XDaixiu3 = 0;
                            }
                        }
                        residual.setLastreplace(String.valueOf(Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)));
                        double XOrdinaryindustry = 0d;
                        double XWeekendindustry = 0d;
                        double XStatutoryresidue = 0d;
                        double XOrdinaryindustrynight = 0d;
                        double XWeekendindustrynight = 0d;
                        double XStatutoryresiduenight = 0d;
                        double XLasttotalh = 0d;
                        if (attendance1.getOrdinaryindustry() == null) {
                            XOrdinaryindustry = 0;
                        } else {
                            XOrdinaryindustry = Double.valueOf(attendance1.getOrdinaryindustry());
                        }
                        if (attendance1.getWeekendindustry() == null) {
                            XWeekendindustry = 0;
                        } else {
                            XWeekendindustry = Double.valueOf(attendance1.getWeekendindustry());
                        }
                        if (attendance1.getStatutoryresidue() == null) {
                            XStatutoryresidue = 0;
                        } else {
                            XStatutoryresidue = Double.valueOf(attendance1.getStatutoryresidue());
                        }
                        if (attendance1.getOrdinaryindustrynight() == null) {
                            XOrdinaryindustrynight = 0;
                        } else {
                            XOrdinaryindustrynight = Double.valueOf(attendance1.getOrdinaryindustrynight());
                        }
                        if (attendance1.getWeekendindustrynight() == null) {
                            XWeekendindustrynight = 0;
                        } else {
                            XWeekendindustrynight = Double.valueOf(attendance1.getWeekendindustrynight());
                        }
                        if (attendance1.getStatutoryresiduenight() == null) {
                            XStatutoryresiduenight = 0;
                        } else {
                            XStatutoryresiduenight = Double.valueOf(attendance1.getStatutoryresiduenight());
                        }
                        XLasttotalh = XOrdinaryindustry + XWeekendindustry + XStatutoryresidue + XOrdinaryindustrynight + XWeekendindustrynight + XStatutoryresiduenight + (Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3));
                        residual.setLasttotalh(String.valueOf(XLasttotalh));

                        double after = 0d;
                        double after3 = 0d;
                        Query query = new Query();
                        String XDate;
                        String XMonths;
                        String XYears;
                        if (cal.get(cal.MONTH) == 1) {
                            XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 2) {
                            XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 3) {
                            XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate = String.valueOf(cal.get(cal.DATE));
                        } else if (cal.get(cal.MONTH) == 4) {
                            XMonths = String.valueOf(cal.get(cal.MONTH) + 8);
                            XYears = String.valueOf(cal.get(cal.YEAR) - 1);
                            XDate = String.valueOf(cal.get(cal.DATE));
                        } else {
                            XMonths = String.valueOf(cal.get(cal.MONTH) - 4);
                            XYears = String.valueOf(cal.get(cal.YEAR));
                            XDate = String.valueOf(cal.get(cal.DATE));
                        }
                        String XData;
                        if (Integer.parseInt(XMonths) < 10 && Integer.parseInt(XDate) < 10) {
                            XData = XYears + "-0" + XMonths + "-0" + XDate;
                        } else if (Integer.parseInt(XMonths) < 10) {
                            XData = XYears + "-0" + XMonths + "-" + XDate;
                        } else if (Integer.parseInt(XDate) < 10) {
                            XData = XYears + "-" + XMonths + "-0" + XDate;
                        } else {
                            XData = XYears + "-" + XMonths + "-" + XDate;
                        }
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                        String Date = sf.format(new Date());
                        query.addCriteria(Criteria.where("userid").is(attendance1.getUser_id()));
                        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            if (customerInfo.getUserinfo().getGridData() != null) {
                                List<CustomerInfo.Personal> customerInfo1 = customerInfo.getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                                for (CustomerInfo.Personal personal : customerInfo1) {
                                    if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(Date.replace("-", ""))) {
                                        after = Double.valueOf(personal.getAfter());
                                        break;
                                    }
                                }
                                for (CustomerInfo.Personal personal : customerInfo1) {
                                    if (Integer.parseInt(personal.getDate().replace("-", "")) <= Integer.parseInt(XData.replace("-", ""))) {
                                        after3 = Double.valueOf(personal.getAfter());
                                        break;
                                    }
                                }
                            } else {
                                after = 0;
                                after3 = 0;
                            }
                        }
                        double xweekendindustry = 0d;
                        double xordinaryindustry = 0d;
                        double xstatutoryresidue = 0d;
                        double xSYJB = 0d;
                        Dictionary xdictionary = new Dictionary();
                        List<Dictionary> xdictionarylist = dictionaryMapper.select(xdictionary);
                        for (Dictionary diction : xdictionarylist) {
                            if (diction.getCode().equals("PR049006")) {
                                xordinaryindustry = Double.valueOf(diction.getValue2());
                            }
                            if (diction.getCode().equals("PR049007")) {
                                xweekendindustry = Double.valueOf(diction.getValue2());
                            }
                            if (diction.getCode().equals("PR049008")) {
                                xstatutoryresidue = Double.valueOf(diction.getValue2());
                            }
                            if (diction.getCode().equals("PR049009")) {
                                xSYJB = Double.valueOf(diction.getValue2());
                            }
                        }
                        one = Double.valueOf(XOrdinaryindustry * xordinaryindustry + XOrdinaryindustrynight * xordinaryindustry * xSYJB);
                        two = Double.valueOf(XWeekendindustry * xweekendindustry + XWeekendindustrynight * xweekendindustry * xSYJB);
                        three = Double.valueOf(XStatutoryresidue * xstatutoryresidue + XStatutoryresiduenight * xstatutoryresidue * xSYJB) * (after / 21.75 / 8);
                        four = Double.valueOf((Xi * 8 - (XDaixiu1 + XDaixiu2 + XDaixiu3)) * xweekendindustry) * (after3 / 21.75 / 8);
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
            String months = String.valueOf(cal.get(cal.MONTH) + 1);
            String user_id = b.getUser_id();
            List<Attendance> AttendanceList = givingMapper.selectAttendance(user_id, years, months);
            int rowundex = 0;
            double one = 0d;
            double two = 0d;
            double three = 0d;
            double Lasttotal = 0d;
            double thistotal = 0d;
            for (Attendance attendance : AttendanceList) {
                double Shortsickleave = 0d;
                double DLZD = 0d;
                Dictionary dictionary = new Dictionary();
                List<Dictionary> dictionarylist = dictionaryMapper.select(dictionary);
                for (Dictionary diction : dictionarylist) {
                    if (diction.getCode().equals("PR047001")) {
                        DLZD = Double.valueOf(diction.getValue2());
                    }
                    if (diction.getCode().equals("PR049005")) {
                        Shortsickleave = Double.valueOf(diction.getValue2());
                    }
                }
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
                        double Late = 0d;
                        double Leaveearly = 0d;
                        double Absenteeism = 0d;
                        double Thismonth = 0d;
                        double Thisdiligence = 0d;
                        if (B.getThismonth() == null || Double.valueOf(B.getThismonth()) == 0.0) {
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
                            two = Double.valueOf(Thismonth / 21.75 / 8 * Shortsickleave * Integer.parseInt(attendance.getShortsickleave()));
                        }
                        if (attendance.getLongsickleave() == null) {
                            three = 0;
                        } else {
                            three = Double.valueOf(DLZD / 21.75 / 8 * Integer.parseInt(attendance.getLongsickleave()));
                        }

                        Thisdiligence = Late + Leaveearly + Absenteeism;
                        lackattendance.setThisdiligence(String.valueOf(Thisdiligence));
                        one = Double.valueOf(Thisdiligence * Thismonth / 21.75 / 8);
                        thistotal = one - two - three;

                        if (thistotal == 0.0) {
                            lackattendance.setThistotal(String.valueOf(thistotal));
                        } else {
                            lackattendance.setThistotal(df.format(thistotal));
                        }

                        String Years = String.valueOf(cal.get(cal.YEAR));
                        String Months = String.valueOf(cal.get(cal.MONTH));
                        String User_id = b.getUser_id();
                        List<Attendance> Attendancelist = givingMapper.selectAttendance(User_id, Years, Months);
                        for (Attendance attendan : Attendancelist) {
                            lackattendance.setLastshortdeficiency(attendan.getShortsickleave());
                            lackattendance.setLastchronicdeficiency(attendan.getLongsickleave());
                            Base bas = new Base();
                            bas.setUser_id(attendan.getUser_id());
                            List<Base> BaseList = baseMapper.select(bas);
                            for (Base Ba : BaseList) {
                                double late = 0d;
                                double leaveearly = 0d;
                                double absenteeism = 0d;
                                double Lastmonth = 0d;
                                double Lastdiligence = 0d;
                                if (Ba.getLastmonth() == null || Double.valueOf(Ba.getLastmonth()) == 0.0) {
                                    Lastmonth = 0;
                                } else {
                                    Lastmonth = Integer.parseInt(Ba.getLastmonth());
                                }
                                if (attendan.getLate() == null) {
                                    late = 0;
                                } else {
                                    late = Integer.parseInt(attendan.getLate());
                                }
                                if (attendan.getLeaveearly() == null) {
                                    leaveearly = 0;
                                } else {
                                    leaveearly = Integer.parseInt(attendan.getLeaveearly());
                                }
                                if (attendan.getAbsenteeism() == null) {
                                    absenteeism = 0;
                                } else {
                                    absenteeism = Integer.parseInt(attendan.getAbsenteeism());
                                }
                                if (attendan.getShortsickleave() == null) {
                                    two = 0;
                                } else {
                                    two = Double.valueOf(Lastmonth / 21.75 / 8 * Shortsickleave * Integer.parseInt(attendan.getShortsickleave()));
                                }
                                if (attendan.getLongsickleave() == null) {
                                    three = 0;
                                } else {
                                    three = Double.valueOf(DLZD / 21.75 / 8 * Integer.parseInt(attendan.getLongsickleave()));
                                }
                                Lastdiligence = late + leaveearly + absenteeism;
                                lackattendance.setLastdiligence(String.valueOf(Lastdiligence));
                                one = Double.valueOf(Lastdiligence * Lastmonth / 21.75 / 8);
                                Lasttotal = one - two - three;
                                if (Lasttotal == 0.0) {
                                    lackattendance.setLasttotal(String.valueOf(Lasttotal));
                                } else {
                                    lackattendance.setLasttotal(df.format(Lasttotal));
                                }
                            }
                        }
                    }
                } else {
                    lackattendance.setRemarks("-");
                    lackattendance.setThisshortdeficiency(attendance.getShortsickleave());
                    lackattendance.setThischronicdeficiency(attendance.getLongsickleave());
                    Base ba = new Base();
                    ba.setUser_id(attendance.getUser_id());
                    List<Base> Baselist = baseMapper.select(ba);
                    for (Base B : Baselist) {
                        double Late = 0d;
                        double Leaveearly = 0d;
                        double Absenteeism = 0d;
                        double Thismonth = 0d;
                        double Thisdiligence = 0d;
                        if (B.getThismonth() == null || Double.valueOf(B.getThismonth()) == 0.0) {
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
                            two = Double.valueOf(Thismonth / 21.75 / 8 * Shortsickleave * Integer.parseInt(attendance.getShortsickleave()));
                        }
                        if (attendance.getLongsickleave() == null) {
                            three = 0;
                        } else {
                            three = Double.valueOf(DLZD / 21.75 / 8 * Integer.parseInt(attendance.getLongsickleave()));
                        }
                        Thisdiligence = Late + Leaveearly + Absenteeism;
                        lackattendance.setThisdiligence(String.valueOf(Thisdiligence));
                        one = Double.valueOf(Thisdiligence * Thismonth / 21.75 / 8);
                        thistotal = one - two - three;
                        if (thistotal == 0.0) {
                            lackattendance.setThistotal(String.valueOf(thistotal));
                        } else {
                            lackattendance.setThistotal(df.format(thistotal));
                        }
                        String Years = String.valueOf(cal.get(cal.YEAR));
                        String Months = String.valueOf(cal.get(cal.MONTH));
                        String User_id = b.getUser_id();
                        List<Attendance> Attendancelist = givingMapper.selectAttendance(User_id, Years, Months);
                        for (Attendance attendan : Attendancelist) {
                            lackattendance.setLastshortdeficiency(attendan.getShortsickleave());
                            lackattendance.setLastchronicdeficiency(attendan.getLongsickleave());
                            Base bas = new Base();
                            bas.setUser_id(attendan.getUser_id());
                            List<Base> BaseList = baseMapper.select(bas);
                            for (Base Ba : BaseList) {
                                double late = 0d;
                                double leaveearly = 0d;
                                double absenteeism = 0d;
                                double Lastmonth = 0d;
                                double Lastdiligence = 0d;
                                if (Ba.getLastmonth() == null || Double.valueOf(Ba.getLastmonth()) == 0.0) {
                                    Lastmonth = 0;
                                } else {
                                    Lastmonth = Integer.parseInt(Ba.getLastmonth());
                                }
                                if (attendan.getLate() == null) {
                                    late = 0;
                                } else {
                                    late = Integer.parseInt(attendan.getLate());
                                }
                                if (attendan.getLeaveearly() == null) {
                                    leaveearly = 0;
                                } else {
                                    leaveearly = Integer.parseInt(attendan.getLeaveearly());
                                }
                                if (attendan.getAbsenteeism() == null) {
                                    absenteeism = 0;
                                } else {
                                    absenteeism = Integer.parseInt(attendan.getAbsenteeism());
                                }
                                if (attendan.getShortsickleave() == null) {
                                    two = 0;
                                } else {
                                    two = Double.valueOf(Lastmonth / 21.75 / 8 * Shortsickleave * Integer.parseInt(attendan.getShortsickleave()));
                                }
                                if (attendan.getLongsickleave() == null) {
                                    three = 0;
                                } else {
                                    three = Double.valueOf(DLZD / 21.75 / 8 * Integer.parseInt(attendan.getLongsickleave()));
                                }
                                Lastdiligence = late + leaveearly + absenteeism;
                                lackattendance.setLastdiligence(String.valueOf(Lastdiligence));
                                one = Double.valueOf(Lastdiligence * Lastmonth / 21.75 / 8);
                                Lasttotal = one - two - three;
                                if (Lasttotal == 0.0) {
                                    lackattendance.setLasttotal(String.valueOf(Lasttotal));
                                } else {
                                    lackattendance.setLasttotal(df.format(Lasttotal));
                                }
                            }
                        }
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

    //入职
    public List<CustomerInfo> getInduction(String givingId) {
        Query query = new Query();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        Calendar _now = Calendar.getInstance();
        now.add(Calendar.MONTH, -2);
        _now.add(Calendar.MONTH, -1); //this
        now.set(Calendar.DAY_OF_MONTH, 15);
        _now.set(Calendar.DAY_OF_MONTH, 16);

        Calendar lastmon = Calendar.getInstance();
        lastmon.add(Calendar.MONTH, -1);
        Calendar thismon = Calendar.getInstance();
        Criteria criteria = Criteria.where("userinfo.enterday")
                .gte(sf.format(now.getTime()))
                .lte(sf.format(_now.getTime()));
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfo = mongoTemplate.find(query, CustomerInfo.class);
        if (customerInfo.size() > 0) {
            List<String> list = new ArrayList<>();
            customerInfo.forEach(customerInfo1 -> list.add(customerInfo1.getUserid()));
            List<Induction> inductions = inductionMapper.selectInduction(list, thismon.get(Calendar.YEAR) + "" + getMouth(sf.format(_now.getTime())),
                    lastmon.get(Calendar.YEAR) + "" + getMouth(sf.format(now.getTime())), getMouth(sf.format(_now.getTime())) + "", getMouth(sf.format(now.getTime())) + "");
            if (inductions.size() > 0) {
                inductions.forEach(induction -> {
                    Optional<CustomerInfo> _customerInfo = customerInfo.stream().filter(a -> (induction.getUser_id()).equals(a.getUserid())).findFirst();
                    if (_customerInfo.isPresent()) {
                        if(givingId!=null&&givingId!=""){
                            induction.setGiving_id(givingId);
                        }
                        try {
                            induction.setWorddate(sf.parse(_customerInfo.get().getUserinfo().getWorkday()));
                            if (_customerInfo.get().getUserinfo().getEnddate() != null && !_customerInfo.get().getUserinfo().getEnddate().equals("")) {
                                induction.setStartdate(sf.parse(_customerInfo.get().getUserinfo().getEnddate()));
                            }
                            if (_customerInfo.get().getUserinfo().getGridData() != null) {
                                List<CustomerInfo.Personal> personals = _customerInfo.get().getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed())
                                        .collect(Collectors.toList());
                                String lastMouth = "0";
                                String thisMouth = "0";
                                for (CustomerInfo.Personal personal : personals) {
                                    boolean _last = false;
                                    boolean _this = false;

                                    if (sf.parse(personal.getDate()).getTime() <= sf.parse((lastmon.get(Calendar.YEAR) + "-" + getMouth(sf.format(now.getTime())) + "-01")).getTime() && !_last) {
                                        lastMouth = personal.getAfter();
                                        _last = true;
                                    }
                                    if (sf.parse(personal.getDate()).getTime() <= sf.parse((thismon.get(Calendar.YEAR) + "-" + getMouth(sf.format(_now.getTime())) + "-01")).getTime() && !_this) {
                                        thisMouth = personal.getAfter();
                                        _this = true;
                                    }
                                    if (_last && _this) {
                                        break;
                                    }
                                }
                                //IF(正社員開始日<>"",ROUND((当月基本工资 - （当月基本工资/21.75日*当月试用期间的出勤日数×试用期工资扣除比例),2),IF(先月出勤日数>0,ROUND((先月基本工资/21.75*先月
                                //出勤日数+当月基本工资),2),IF(今月試用社員出勤日数>0,ROUND(当月基本工资/21.75*今月試用社員出勤日数,2),当月基本工资)))
                                DecimalFormat df = new DecimalFormat("#.00");
                                if (_customerInfo.get().getUserinfo().getWorkday().equals("")) {
                                    induction.setGive(df.format(Double.valueOf(thisMouth) - (Double.valueOf(thisMouth) / 21.75 * Double.valueOf(induction.getThismouth()) * 0.1)));
                                } else {
                                    if (Integer.parseInt(induction.getLastmouth()) > 0) {
                                        induction.setGive(df.format(Double.valueOf(lastMouth) / 21.75 * Integer.parseInt(induction.getLastmouth()) * 0.1 + Double.valueOf(thisMouth)));
                                    } else {
                                        if (Integer.parseInt(induction.getThismouth()) > 0) {
                                            induction.setGive(df.format(Double.valueOf(thisMouth) / 21.75 * Integer.parseInt(induction.getThismouth())));
                                        } else {
                                            induction.setGive(thisMouth);
                                        }
                                    }
                                }
                                //IF(先月基本工资<>0,ROUND(纳付率.食堂手当+纳付率.食堂手当/21.75*先月出勤日数,2),ROUND(纳付率.食堂手当/21.75*今月試用社員出勤日数,2))
                                if (Double.valueOf(lastMouth) > 0) {
                                    induction.setLunch(df.format(105 + 105 / 21.75 * Integer.parseInt(induction.getLastmouth())));
                                } else {
                                    induction.setLunch(df.format(105 / 21.75 * Integer.parseInt(induction.getThismouth())));
                                }
                                //IF(先月基本工资<>0,ROUND(纳付率.交通手当+纳付率.交通手当/21.75*先月出勤日数,2),ROUND(纳付率.交通手当/21.75*今月試用社員出勤日数,2))
                                if (Double.valueOf(lastMouth) > 0) {
                                    induction.setLunch(df.format(84 + 84 / 21.75 * Integer.parseInt(induction.getLastmouth())));
                                } else {
                                    induction.setLunch(df.format(84 / 21.75 * Integer.parseInt(induction.getThismouth())));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        return null;
    }


    //退职
    public void getRetire(String givingId) {
        Query query = new Query();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.add(Calendar.MONTH, -1);
        now.add(Calendar.MONTH, -2);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR));
        now.set(Calendar.MONTH, now.get(Calendar.MONTH));
        int lastDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        Criteria criteria = Criteria.where("userinfo.resignation_date")
                .gte(now.get(Calendar.YEAR) + "-" + getMouth(sf.format(now.getTime())) + "-" + lastDay)
                .lte(cal.get(Calendar.YEAR) + "-" + getMouth(sf.format(cal.getTime())) + "-01");
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfo = mongoTemplate.find(query, CustomerInfo.class);
        if (customerInfo.size() > 0) {
            List<String> list = new ArrayList<>();
            customerInfo.forEach(customerInfo1 -> list.add(customerInfo1.getUserid()));
            List<Retire> retires = retireMapper.selectRetire(list, last.get(Calendar.YEAR) + "" + getMouth(sf.format(last.getTime())), getMouth(sf.format(last.getTime())) + "");
            if (retires.size() > 0) {
                retires.forEach(retire -> {
                    Optional<CustomerInfo> _customerInfo = customerInfo.stream().filter(a -> (retire.getUser_id()).equals(a.getUserid())).findFirst();
                    if (_customerInfo.isPresent()) {
                        if(givingId!=null&&givingId!=""){
                            retire.setGiving_id(givingId);
                        }
                        String thisMouth = "0";
                        try {
                            if (_customerInfo.get().getUserinfo().getResignation_date() != null && !_customerInfo.get().getUserinfo().getResignation_date().equals("")) {
                                retire.setRetiredate(sf.parse(_customerInfo.get().getUserinfo().getResignation_date()));
                            }
                            if (_customerInfo.get().getUserinfo().getGridData() != null) {
                                List<CustomerInfo.Personal> personals = _customerInfo.get().getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed())
                                        .collect(Collectors.toList());

                                for (CustomerInfo.Personal personal : personals) {
                                    if (sf.parse(personal.getDate()).getTime() <= sf.parse((last.get(Calendar.YEAR) + "-" + getMouth(sf.format(last.getTime())) + "-01")).getTime()) {
                                        thisMouth = personal.getAfter();
                                        return;
                                    }
                                }
                            }
                            DecimalFormat df = new DecimalFormat("#.00");
                            //IF(今月出勤日数="全月",ROUND(基数.当月基本工资,2),IF(今月出勤日数<>"",ROUND(基数.当月基本工资/21.75*今月出勤日数,2),0))
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMouth(sf.format(last.getTime()))))) {
                                retire.setGive(df.format(Double.valueOf(thisMouth)));
                            } else if (Integer.parseInt(retire.getAttendance()) != 0) {
                                retire.setGive(df.format(Double.valueOf(df.format(Double.valueOf(thisMouth) / 21.75 * Integer.parseInt(retire.getAttendance())))));
                            } else {
                                retire.setGive("0");
                            }
                            //IF(今月出勤日数="全月",纳付率.食堂手当,ROUND(纳付率.食堂手当/21.75*今月出勤日数,2)
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMouth(sf.format(last.getTime()))))) {
                                retire.setLunch("105");
                            } else {
                                retire.setLunch(df.format(105 / 21.75 * Integer.parseInt(retire.getAttendance())));
                            }
                            //IF(今月出勤日数="全月",纳付率.交通手当,ROUND(纳付率.交通手当/21.75*今月出勤日数,2))
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMouth(sf.format(last.getTime()))))) {
                                retire.setLunch("84");
                            } else {
                                retire.setLunch(df.format(84 / 21.75 * Integer.parseInt(retire.getAttendance())));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    public String getMouth(String mouth) {
        String _mouth = mouth.substring(5, 7);
        if (_mouth.substring(0, 1) == "0") {
            return _mouth.substring(1);
        }
        return _mouth;
    }



    private static int getWorkDays(int theYear, int theMonth) {
        int workDays = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(theYear, theMonth - 1, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                workDays++;
            }
            cal.add(Calendar.DATE, 1);
        }
        return workDays;
    }
}
