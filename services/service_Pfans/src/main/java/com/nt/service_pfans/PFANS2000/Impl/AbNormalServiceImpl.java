package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AbNormalServiceImpl implements AbNormalService {

    @Autowired
    private AbNormalMapper abNormalMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;
    @Autowired
    private ReplacerestMapper replacerestMapper;
    @Autowired
    private WorkingDayMapper workingDayMapper;
    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;
    @Autowired
    private OvertimeMapper overtimeMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private PunchcardRecordService punchcardRecordService;

    @Override
    public List<AbNormal> list(AbNormal abNormal) throws Exception {
        return abNormalMapper.select(abNormal);
    }

    //add-ws-6/8-禅道035
    @Override
    public List<AbNormal> list2(AbNormal abNormal) throws Exception {
        return abNormalMapper.select(abNormal);
    }

    //add-ws-6/8-禅道035
    @Override
    public List<AbNormal> selectAbNormalParent(String userid) throws Exception {
        return abNormalMapper.selectAbNormalParent(userid);
    }

    @Override
    public Double getSickleave(String userid) throws Exception {
        Double a = abNormalMapper.selectAbNormalDate(userid);
        if (a == null) {
            a = 0.0;
        }
        return a;
    }

    @Override
    public void insert(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        String strtus = abNormal.getStatus();

        abNormal.preInsert(tokenModel);
//        //总经理新建自动通过
//        if(strtus.equals(AuthConstants.APPROVED_FLAG_YES)){
//            abNormal.setStatus(AuthConstants.APPROVED_FLAG_YES);
//        }
        abNormal.setAbnormalid(UUID.randomUUID().toString());
        abNormalMapper.insert(abNormal);
    }

    //ADD_FJL_0904  添加删除data
    @Override
    public void delete(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preUpdate(tokenModel);
        //逻辑删除
        abNormal.setStatus(AuthConstants.DEL_FLAG_DELETE);
        abNormalMapper.updateByPrimaryKey(abNormal);
    }

    //ADD_FJL_0904  添加删除data
    @Override
    public void upd(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preUpdate(tokenModel);
        abNormalMapper.updateByPrimaryKey(abNormal);
        //add ccm 2020708 异常实时反应
        if(abNormal.getStatus().equals("4"))
        {
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(abNormal.getOccurrencedate());
                Calendar calend = Calendar.getInstance();
                calend.setTime(abNormal.getFinisheddate());
                for(Calendar item = calStart;item.compareTo(calend) <= 0;item.add(Calendar.DAY_OF_MONTH,1))
                {
                    punchcardRecordService.methodAttendance_b(item,abNormal.getUser_id());
                }
        }

        if(abNormal.getStatus().equals("7"))
        {
            List<Date> Longlist = new ArrayList<Date>();
            SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");

            Calendar calStart = Calendar.getInstance();
            calStart.setTime(abNormal.getOccurrencedate());
            Longlist.add(sf1ymd.parse(sf1ymd.format(calStart.getTime())));

            Calendar calend = Calendar.getInstance();
            calend.setTime(abNormal.getFinisheddate());
            Longlist.add(sf1ymd.parse(sf1ymd.format(calend.getTime())));

            Calendar calReStart = Calendar.getInstance();
            calReStart.setTime(abNormal.getReoccurrencedate());
            Longlist.add(sf1ymd.parse(sf1ymd.format(calReStart.getTime())));

            Calendar calReend = Calendar.getInstance();
            calReend.setTime(abNormal.getRefinisheddate());
            Longlist.add(sf1ymd.parse(sf1ymd.format(calReend.getTime())));

            Collections.sort(Longlist);
            if(Longlist.size() == 4)
            {
                calStart.setTime(Longlist.get(0));
                calend.setTime(Longlist.get(3));
                for(Calendar item = calStart;item.compareTo(calend) <= 0;item.add(Calendar.DAY_OF_MONTH,1))
                {
                    punchcardRecordService.methodAttendance_b(item,abNormal.getUser_id());
                }
            }

        }

        //add ccm 2020708 异常实时反应

    }

    @Override
    public AbNormal One(String abnormalid) throws Exception {
        return abNormalMapper.selectByPrimaryKey(abnormalid);
    }

    @Override
    public Map<String, String> cklength(AbNormal abNormal) throws Exception {

        Map<String, String> rst = new HashMap<String, String>();
        double lengths = 1;
        double relengths = 1;
        Calendar calendar = Calendar.getInstance();
        //当前年度
        int year = 0;
        int month = calendar.get(Calendar.MONTH);
        if(month >= 1 && month <= 3) {
            year = calendar.get(Calendar.YEAR) - 1;
        }else {
            year = calendar.get(Calendar.YEAR);
        }

        if ("4".equals(abNormal.getLengthtime())) {
            lengths = 0.5;
        }
        if ("PR013006".equals(abNormal.getErrortype())) {
            lengths = Convert.toDouble(abNormal.getLengthtime()) / 8;
        }
        if ("PR013005".equals(abNormal.getErrortype())) {
            if (StrUtil.isNotBlank(abNormal.getLengthtime())) {
                lengths = Convert.toDouble(abNormal.getLengthtime()) / 8;
            }
        }
        //                ADD_FJL_05/26  --添加非空判断
//        if(StrUtil.isNotBlank(abNormal.getRelengthtime())) {
        if (StrUtil.isNotBlank(abNormal.getRelengthtime()) && !abNormal.getRelengthtime().equals("0")) {
            //                ADD_FJL_05/26  --添加非空判断
            if ("4".equals(abNormal.getRelengthtime())) {
                relengths = 0.5;
                lengths = relengths - lengths;
            }
            if ("PR013006".equals(abNormal.getErrortype())) {
                relengths = Convert.toDouble(abNormal.getRelengthtime()) / 8;
                lengths = relengths - lengths;
            }

            if ("PR013005".equals(abNormal.getErrortype()) && Convert.toDouble(abNormal.getRelengthtime()) > 8) {
                relengths = Convert.toDouble(abNormal.getRelengthtime()) / 8;
                lengths = relengths;
            }
        }

        if ("PR013005".equals(abNormal.getErrortype())) {
            TokenModel tokenModel =new TokenModel();
            if (abNormal.getStatus() == null || abNormal.getStatus().isEmpty()) {
                List<String> ls = new ArrayList<String>();
                ls.add(abNormal.getUser_id());
                //List<AnnualLeave> listannualLeave = annualLeaveMapper.getDataList(ls);
                tokenModel.setOwnerList(ls);
                List<AnnualLeave> listannualLeave = annualLeaveService.getDataList(tokenModel);
                double shengyu = 0;
                String annual_avg_remaing = null;
                if (listannualLeave.size() > 0) {
                    shengyu = listannualLeave.get(0).getRemaining_annual_leave_thisyear().doubleValue();
                    annual_avg_remaing = listannualLeave.get(0).getAnnual_avg_remaining();
                    if(annual_avg_remaing!=null && !annual_avg_remaing.isEmpty() && !annual_avg_remaing.equals("-"))
                    {
                        if(Double.valueOf(annual_avg_remaing) > 0)
                        {
                            String Timecheck = annual_avg_remaing;
                            rst.put("dat", Timecheck);
                            rst.put("error", abNormal.getErrortype());
                            rst.put("can", "yes");
                        } else {
                            rst.put("dat", "");
                            rst.put("can", "no");
                        }
                    }
                    else
                    {
                        if (shengyu > 0) {
                            String Timecheck = String.valueOf(shengyu);
                            rst.put("dat", Timecheck);
                            rst.put("error", abNormal.getErrortype());
                            rst.put("can", "yes");
                        } else {
                            rst.put("dat", "");
                            rst.put("can", "no");
                        }
                    }
                } else {
                    rst.put("dat", "");
                    rst.put("can", "no");
                }

            }
            else if (abNormal.getStatus().equals("0") || abNormal.getStatus().equals("3")) {
                List<String> ls = new ArrayList<String>();
                ls.add(abNormal.getUser_id());
                //离职人员剩余年休
                String annual_avg_remaing = null;
                //List<AnnualLeave> list = annualLeaveMapper.getDataList(ls);
                tokenModel.setOwnerList(ls);
                List<AnnualLeave> list = annualLeaveService.getDataList(tokenModel);
                if (list.size() > 0) {
                    //upd ccm 2020/6/9
                    annual_avg_remaing = list.get(0).getAnnual_avg_remaining();
                    if(annual_avg_remaing!=null && !annual_avg_remaing.isEmpty() && !annual_avg_remaing.equals("-"))
                    {
                        if ((Double.valueOf(annual_avg_remaing) - list.get(0).getAnnual_leave_shenqingzhong().doubleValue()) >= lengths) {
                            String Timecheck = String.valueOf(Double.valueOf(annual_avg_remaing) - list.get(0).getAnnual_leave_shenqingzhong().doubleValue());
                            rst.put("dat", Timecheck);
                            rst.put("error", abNormal.getErrortype());
                            rst.put("can", "yes");
                        } else {
                            rst.put("dat", "");
                            rst.put("can", "no");
                        }
                    }
                    else
                    {
                        if ((list.get(0).getRemaining_annual_leave_thisyear().doubleValue() - list.get(0).getAnnual_leave_shenqingzhong().doubleValue()) >= lengths) {
                            String Timecheck = String.valueOf(list.get(0).getRemaining_annual_leave_thisyear().doubleValue() - list.get(0).getAnnual_leave_shenqingzhong().doubleValue());
                            rst.put("dat", Timecheck);
                            rst.put("error", abNormal.getErrortype());
                            rst.put("can", "yes");
                        } else {
                            rst.put("dat", "");
                            rst.put("can", "no");
                        }
                    }
                } else {
                    rst.put("dat", "");
                    rst.put("can", "no");
                }
            }
            else if (abNormal.getStatus().equals("4") || abNormal.getStatus().equals("6")) {
                List<String> ls = new ArrayList<String>();
                ls.add(abNormal.getUser_id());
                //List<AnnualLeave> listannualLeave = annualLeaveMapper.getDataList(ls);
                tokenModel.setOwnerList(ls);
                List<AnnualLeave> listannualLeave = annualLeaveService.getDataList(tokenModel);
                double shengyu = 0;
                String annual_avg_remaing = null;
                if (listannualLeave.size() > 0) {
                    AbNormal ab = new AbNormal();
                    ab.setAbnormalid(abNormal.getAbnormalid());
                    List<AbNormal> abnormalone = abNormalMapper.select(ab);
                    if(abnormalone.size()>0)
                    {
                        shengyu = listannualLeave.get(0).getRemaining_annual_leave_thisyear().doubleValue() + Double.valueOf(abnormalone.get(0).getLengthtime())/8;
                        annual_avg_remaing = listannualLeave.get(0).getAnnual_avg_remaining() + Double.valueOf(abnormalone.get(0).getLengthtime())/8;
                    }
                    else
                    {
                        shengyu = listannualLeave.get(0).getRemaining_annual_leave_thisyear().doubleValue();
                        annual_avg_remaing = listannualLeave.get(0).getAnnual_avg_remaining();
                    }

                    List<AbNormal> list = abNormalMapper.selectfinishAnnuel1(abNormal.getUser_id(),String.valueOf(year));
                    if (list.size() > 0) {
                        double shenqing = 0;
                        for (AbNormal a : list) {
                            if (a.getStatus().equals("2") || a.getStatus().equals("3")) {
                                shenqing = shenqing + Double.valueOf(a.getLengthtime());
                            } else if (a.getStatus().equals("5") || a.getStatus().equals("6")) {
                                shenqing = shenqing + Double.valueOf(a.getRelengthtime());
                            }
                        }
                        shenqing = shenqing / 8;

                        if(annual_avg_remaing!=null && !annual_avg_remaing.isEmpty() && !annual_avg_remaing.contains("-"))
                        {
                            if (Double.valueOf(annual_avg_remaing) - shenqing >= lengths) {
                                String Timecheck = String.valueOf(Double.valueOf(annual_avg_remaing) - shenqing);
                                rst.put("dat", Timecheck);
                                rst.put("error", abNormal.getErrortype());
                                rst.put("can", "yes");
                            } else {
                                rst.put("dat", "");
                                rst.put("can", "no");
                            }
                        }
                        else
                        {
                            if (shengyu - shenqing >= lengths) {
                                String Timecheck = String.valueOf(shengyu - shenqing);

                                rst.put("dat", Timecheck);
                                rst.put("error", abNormal.getErrortype());
                                rst.put("can", "yes");
                            } else {
                                rst.put("dat", "");
                                rst.put("can", "no");
                            }
                        }
                    } else {
                        if(annual_avg_remaing!=null && !annual_avg_remaing.isEmpty() && !annual_avg_remaing.equals("-"))
                        {
                            if (Double.valueOf(annual_avg_remaing) > 0) {
                                String Timecheck = annual_avg_remaing;
                                rst.put("dat", Timecheck);
                                rst.put("error", abNormal.getErrortype());
                                rst.put("can", "yes");
                            } else {
                                rst.put("dat", "");
                                rst.put("can", "no");
                            }
                        }
                        else
                        {
                            if (shengyu > 0) {
                                String Timecheck = String.valueOf(shengyu);
                                rst.put("dat", Timecheck);
                                rst.put("error", abNormal.getErrortype());
                                rst.put("can", "yes");
                            } else {
                                rst.put("dat", "");
                                rst.put("can", "no");
                            }
                        }
                    }
                } else {
                    rst.put("dat", "");
                    rst.put("can", "no");
                }
            }
        }
        else if ("PR013006".equals(abNormal.getErrortype())) {

            List<restViewVo> list = annualLeaveMapper.getrest(abNormal.getUser_id());
            String dat = "";
            for (restViewVo item : list) {
                if (Double.parseDouble(item.getRestdays()) != 0) {
                    if (lengths >= 0) {
                        lengths = lengths - Double.parseDouble(item.getRestdays());
                        dat += dat + "," + item.getApplicationdate();
                        if (lengths <= 0) {
                            String check = String.valueOf(list.get(0).getRestdays());
                            dat = dat.substring(1, dat.length());
                            rst.put("checkdat", check);
                            rst.put("error", abNormal.getErrortype());
                            rst.put("dat", dat);
                            rst.put("can", "yes");
                            return rst;
                        }
                    } else {
                        String check = String.valueOf(list.get(0).getRestdays());
                        rst.put("checkdat", check);
                        rst.put("error", abNormal.getErrortype());
                        rst.put("dat", "");
                        rst.put("can", "yes");
                    }
                }
            }
            rst.put("dat", "");
            rst.put("can", "no");
            //upd-ws-禅道159
        } else if ("PR013007".equals(abNormal.getErrortype())) {
            if (StrUtil.isNotBlank(abNormal.getRelengthtime()) && abNormal.getRelengthtime().equals("0")) {
                relengths = 0.5;
                lengths = relengths - lengths;
            }
            List<restViewVo> list = annualLeaveMapper.getrest2(abNormal.getUser_id());
            String dat = "";
            for (restViewVo item : list) {
                //add-ws-09/01-禅道任务484
                int scale = 1;//设置位数
                int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                BigDecimal bd = new BigDecimal(item.getRestdays());
                bd = bd.setScale(scale, roundingMode);
                BigDecimal bd1 = new BigDecimal(item.getShenqinglengthtime());
                bd1 = bd1.setScale(scale, roundingMode);
                BigDecimal bd2 = bd.subtract(bd1);
                if (bd.compareTo(bd1) == 1) {
                    //add-ws-09/01-禅道任务484
                    if (lengths >= 0) {
                        lengths = lengths - Double.parseDouble(item.getRestdays());
                        dat += dat + "," + item.getApplicationdate();
                        if (lengths <= 0) {
                            String check = String.valueOf(bd2);
                            rst.put("checkdat", check);
                            rst.put("error", abNormal.getErrortype());
                            rst.put("dat", "");
                            rst.put("can", "yes");
                            return rst;
                        }
                    } else {
                        String check = String.valueOf(bd2);
                        rst.put("checkdat", check);
                        rst.put("error", abNormal.getErrortype());
                        rst.put("dat", "");
                        rst.put("can", "yes");
                        return rst;
                    }
//add-ws-09/01-禅道任务484
                }else if (bd.compareTo(bd1) == -1) {
                    String check =  String.valueOf(Convert.toDouble(abNormal.getLengthtime()) / 8);
                    rst.put("checkdat", check);
                    rst.put("error", abNormal.getErrortype());
                    rst.put("dat", "");
                    rst.put("can", "yes");
                    return rst;
                }else if (bd.compareTo(bd1) == 0) {
                    String check =  String.valueOf(Convert.toDouble(abNormal.getLengthtime()) / 8);
                    rst.put("checkdat", check);
                    rst.put("error", abNormal.getErrortype());
                    rst.put("dat", "");
                    rst.put("can", "yes");
                    return rst;
                }
            }
//upd-ws-禅道159

            rst.put("dat", "");
            rst.put("can", "no");
        }

        return rst;
    }

    @Override
    public void updateOvertime(AbNormal abNormal) throws Exception {
        AbNormal ab = abNormalMapper.selectByPrimaryKey(abNormal.getAbnormalid());
        Double length = Convert.toDouble(ab.getRelengthtime());
        if ("PR013006".equals(ab.getErrortype())) {
            String[] ids = ab.getRestdate().split(",");
            for (String id : ids) {
                Overtime ov = overtimeMapper.selectByPrimaryKey(id);
                Double ov_length = 8D;
//                ADD_FJL_05/26  --添加非空判断
                Double diff = 0D;
                if (!StringUtils.isNullOrEmpty(ov.getUsedlength())) {
                    diff = Convert.toDouble(ab.getRelengthtime()) - ov_length + Convert.toDouble(ov.getUsedlength());
                } else {
                    diff = Convert.toDouble(ab.getRelengthtime()) - ov_length;
                }
//                ADD_FJL_05/26  --添加非空判断
                if (diff > 0) {
                    ov.setUsedlength("8");
                } else {
                    ov.setUsedlength(Convert.toStr(ov_length + diff));
                }
                overtimeMapper.updateByPrimaryKeySelective(ov);
            }
        }

    }

    //add_fjl_05/26 --添加代休剩余
    public List<restViewVo> getRestday(String user_id) throws Exception {
        return abNormalMapper.getRestday(user_id);
    }

    //add_fjl_05/26 --添加代休剩余
    //代休
    public void updateReplacerest(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        Replacerest replacerest = new Replacerest();
        replacerest.setUser_id(abNormal.getUser_id());
        replacerest.setRecognitionstate("0");
        List<Replacerest> recruitlist = replacerestMapper.select(replacerest);
        for (Replacerest re : recruitlist) {
            if (abNormal.getErrortype().equals("PR013007")) //代休特殊
            {
                String strDuration = String.valueOf(Double.valueOf(re.getDuration_teshu()) - Double.valueOf(abNormal.getLengthtime()));
                replacerest.setDuration_teshu(strDuration);
                if (strDuration.equals("0")) {
                    replacerest.setDuration_teshu(null);
                    replacerest.setRecognitionstate("1");
                }
            }
            if (abNormal.getErrortype().equals("PR013006")) //代休周末
            {
                String strDuration = String.valueOf(Double.valueOf(re.getDuration()) - Double.valueOf(abNormal.getLengthtime()));
                replacerest.setDuration(strDuration);
                if (strDuration.equals("0")) {
                    replacerest.setDuration(null);
                    replacerest.setRecognitionstate("1");
                }
            }
            replacerest.preUpdate(tokenModel);
            replacerestMapper.updateByPrimaryKey(replacerest);
        }

    }

    //    add_fjl_06/16  -- 添加异常申请每天累计不超过8小时check  start
    public Double getLeaveNumber(AbNormal abNormal) throws Exception {
        //List<AbNormal> abNormalList =  abNormalMapper.getLeaveNumber(abNormal.getUser_id());
        AbNormal ab = new AbNormal();
        ab.setUser_id(abNormal.getUser_id());
        List<AbNormal> abNormalList = abNormalMapper.select(ab);
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
        String odate = "";
        String fdate = "";
        if (StringUtils.isNullOrEmpty(abNormal.getStatus()) || Integer.parseInt(abNormal.getStatus()) < 4) {
            odate = st.format(abNormal.getOccurrencedate());
            fdate = st.format(abNormal.getFinisheddate());
        } else {
            odate = st.format(abNormal.getReoccurrencedate());
            fdate = st.format(abNormal.getRefinisheddate());
        }
        double retime = 0.0;
        double time = 0.0;
        double timeSum = 0.0;
        if (abNormalList.size() > 0) {
            //如果这条数据已经存在，筛选掉
            if (!StringUtils.isNullOrEmpty(abNormal.getAbnormalid())) {
                abNormalList = abNormalList.stream().filter(item -> !abNormal.getAbnormalid().equals(item.getAbnormalid())).collect(Collectors.toList());
            }
            for (int a = 0; a < abNormalList.size(); a++) {
                //实际日期
                if (Integer.parseInt(abNormalList.get(a).getStatus()) > 4) {
                    //把新建的开始日包含在DB区间内
                    if (odate.compareTo(st.format(abNormalList.get(a).getReoccurrencedate())) >= 0
                            && odate.compareTo(st.format(abNormalList.get(a).getRefinisheddate())) <= 0
                            && fdate.compareTo(st.format(abNormalList.get(a).getRefinisheddate())) >= 0) {
                        double da = Double.valueOf(st.format(abNormalList.get(a).getRefinisheddate()).replace("-", "")) - Double.valueOf(odate.replace("-", "")) + 1;
                        if (da == 1 && fdate.compareTo(st.format(abNormalList.get(a).getRefinisheddate())) == 0 && st.format(abNormalList.get(a).getReoccurrencedate()).compareTo(st.format(abNormalList.get(a).getRefinisheddate())) == 0) {
                            retime += Double.valueOf(abNormalList.get(a).getRelengthtime());
                        } else {
                            retime += (da) * 8;
                        }
                        //把DB区间的开始日包含在新建区间内
                    } else if (st.format(abNormalList.get(a).getReoccurrencedate()).compareTo(odate) >= 0
                            && st.format(abNormalList.get(a).getReoccurrencedate()).compareTo(fdate) <= 0
                            && st.format(abNormalList.get(a).getRefinisheddate()).compareTo(fdate) >= 0) {
                        double dr = Double.valueOf(fdate.replace("-", "")) - Double.valueOf(st.format(abNormalList.get(a).getReoccurrencedate()).replace("-", "")) + 1;
                        if (dr == 1 && st.format(abNormalList.get(a).getReoccurrencedate()).compareTo(st.format(abNormalList.get(a).getRefinisheddate())) == 0) {
                            retime += Double.valueOf(abNormalList.get(a).getRelengthtime());
                        } else {
                            retime += (dr) * 8;
                        }
                        // 新建包含DB,取DB中的时间长度
                    } else if (st.format(abNormalList.get(a).getReoccurrencedate()).compareTo(odate) > 0
                            && st.format(abNormalList.get(a).getRefinisheddate()).compareTo(fdate) < 0) {
                        //加餐，哺乳（女）
                        if (abNormalList.get(a).getErrortype().equals("PR013022")) {
                            double t = Double.valueOf(st.format(abNormalList.get(a).getRefinisheddate()).replace("-", "")) - Double.valueOf(st.format(abNormalList.get(a).getReoccurrencedate()).replace("-", "")) + 1;
                            retime += Double.valueOf(abNormalList.get(a).getRelengthtime()) * t;
                        } else {
                            retime += Double.valueOf(abNormalList.get(a).getRelengthtime());
                        }
                        //DB包含新建，取新建的时间长度
                    } else if (odate.compareTo(st.format(abNormalList.get(a).getReoccurrencedate())) >= 0
                            && fdate.compareTo(st.format(abNormalList.get(a).getRefinisheddate())) <= 0) {
                        if (odate.compareTo(st.format(abNormalList.get(a).getReoccurrencedate())) == 0
                                && fdate.compareTo(st.format(abNormalList.get(a).getRefinisheddate())) == 0) {
                            retime += Double.valueOf(abNormalList.get(a).getRelengthtime());
                        } else {
                            retime += Double.valueOf(abNormal.getRelengthtime());
                        }
                    }
                } else { //预计日期
                    //把新建的开始日包含在DB区间内
                    if (odate.compareTo(st.format(abNormalList.get(a).getOccurrencedate())) >= 0
                            && odate.compareTo(st.format(abNormalList.get(a).getFinisheddate())) <= 0
                            && fdate.compareTo(st.format(abNormalList.get(a).getFinisheddate())) >= 0) {
                        double da = Double.valueOf(st.format(abNormalList.get(a).getFinisheddate()).replace("-", "")) - Double.valueOf(odate.replace("-", "")) + 1;
                        if (da == 1 && fdate.compareTo(st.format(abNormalList.get(a).getFinisheddate())) == 0 && st.format(abNormalList.get(a).getOccurrencedate()).compareTo(st.format(abNormalList.get(a).getFinisheddate())) == 0) {
                            time += Double.valueOf(abNormalList.get(a).getLengthtime());
                        } else {
                            time += (da) * 8;
                        }
                        //把DB区间的开始日包含在新建区间内
                    } else if (st.format(abNormalList.get(a).getOccurrencedate()).compareTo(odate) >= 0
                            && st.format(abNormalList.get(a).getOccurrencedate()).compareTo(fdate) <= 0
                            && st.format(abNormalList.get(a).getFinisheddate()).compareTo(fdate) >= 0) {
                        double dr = Double.valueOf(fdate.replace("-", "")) - Double.valueOf(st.format(abNormalList.get(a).getOccurrencedate()).replace("-", "")) + 1;
                        if (dr == 1 && st.format(abNormalList.get(a).getOccurrencedate()).compareTo(st.format(abNormalList.get(a).getFinisheddate())) == 0) {
                            time += Double.valueOf(abNormalList.get(a).getLengthtime());
                        } else {
                            time += (dr) * 8;
                        }
                        // 新建包含DB,取DB中的时间长度
                    } else if (st.format(abNormalList.get(a).getOccurrencedate()).compareTo(odate) > 0
                            && st.format(abNormalList.get(a).getFinisheddate()).compareTo(fdate) < 0) {
                        //加餐，哺乳（女）
                        if (abNormalList.get(a).getErrortype().equals("PR013022")) {
                            double t = Double.valueOf(st.format(abNormalList.get(a).getFinisheddate()).replace("-", "")) - Double.valueOf(st.format(abNormalList.get(a).getOccurrencedate()).replace("-", "")) + 1;
                            time += Double.valueOf(abNormalList.get(a).getLengthtime()) * t;
                        } else {
                            time += Double.valueOf(abNormalList.get(a).getLengthtime());
                        }
                        //DB包含新建，取新建的时间长度
                    } else if (odate.compareTo(st.format(abNormalList.get(a).getOccurrencedate())) >= 0
                            && fdate.compareTo(st.format(abNormalList.get(a).getFinisheddate())) <= 0) {
                        if (odate.compareTo(st.format(abNormalList.get(a).getOccurrencedate())) == 0
                                && fdate.compareTo(st.format(abNormalList.get(a).getFinisheddate())) == 0) {
                            time += Double.valueOf(abNormalList.get(a).getLengthtime());
                        } else {
                            time += Double.valueOf(abNormal.getLengthtime());
                        }
                    }
                }
                timeSum = retime + time;
            }
        }
        return timeSum;
    }
    //    add_fjl_06/16  -- 添加异常申请每天累计不超过8小时check  end

    //add ccm 0806 查询申请人的剩余年休，
    @Override
    public List<AnnualLeave> getremainingByuserid(String userid) throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        int month = calendar.get(Calendar.MONTH);
        if(month >= 1 && month <= 3) {
            year = calendar.get(Calendar.YEAR) - 1;
        }else {
            year = calendar.get(Calendar.YEAR);
        }
        TokenModel tokenModel = new TokenModel();
        String remaning ="0";
        List<AnnualLeave> aList = new ArrayList<AnnualLeave>();
        aList = abNormalMapper.getremainingByuserid(userid);
        if(aList.size()>0)
        {
            remaning = annualLeaveService.remainingAnnual(userid,String.valueOf(year));
            aList.get(0).setAnnual_avg_remaining(remaning);
        }
        return aList;
    }
    //add ccm 0806 查询申请人的剩余年休，

}
