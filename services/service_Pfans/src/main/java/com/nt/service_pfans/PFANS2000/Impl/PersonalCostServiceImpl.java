package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.util.StrUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS2000.PersonalCostYears;
import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostYearsMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonalCostServiceImpl implements PersonalCostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    PersonalCostYearsMapper personalCostYearsMapper;

    @Autowired
    PersonalCostMapper personalCostMapper;

    @Override
    public List<PersonalCostYears> getPerCostYarList(PersonalCostYears personalCostYears) {
        return personalCostYearsMapper.select(personalCostYears);
    }

    @Override
    public PersonalCost insertPenalcost(String year, TokenModel tokenModel) throws Exception{
        Query query = new Query();
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
        //年度
        PersonalCostYears personalCostYears = new PersonalCostYears();
        personalCostYears.preInsert(tokenModel);
        String personalCostYerasid = UUID.randomUUID().toString();
        //暂时处理
        personalCostYears.setYears(year);
        personalCostYears.setYearsantid(personalCostYerasid);
        personalCostYearsMapper.insert(personalCostYears);
        for(Iterator<CustomerInfo> custList = customerInfos.iterator(); custList.hasNext();){
            CustomerInfo customerInfoAnt = custList.next();
            //清除去年离职
            if(StringUtils.isNullOrEmpty(customerInfoAnt.getUserinfo().getResignation_date())){
                PersonalCost personalCost = new PersonalCost();
                //token
                personalCost.preInsert(tokenModel);
                personalCost.setPersonalcostid(UUID.randomUUID().toString());
                //年度
                personalCost.setYearsantid(personalCostYerasid);
                //userid
                personalCost.setUserid(customerInfoAnt.getUserid());
                //7~3月人件费 1月1日自动生成
                BigDecimal Basic = BigDecimal.valueOf(Double.valueOf(customerInfoAnt.getUserinfo().getBasic()));
                BigDecimal duty = BigDecimal.valueOf(Double.valueOf(customerInfoAnt.getUserinfo().getDuty()));
                Double lastAnt = Basic.add(duty).doubleValue();
                BigDecimal jutomacost = new BigDecimal(lastAnt).setScale(2, BigDecimal.ROUND_HALF_UP);
                personalCost.setJutomacost(jutomacost.toString());
                //rank
                personalCost.setPrank(customerInfoAnt.getUserinfo().getRank());
                personalCostMapper.insert(personalCost);
            }
            custList.remove();
        }
        return null;
    }



    @Override
    public List<PersonalCost> getPersonalCost(String groupid,String yearsantid) throws Exception {
        List<CustomerInfo> customerInfoList =  mongoTemplate.find(new Query(Criteria.where("userinfo.groupid").is(groupid)), CustomerInfo.class);
        List<PersonalCost> personalCostList = personalCostMapper.selectPersonalCostResult(customerInfoList,yearsantid);

        return personalCostList;
    }

    @Override
    public void upPersonalCost(List<PersonalCost> personalCostList, TokenModel tokenModel) throws Exception {
        personalCostMapper.updatePersonalCost(personalCostList,tokenModel);
    }
//
//    @Override
//    public void insert(Recruit recruit, TokenModel tokenModel) throws Exception {
////add-ws-8/4-禅道任务296--
//        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
//        Date date = new Date();
//        List<Recruit> recruitlist = recruitMapper.selectAll();
//        String year = sf1.format(date);
//        int number = 0;
//        String Numbers = "";
//        String no = "";
//        if (recruitlist.size() > 0) {
//            for (Recruit recr : recruitlist) {
//                if (recr.getNumbers() != "" && recr.getNumbers() != null) {
//                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(recr.getNumbers(), 2, 10));
//                    if (Integer.valueOf(year).equals(Integer.valueOf(checknumber))) {
//                        number = number + 1;
//                    }
//                }
//
//            }
//            if (number <= 8) {
//                no = "00" + (number + 1);
//            } else {
//                no = "0" + (number + 1);
//            }
//        } else {
//            no = "001";
//        }
//        Numbers = "ZP" + year + no;
//        //add-ws-8/4-禅道任务296--
//        recruit.preInsert(tokenModel);
//        recruit.setNumbers(Numbers);
//        recruit.setRecruitid(UUID.randomUUID().toString());
//        recruitMapper.insert(recruit);
//    }
//
//    @Override
//    public List<Recruit> getRecruitList(Recruit recruit, HttpServletRequest request) throws Exception {
//
//        return recruitMapper.select(recruit);
//    }
}
