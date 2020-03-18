package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.*;
//import com.nt.dao_Pfans.PFANS1000.Businessplandet;
import com.nt.dao_Pfans.PFANS1000.Vo.ActualPL;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.mapper.BusinessplanMapper;
//import com.nt.service_pfans.PFANS1000.mapper.BusinessplandetMapper;
import com.nt.service_pfans.PFANS1000.mapper.PersonnelplanMapper;
import com.nt.service_pfans.PFANS1000.mapper.PieceworktotalMapper;
import com.nt.service_pfans.PFANS1000.mapper.TotalplanMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@EnableScheduling
public class BusinessplanServiceImpl implements BusinessplanService {

    @Autowired
    private BusinessplanMapper businessplanMapper;
    @Autowired
    private TotalplanMapper totalplanMapper;
    @Autowired
    private PieceworktotalMapper pieceworktotalMapper;
    @Autowired
    private PersonnelplanMapper personnelplanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    //@Autowired
    //private BusinessplandetMapper businessplandetMapper;

    @Override
    public List<Businessplan> get(Businessplan businessplan) throws Exception {
        return businessplanMapper.select(businessplan);
    }

    @Override
    public Businessplan selectById(String businessplanid) throws Exception {
        Businessplan businessplan = businessplanMapper.selectByPrimaryKey(businessplanid);
        return businessplan;
    }

    @Override
    public void updateBusinessplanVo(BusinessplanVo businessplanVo, TokenModel tokenModel) throws Exception {
        Businessplan businessplan = new Businessplan();
        BeanUtils.copyProperties(businessplanVo.getBusinessplan(), businessplan);
        businessplan.preUpdate(tokenModel);
        businessplanMapper.updateByPrimaryKey(businessplan);
        String businessplanid = businessplan.getBusinessplanid();
        Pieceworktotal piece = new Pieceworktotal();
        Totalplan total = new Totalplan();
        piece.setBusinessplanid(businessplanid);
        total.setBusinessplanid(businessplanid);
        pieceworktotalMapper.delete(piece);
        totalplanMapper.delete(total);
        List<Pieceworktotal> pieceworktotallist = businessplanVo.getPieceworktotal();
        List<Totalplan> totalplanlist = businessplanVo.getTotalplan();
        if (pieceworktotallist != null) {
            int rowindex = 0;
            for (Pieceworktotal pieceworktotal : pieceworktotallist) {
                rowindex = rowindex + 1;
                pieceworktotal.preInsert(tokenModel);
                pieceworktotal.setPieceworktotal_id(UUID.randomUUID().toString());
                pieceworktotal.setBusinessplanid(businessplanid);
                pieceworktotal.setRowindex(rowindex);
                pieceworktotalMapper.insertSelective(pieceworktotal);
            }
        }
        if (totalplanlist != null) {
            int rowindex = 0;
            for (Totalplan totalplan : totalplanlist) {
                rowindex = rowindex + 1;
                totalplan.preInsert(tokenModel);
                totalplan.setTotalplan_id(UUID.randomUUID().toString());
                totalplan.setBusinessplanid(businessplanid);
                totalplan.setRowindex(rowindex);
                totalplanMapper.insertSelective(totalplan);
            }
        }
    }

    @Override
    public String[] getPersonPlan(int year, String groupid) throws Exception {
        String[] personTable = new String[4];

        List<ActualPL> actualPl = businessplanMapper.getAcutal(groupid,year + "-04-01", (year + 1) + "-03-31");
        List<PersonPlanTable> personPlanTables = businessplanMapper.selectPersonTable(groupid);
        PersonnelPlan personnelPlan = new PersonnelPlan();
        personnelPlan.setYears(year);
        personnelPlan.setGroupid(groupid);
        List<PersonnelPlan> personnelPlans = personnelplanMapper.select(personnelPlan);
        if (personnelPlans.size() > 0) {
            personnelPlan = personnelPlans.get(0);
            PersonPlanTable personPlan = new PersonPlanTable();
            Field[] fields = personPlan.getClass().getDeclaredFields();
            List<PersonPlanTable> nowPersonTable = getNowPersonTable(personnelPlan, personPlanTables);
            List<PersonPlanTable> nextPersonTable = getNextPersonTable(personnelPlan, personPlanTables);
                List<PersonPlanTable> allPersonTable = new ArrayList<>();
                allPersonTable.addAll(nowPersonTable);
                allPersonTable.addAll(nextPersonTable);
                for (PersonPlanTable allPT :
                        allPersonTable) {
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        if (fields[i].getGenericType() == int.class) {
                            int value = (int) fields[i].get(allPT) + (int) fields[i].get(personPlan);
                            fields[i].set(personPlan, value);
                        } else if (fields[i].getGenericType() == BigDecimal.class) {
                            BigDecimal value1 = (BigDecimal) fields[i].get(allPT);
                            BigDecimal value2 = fields[i].get(personPlan) == null ? new BigDecimal(0) : (BigDecimal) fields[i].get(personPlan);
                            fields[i].set(personPlan, value1.add(value2));
                        }
                    }
            }
            personTable[0] = JSON.toJSONString(nowPersonTable);
            personTable[1] = JSON.toJSONString(nextPersonTable);
            personTable[2] = JSON.toJSONString(personPlan);
            personTable[3] = JSON.toJSONString(actualPl);
        } else {
            personTable[0] = "";
            personTable[1] = "";
            personTable[2] = "";
            personTable[3] = JSON.toJSONString(actualPl);
        }
        return personTable;
    }

    @Override
    public void insertBusinessplan(Businessplan businessplan, TokenModel tokenModel) throws Exception {
        String businessplanid = UUID.randomUUID().toString();
        businessplan.setBusinessplanid(businessplanid);
        businessplan.preInsert(tokenModel);
        businessplanMapper.insert(businessplan);

    }

    private List<PersonPlanTable> getNowPersonTable(PersonnelPlan personnelPlan, List<PersonPlanTable> personPlanTables) throws Exception {
        if(!personnelPlan.getEmployed().equals("[]")){
        List<PersonPlanTable> _personPlanTables =  deepCopy(personPlanTables);
        List<Employed> employedList = JSON.parseArray(personnelPlan.getEmployed(), Employed.class);
        for (PersonPlanTable pt :
                _personPlanTables) {
            for (Employed employed :
                    employedList) {
                if (pt.getCode().equals(employed.getNextyear())) {
                    for (int i = 1; i <= 12; i++) {
                        int count = (int) PropertyUtils.getSimpleProperty(pt, "amount" + i) + 1;
                        PropertyUtils.setProperty(pt, "amount" + i, count);
                    }
                }
            }
            BigDecimal payHour = pt.getPayhour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getPayhour());
            BigDecimal overTimeHour = pt.getOvertimehour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getOvertimehour());
            BigDecimal giving46 = pt.getMoney46().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney46()); //给与4-6
            BigDecimal giving37 = pt.getMoney73().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney73()); //给与3-7
            for (int i = 1; i <= 12; i++) {
                BigDecimal giving;
                int count = (int) PropertyUtils.getProperty(pt, "amount" + i);
                BigDecimal workingHour = new BigDecimal(count).multiply(payHour); //残業
                BigDecimal pay = workingHour.multiply(overTimeHour); //残業费
                PropertyUtils.setProperty(pt, "workinghour" + i, workingHour);
                PropertyUtils.setProperty(pt, "pay" + i, pay);
                if (i == 4 || i == 5 || i == 6) {
                    giving = new BigDecimal(count).multiply(giving46);
                } else {
                    giving = new BigDecimal(count).multiply(giving37);
                }
                PropertyUtils.setProperty(pt, "giving" + i, giving);
            }
            pt.setAmountfirst(pt.getAmount4() * 6);
            pt.setAmountsecond(pt.getAmount4() * 6);
            pt.setAmounttotal(pt.getAmount4() * 12);

            pt.setWorkinghourfirst(pt.getWorkinghour4().multiply(new BigDecimal(6)));
            pt.setWorkinghoursecond(pt.getWorkinghour4().multiply(new BigDecimal(6)));
            pt.setWorkinghourtotal(pt.getWorkinghour4().multiply(new BigDecimal(12)));

            pt.setPayfirst(pt.getPay4().multiply(new BigDecimal(6)));
            pt.setPaysecond(pt.getPay4().multiply(new BigDecimal(6)));
            pt.setPaytotal(pt.getPay4().multiply(new BigDecimal(12)));

            pt.setGivingfirst(AllAdd(pt.getGiving4(), pt.getGiving5(), pt.getGiving6(), pt.getGiving7(), pt.getGiving8(), pt.getGiving9()));
            pt.setGivingsecond(AllAdd(pt.getGiving10(), pt.getGiving11(), pt.getGiving12(), pt.getGiving1(), pt.getGiving2(), pt.getGiving3()));
            pt.setGivingtotal(pt.getGivingfirst().add(pt.getGivingsecond()));
        }
        return _personPlanTables;
        }
        return new ArrayList<PersonPlanTable>();
    }

    private List<PersonPlanTable> getNextPersonTable(PersonnelPlan personnelPlan, List<PersonPlanTable> personPlanTables) throws Exception {
        if(!personnelPlan.getNewentry().equals("[{\"isoutside\":false,\"entermouth\":null}]")) {
            int[] arr = new int[]{4,5,6,7,8,9,10,11,12,1,2,3};
            List<PersonPlanTable> _personPlanTables = deepCopy(personPlanTables);
            Calendar calendar = Calendar.getInstance();
            List<NewEmployed> employedList = JSON.parseArray(personnelPlan.getNewentry(), NewEmployed.class);
            for (PersonPlanTable pt :
                    _personPlanTables) {
                for (NewEmployed employed :
                        employedList) {
                    if (pt.getCode().equals(employed.getNextyear()) && !employed.isIsoutside() && StringUtils.isNotBlank(employed.getEntermouth())) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(employed.getEntermouth());
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE, 1);
                        int mouth = getIndex(arr,calendar.get(Calendar.MONTH) + 1);
                        for( int i = mouth ; i< arr.length ; i++){
                            int count = (int) PropertyUtils.getSimpleProperty(pt, "amount" + arr[i]) + 1;
                            PropertyUtils.setProperty(pt, "amount" + arr[i], count);
                        }
                    }
                }
                BigDecimal payHour = pt.getPayhour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getPayhour());
                BigDecimal overTimeHour = pt.getOvertimehour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getOvertimehour());
                BigDecimal giving46 = pt.getMoney46().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney46()); //给与4-6
                BigDecimal giving37 = pt.getMoney73().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney73()); //给与3-7
                for (int i = 1; i <= 12; i++) {
                    BigDecimal giving;
                    int count = (int) PropertyUtils.getProperty(pt, "amount" + i);
                    BigDecimal workingHour = new BigDecimal(count).multiply(payHour); //残業
                    BigDecimal pay = workingHour.multiply(overTimeHour); //残業费
                    PropertyUtils.setProperty(pt, "workinghour" + i, workingHour);
                    PropertyUtils.setProperty(pt, "pay" + i, pay);
                    if (i == 4 || i == 5 || i == 6) {
                        giving = new BigDecimal(count).multiply(giving46);
                    } else {
                        giving = new BigDecimal(count).multiply(giving37);
                    }
                    PropertyUtils.setProperty(pt, "giving" + i, giving);
                }
                pt.setAmountfirst(pt.getAmount4() + pt.getAmount5() + pt.getAmount6() + pt.getAmount7() + pt.getAmount8() + pt.getAmount9());
                pt.setAmountsecond(pt.getAmount10() + pt.getAmount11() + pt.getAmount12() + pt.getAmount1() + pt.getAmount2() + pt.getAmount3());
                pt.setAmounttotal(pt.getAmountfirst() + pt.getAmountsecond());

                pt.setWorkinghourfirst(AllAdd(pt.getWorkinghour4(), pt.getWorkinghour5(), pt.getWorkinghour6(), pt.getWorkinghour7(), pt.getWorkinghour8(), pt.getWorkinghour9()));
                pt.setWorkinghoursecond(AllAdd(pt.getWorkinghour10(), pt.getWorkinghour11(), pt.getWorkinghour12(), pt.getWorkinghour1(), pt.getWorkinghour2(), pt.getWorkinghour3()));
                pt.setWorkinghourtotal(pt.getWorkinghourfirst().add(pt.getWorkinghoursecond()));

                pt.setPayfirst(AllAdd(pt.getPay4(), pt.getPay5(), pt.getPay6(), pt.getPay7(), pt.getPay8(), pt.getPay9()));
                pt.setPaysecond(AllAdd(pt.getPay10(), pt.getPay11(), pt.getPay12(), pt.getPay1(), pt.getPay2(), pt.getPay3()));
                pt.setPaytotal(pt.getPayfirst().add(pt.getPaysecond()));

                pt.setGivingfirst(AllAdd(pt.getGiving4(), pt.getGiving5(), pt.getGiving6(), pt.getGiving7(), pt.getGiving8(), pt.getGiving9()));
                pt.setGivingsecond(AllAdd(pt.getGiving10(), pt.getGiving11(), pt.getGiving12(), pt.getGiving1(), pt.getGiving2(), pt.getGiving3()));
                pt.setGivingtotal(pt.getGivingfirst().add(pt.getGivingsecond()));
            }
            return _personPlanTables;
        }
        return new ArrayList<PersonPlanTable>();
    }

    private BigDecimal AllAdd(BigDecimal... number) {
        BigDecimal count = new BigDecimal(0);
        for (BigDecimal bd :
                number) {
            count = count.add(bd);
        }
        return count;
    }

    private static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        out.close();
        in.close();
        byteOut.close();
        return dest;
    }

    private int getIndex(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return 0;
    }

    //获取第二层 type=2 groupOrg
    private List<OrgTree> getGroupTree(OrgTree orgTree){
        List<OrgTree> orgTreeList = new ArrayList<>();
      List<OrgTree>  orgTrees =  orgTree.getOrgs();
        for (OrgTree org:
                orgTrees ) {
            orgTreeList.addAll(org.getOrgs());
        }
      return  orgTreeList;
    }
}
