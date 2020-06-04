package com.nt.service_pfans.PFANS6000.Impl;


import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.utils.LogicalException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class DelegainformationServiceImpl implements DeleginformationService {


    @Autowired
    private DelegainformationMapper delegainformationMapper;

    @Autowired
    private WorkingDayMapper workingdayMapper;


//    @Override
////    public List<DelegainformationVo> getDelegainformation() throws Exception {
////        return delegainformationMapper.getinfo();
////    }

    @Override
    public List<DelegainformationVo> getYears(String year,String group_id,List<String> owners) throws Exception {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        int int4 = countWorkDay(Integer.parseInt(year),4);
        int int5 = countWorkDay(Integer.parseInt(year),5);
        int int6 = countWorkDay(Integer.parseInt(year),6);
        int int7 = countWorkDay(Integer.parseInt(year),7);
        int int8 = countWorkDay(Integer.parseInt(year),8);
        int int9 = countWorkDay(Integer.parseInt(year),9);
        int int10 = countWorkDay(Integer.parseInt(year),10);
        int int11 = countWorkDay(Integer.parseInt(year),11);
        int int12 = countWorkDay(Integer.parseInt(year),12);
        int int1 = countWorkDay(Integer.parseInt(year) + 1,1);
        int int2 = countWorkDay(Integer.parseInt(year) + 1,2);
        int int3 = countWorkDay(Integer.parseInt(year) + 1,3);
        List<String> list= new ArrayList<String>();
        list.add(String.valueOf(int4));
        list.add(String.valueOf(int5));
        list.add(String.valueOf(int6));
        list.add(String.valueOf(int7));
        list.add(String.valueOf(int8));
        list.add(String.valueOf(int9));
        list.add(String.valueOf(int10));
        list.add(String.valueOf(int11));
        list.add(String.valueOf(int12));
        list.add(String.valueOf(int1));
        list.add(String.valueOf(int2));
        list.add(String.valueOf(int3));
        List<WorkingDay> workingdayList = workingdayMapper.getworkingyear(year + "04",String.valueOf(Integer.parseInt(year) + 1) + "03");
        for(WorkingDay day : workingdayList){
            if(day.getYears().equals("04")){
                int4 = int4 - Integer.parseInt(day.getTenantid());
                list.set(0,String.valueOf(int4));
            }
            if(day.getYears().equals("05")){
                int5 = int5 - Integer.parseInt(day.getTenantid());
                list.set(1,String.valueOf(int5));
            }
            if(day.getYears().equals("06")){
                int6 = int6 - Integer.parseInt(day.getTenantid());
                list.set(2,String.valueOf(int6));
            }
            if(day.getYears().equals("07")){
                int7 = int7 - Integer.parseInt(day.getTenantid());
                list.set(3,String.valueOf(int7));
            }
            if(day.getYears().equals("08")){
                int8 = int8 - Integer.parseInt(day.getTenantid());
                list.set(4,String.valueOf(int8));
            }
            if(day.getYears().equals("09")){
                int9 = int9 - Integer.parseInt(day.getTenantid());
                list.set(5,String.valueOf(int9));
            }
            if(day.getYears().equals("10")){
                int10 = int10 - Integer.parseInt(day.getTenantid());
                list.set(6,String.valueOf(int10));
            }
            if(day.getYears().equals("11")){
                int11 = int11 - Integer.parseInt(day.getTenantid());
                list.set(7,String.valueOf(int11));
            }
            if(day.getYears().equals("12")){
                int12 = int12 - Integer.parseInt(day.getTenantid());
                list.set(8,String.valueOf(int12));
            }
            if(day.getYears().equals("01")){
                int1 = int1 - Integer.parseInt(day.getTenantid());
                list.set(9,String.valueOf(int1));
            }
            if(day.getYears().equals("02")){
                int2 = int2 - Integer.parseInt(day.getTenantid());
                list.set(10,String.valueOf(int2));
            }
            if(day.getYears().equals("03")){
                int3 = int3 - Integer.parseInt(day.getTenantid());
                list.set(11,String.valueOf(int3));
            }
        }
        List<DelegainformationVo> Vo = delegainformationMapper.getYears(year,group_id,Integer.parseInt(list.get(0)),Integer.parseInt(list.get(1)),
                Integer.parseInt(list.get(2)),Integer.parseInt(list.get(3)),
                Integer.parseInt(list.get(4)),Integer.parseInt(list.get(5)),
                Integer.parseInt(list.get(5)),Integer.parseInt(list.get(7)),
                Integer.parseInt(list.get(8)),Integer.parseInt(list.get(9)),
                Integer.parseInt(list.get(10)),Integer.parseInt(list.get(11)));

        List<DelegainformationVo> Vo1 = delegainformationMapper.getYears1(year,group_id,Vo);
        for(int i = 0; i < Vo1.size(); i ++){
            Vo.add(Vo1.get(i));
        }
        return Vo;
    }

    @Override
    public void updateDeleginformation(List<Delegainformation> delegainformationList, TokenModel tokenModel) throws Exception {
        for (Delegainformation delegainformation : delegainformationList) {
            if(!StringUtils.isNullOrEmpty(delegainformation.getDelegainformation_id())){
                delegainformation.preUpdate(tokenModel);
                delegainformationMapper.updateByPrimaryKeySelective(delegainformation);
            }
            else{
                if(!StringUtils.isNullOrEmpty(delegainformation.getAccount())){
                    delegainformation.preInsert(tokenModel);
                    delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
                    delegainformationMapper.insert(delegainformation);
                }
            }
        }
    }

    public static int countWorkDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        // 月份是从0开始计算，所以需要减1
        c.set(Calendar.MONTH, month - 1);

        // 当月最后一天的日期
        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 开始日期为1号
        int start = 1;
        // 计数
        int count = 0;
        while (start <= max) {
            c.set(Calendar.DAY_OF_MONTH, start);
            if (isWorkDay(c)) {
                count++;
            }
            start++;
        }
        return count;
    }

    // 判断是否工作日（未排除法定节假日，由于涉及到农历节日，处理很麻烦）
    public static boolean isWorkDay(Calendar c) {
        // 获取星期,1~7,其中1代表星期日，2代表星期一 ... 7代表星期六
        int week = c.get(Calendar.DAY_OF_WEEK);
        // 不是周六和周日的都认为是工作日
        return week != Calendar.SUNDAY && week != Calendar.SATURDAY;
    }
}
