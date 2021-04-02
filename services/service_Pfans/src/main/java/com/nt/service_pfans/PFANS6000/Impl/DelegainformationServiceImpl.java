package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.date.DateUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Delegainformationtax;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationtaxVo;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationMapper;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationtaxMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.utils.LogicalException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class DelegainformationServiceImpl implements DeleginformationService {


    @Autowired
    private DelegainformationMapper delegainformationMapper;

    @Autowired
    private WorkingDayMapper workingdayMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private CoststatisticsService coststatisticsService;

    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    @Autowired
    private DelegainformationtaxMapper delegainformationtaxMapper;
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end

//    @Override
////    public List<DelegainformationVo> getDelegainformation() throws Exception {
////        return delegainformationMapper.getinfo();
////    }

    @Override
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    //public List<DelegainformationVo> getYears(String year,String group_id,List<String> owners) throws Exception {
    public DelegainformationtaxVo getYears(String year, String group_id, List<String> owners) throws Exception {
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end
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
                Integer.parseInt(list.get(6)),Integer.parseInt(list.get(7)),
                Integer.parseInt(list.get(8)),Integer.parseInt(list.get(9)),
                Integer.parseInt(list.get(10)),Integer.parseInt(list.get(11)));

        List<DelegainformationVo> Vo1 = delegainformationMapper.getYears1(year,group_id,Vo);
        for(int i = 0; i < Vo1.size(); i ++){
            Vo.add(Vo1.get(i));
        }
        //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
        DelegainformationtaxVo taxVo = new DelegainformationtaxVo();
        taxVo.setDelegainformationVo(Vo);
        Delegainformationtax tax = new Delegainformationtax();
        tax.setYear(year);
        tax.setGroup_id(group_id);
        List<Delegainformationtax> taxlist = delegainformationtaxMapper.select(tax);
        if(taxlist.size() > 0){
            taxVo.setDelegainformationtaxList(taxlist);
        }
        return taxVo;
        //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end
    }

    @Override
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    //public void updateDeleginformation(List<Delegainformation> delegainformationList, TokenModel tokenModel) throws Exception {
    public void updateDeleginformation(DelegainformationtaxVo taxVo, TokenModel tokenModel) throws Exception {
        List<Delegainformation> delegainformationList = taxVo.getDelegainformationList();
        List<Delegainformationtax> taxList = taxVo.getDelegainformationtaxList();
        if(taxList.size() > 0){
            Delegainformationtax tax = taxList.get(0);
            Delegainformationtax taxup = new Delegainformationtax();
            taxup.setYear(tax.getYear());
            taxup.setGroup_id(tax.getGroup_id());
            List<Delegainformationtax> taxlistup = delegainformationtaxMapper.select(taxup);
            if(taxlistup.size() > 0){
                tax.preUpdate(tokenModel);
                delegainformationtaxMapper.updateByPrimaryKey(tax);
            }
            else{
                tax.preInsert(tokenModel);
                tax.setDelegainformationtax_id(UUID.randomUUID().toString());
                delegainformationtaxMapper.insert(tax);
            }
        }
        //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end
        String sDate = DateUtil.format(new Date(), "MM");
        String sDateyy = DateUtil.format(new Date(), "yyyy");
        for (Delegainformation delegainformation : delegainformationList) {
            if(sDate.equals("04")) {
                delegainformation.setApril("");
            }
            if(sDate.equals("05")){
                delegainformation.setMay("");
            }
            if(sDate.equals("06")){
                delegainformation.setJune("");
            }
            if(sDate.equals("07")){
                delegainformation.setJuly("");
            }
            if(sDate.equals("08")){
                delegainformation.setAugust("");
            }
            if(sDate.equals("09")){
                delegainformation.setSeptember("");
            }
            if(sDate.equals("10")){
                delegainformation.setOctober("");
            }
            if(sDate.equals("11")){
                delegainformation.setNovember("");
            }
            if(sDate.equals("12")){
                delegainformation.setDecember("");
            }
            if(sDate.equals("01")){
                delegainformation.setJanuary("");
            }
            if(sDate.equals("02")){
                delegainformation.setFebruary("");
            }
            if(sDate.equals("03")){
                delegainformation.setMarch("");
            }
            if(!StringUtils.isNullOrEmpty(delegainformation.getDelegainformation_id())){
                delegainformation.preUpdate(tokenModel);
                delegainformationMapper.updateByPrimaryKeySelective(delegainformation);
            }
            else{
                Delegainformation del = new Delegainformation();
                del.setGroup_id(delegainformation.getGroup_id());
                del.setAccount(delegainformation.getAccount());
                //add ccm 1224 添加年度条件 fr
                del.setYear(delegainformation.getYear());
//                if(Integer.valueOf(sDate) < 4)
//                {
//                    del.setYear(String.valueOf(Integer.valueOf(sDateyy) - 1));
//                }
                //add ccm 1224 添加年度条件 to
                List<Delegainformation> tion = delegainformationMapper.select(del);
                if(tion.size() > 0){
                    delegainformation.preUpdate(tokenModel);
                    delegainformation.setDelegainformation_id(tion.get(0).getDelegainformation_id());
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

    //add ccm 1027 定时任务  每月工数统计截止日第二天自动保存工数
    @Scheduled(cron="0 0 3 * * ?")
    public void saveDelegaTask()throws Exception {
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        List<Dictionary> dictionaryL = dictionaryService.getForSelect("BP027");
        TokenModel tokenModel = new TokenModel();
        if(dictionaryL.size()>0)
        {
            String ymd = sfymd.format(new Date());
            String y = ymd.substring(0,4);
            String mm = ymd.substring(5,7);
            String dd = ymd.substring(8,10);

            if(Integer.valueOf(mm)<4)
            {
                y = String.valueOf(Integer.valueOf(y) - 1);
            }

            if(Integer.valueOf(dd) == Integer.valueOf(dictionaryL.get(0).getValue1()) + 1)
            {
                List<DelegainformationVo> delvoList = new ArrayList<DelegainformationVo>();
                List<Delegainformation> delList = new ArrayList<Delegainformation>();
                List<String> a = new ArrayList<String>();
                List<OrgTree> orgsList =  orgTreeService.getById(new OrgTree());
                for(OrgTree orgTree:orgsList)
                {
                    for(OrgTree org:orgTree.getOrgs())
                    {
                        for(OrgTree or:org.getOrgs())
                        {
                            if(or.getType().equals("2"))
                            {
                                //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
                                //delvoList = getYears(y,or.get_id(),a);
                                DelegainformationtaxVo taxvo = getYears(y,or.get_id(),a);
                                delvoList = taxvo.getDelegainformationVo();
                                //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end
                                if(delvoList.size()>0)
                                {
                                    for(DelegainformationVo vo : delvoList)
                                    {
                                        Delegainformation del = new Delegainformation();
                                        del.setDelegainformation_id(vo.getDelegainformation_id());
                                        //四月
                                        del.setApril(vo.getApril());
                                        if(StringUtils.isNullOrEmpty(vo.getApril()))
                                        {
                                            del.setApril("0");
                                        }
                                        //五月
                                        del.setMay(vo.getMay());
                                        if(StringUtils.isNullOrEmpty(vo.getMay()))
                                        {
                                            del.setMay("0");
                                        }
                                        //六月
                                        del.setJune(vo.getJune());
                                        if(StringUtils.isNullOrEmpty(vo.getJune()))
                                        {
                                            del.setJune("0");
                                        }
                                        //七月
                                        del.setJuly(vo.getJuly());
                                        if(StringUtils.isNullOrEmpty(vo.getJuly()))
                                        {
                                            del.setJuly("0");
                                        }
                                        //八月
                                        del.setAugust(vo.getAugust());
                                        if(StringUtils.isNullOrEmpty(vo.getAugust()))
                                        {
                                            del.setAugust("0");
                                        }
                                        //九月
                                        del.setSeptember(vo.getSeptember());
                                        if(StringUtils.isNullOrEmpty(vo.getSeptember()))
                                        {
                                            del.setSeptember("0");
                                        }
                                        //十月
                                        del.setOctober(vo.getOctober());
                                        if(StringUtils.isNullOrEmpty(vo.getOctober()))
                                        {
                                            del.setOctober("0");
                                        }
                                        //十一月
                                        del.setNovember(vo.getNovember());
                                        if(StringUtils.isNullOrEmpty(vo.getNovember()))
                                        {
                                            del.setNovember("0");
                                        }
                                        //十二月
                                        del.setDecember(vo.getDecember());
                                        if(StringUtils.isNullOrEmpty(vo.getDecember()))
                                        {
                                            del.setDecember("0");
                                        }
                                        //明年一月
                                        del.setJanuary(vo.getJanuary());
                                        if(StringUtils.isNullOrEmpty(vo.getJanuary()))
                                        {
                                            del.setJanuary("0");
                                        }
                                        //明年二月
                                        del.setFebruary(vo.getFebruary());
                                        if(StringUtils.isNullOrEmpty(vo.getFebruary()))
                                        {
                                            del.setFebruary("0");
                                        }
                                        //明年三月
                                        del.setMarch(vo.getMarch());
                                        if(StringUtils.isNullOrEmpty(vo.getMarch()))
                                        {
                                            del.setMarch("0");
                                        }
                                        del.setYear(vo.getYear());
                                        del.setAccount(vo.getAccount());
                                        del.setGroup_id(vo.getGroup_id());
                                        delList.add(del);
                                    }
                                    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
                                    taxvo.setDelegainformationList(delList);
                                    updateDeleginformation(taxvo,tokenModel);
                                    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end
                                    Coststatistics coststatistics = new Coststatistics();
                                    coststatisticsService.insertCoststatistics(or.get_id(),y,coststatistics,tokenModel);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //add ccm 1027 定时任务  每月工数统计截止日第二天自动保存工数
}
