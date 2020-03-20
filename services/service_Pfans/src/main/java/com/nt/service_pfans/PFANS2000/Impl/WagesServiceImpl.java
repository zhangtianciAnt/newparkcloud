package com.nt.service_pfans.PFANS2000.Impl;


import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.BaseVo;
import com.nt.service_pfans.PFANS2000.WagesService;
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
public class WagesServiceImpl implements WagesService {

    @Autowired
    private WagesMapper wagesMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private InductionMapper inductionMapper;
    @Autowired
    private RetireMapper retireMapper;
    @Autowired
    private GivingMapper givingMapper;
    @Autowired
    private BonussendMapper bonussendMapper;



    @Override
    public List<Wages> select(TokenModel tokenModel) {
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
                            }else if(Integer.parseInt(retire.getAttendance())!=0){
                                    retire.setGive(df.format(Double.valueOf(df.format(Double.valueOf(thisMouth)/21.75*Integer.parseInt(retire.getAttendance())))));
                                }else{
                                retire.setGive("0");
                            }
                            //IF(今月出勤日数="全月",纳付率.食堂手当,ROUND(纳付率.食堂手当/21.75*今月出勤日数,2)
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMouth(sf.format(last.getTime()))))) {
                                retire.setLunch("105");
                            }else{
                                retire.setLunch(df.format(105/21.75*Integer.parseInt(retire.getAttendance())));
                            }
                            //IF(今月出勤日数="全月",纳付率.交通手当,ROUND(纳付率.交通手当/21.75*今月出勤日数,2))
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMouth(sf.format(last.getTime()))))) {
                                retire.setLunch("84");
                            }else{
                                retire.setLunch(df.format(84/21.75*Integer.parseInt(retire.getAttendance())));
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

    public String getMouth(String mouth) {
        String _mouth = mouth.substring(5, 7);
        if (_mouth.substring(0, 1) == "0") {
            return _mouth.substring(1);
        }
        return _mouth;
    }

    private static int getWorkDays(int theYear, int theMonth) {
        // 计算指定月有多少工作日
        int workDays = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(theYear, theMonth - 1, 1);// 从每月1号开始
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

    @Override
    public List<Wages> wagesList(Wages wages) throws Exception {
        List<Wages> listw = wagesMapper.select(wages);
        for(Wages wages1 : listw){
            if(wages1.getGiving_id() != null){
                Giving giving = new Giving();
                giving.setGiving_id(wages1.getGiving_id());
                List<Giving> givingList = givingMapper.select(giving);
                for(Giving giving1 : givingList) {
                    if (wages1.getGiving_id().equals(giving1.getGiving_id())) {
                    wages1.setGiving_id(giving1.getMonths());
                    }
                }

            }
        }
        return listw;
    }

    @Override
    public List<BaseVo> selectBase() throws Exception {
        return givingMapper.selectBase();
    }

    @Override
    public List<Bonussend> bonusList(Bonussend bonussend) throws Exception {

        return bonussendMapper.select(bonussend);
    }




}
