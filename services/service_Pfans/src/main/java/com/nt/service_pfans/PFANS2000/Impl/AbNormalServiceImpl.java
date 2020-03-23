package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

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
    private AnnualLeaveMapper annualLeaveMapper;

    @Override
    public List<AbNormal> list(AbNormal abNormal) throws Exception {
        return abNormalMapper.select(abNormal);
    }

    @Override
    public List<AbNormal> selectAbNormalParent(String userid) throws Exception {
        return abNormalMapper.selectAbNormalParent(userid);
    }

    @Override
    public Double getSickleave(String userid) throws Exception {
        Double a =abNormalMapper.selectAbNormalDate(userid);
        if(a == null){
            a = 0.0;
        }
        return a;
    }

    @Override
    public void insert(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preInsert(tokenModel);
        abNormal.setAbnormalid(UUID.randomUUID().toString());
        abNormalMapper.insert(abNormal);
    }

    @Override
    public void upd(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preUpdate(tokenModel);
        abNormalMapper.updateByPrimaryKey(abNormal);
        if(abNormal.getStatus().equals(AuthConstants.APPROVED_FLAG_YES)){

            if(abNormal.getErrortype().equals("PR013006")){//代休周末
                //修改代休记录
                updateReplacerest(abNormal,tokenModel);
            }
            //旷工基本计算单位
            String absenteeism = null;
            //工作时间
            String workinghours = null;
            AttendanceSetting attendancesetting = new AttendanceSetting();
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0){
                //旷工基本计算单位
                absenteeism = attendancesettinglist.get(0).getAbsenteeism();
                workinghours = attendancesettinglist.get(0).getWorkinghours();
            }
            Attendance attendance = new Attendance();
            attendance.setUser_id(abNormal.getUser_id());
            attendance.setDates(abNormal.getOccurrencedate());
            List<Attendance> attendancelist = attendanceMapper.select(attendance);
            DecimalFormat df = new DecimalFormat("######0.00");
            if(attendancelist.size() > 0){
                for (Attendance attend : attendancelist) {
                    if(abNormal.getErrortype().equals("PR013001")){//外出
                        if(Double.valueOf(abNormal.getLengthtime()) >= Double.valueOf(attend.getAbsenteeism())){
                            if(Double.valueOf(abNormal.getLengthtime()) >= Double.valueOf(workinghours)){
                                attend.setNormal(workinghours);
                                attend.setAbsenteeism("");
                            }
                            else{
                                attend.setNormal(abNormal.getLengthtime());
                            }
                        }
                    }
                    else if(abNormal.getErrortype().equals("PR013005")){//年休
                        attend.setAnnualrest(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013008")){//事休
                        attend.setCompassionateleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013009")){//短期病休
                        attend.setShortsickleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013010")){//長期病休
                        attend.setLongsickleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013012")){//産休（女）
                        attend.setNursingleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013004")
                            || abNormal.getErrortype().equals("PR013011")
                            || abNormal.getErrortype().equals("PR013014")
                            || abNormal.getErrortype().equals("PR013015")
                            || abNormal.getErrortype().equals("PR013016")
                            || abNormal.getErrortype().equals("PR013017")
                            || abNormal.getErrortype().equals("PR013018")
                            || abNormal.getErrortype().equals("PR013019")
                            ){
                        //女性三期、结婚休假、家长会假、葬儀休暇、妊娠檢查休暇、哺乳休暇、労災休暇、其他休暇
                        attend.setWelfare(abNormal.getLengthtime());
                    }
                    attend.preUpdate(tokenModel);
                    attendanceMapper.updateByPrimaryKey(attend);
                }
            }
        }
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

        if("4".equals(abNormal.getLengthtime())){
            lengths = 0.5;
        }
        if(StrUtil.isNotBlank(abNormal.getRelengthtime())) {
            if ("4".equals(abNormal.getRelengthtime())) {
                relengths = 0.5;
            }
            lengths = relengths - lengths;
        }

        if("PR013005".equals(abNormal.getErrortype())){
            List<AnnualLeave> list = annualLeaveMapper.getDataList(abNormal.getUser_id());
            if(list.size() > 0 ){
                if(list.get(0).getRemaining_annual_leave_thisyear().doubleValue() >= lengths){
                    String Timecheck = String.valueOf(list.get(0).getRemaining_annual_leave_thisyear().doubleValue());
                    rst.put("dat",Timecheck);
                    rst.put("error",abNormal.getErrortype());
                    rst.put("can","yes");
                }else{
                    rst.put("dat","");
                    rst.put("can","no");
                }
            }else{
                rst.put("dat","");
                rst.put("can","no");
            }
        }else if("PR013006".equals(abNormal.getErrortype())){

                List<restViewVo> list = annualLeaveMapper.getrest(abNormal.getUser_id());
                String dat="";
                for(restViewVo item:list){
                    if(Double.parseDouble(item.getRestdays()) != 0){
                        if(lengths >= 0){
                            lengths = lengths - Double.parseDouble(item.getRestdays());
                            dat += dat + "," + item.getApplicationdate();
                            if(lengths <= 0){
                                String check = String.valueOf(list.get(0).getRestdays());
                                dat = dat.substring(1,dat.length());
                                rst.put("checkdat",check);
                                rst.put("error",abNormal.getErrortype());
                                rst.put("dat",dat);
                                rst.put("can","yes");
                                return rst;
                            }
                        }else{
                            String check = String.valueOf(list.get(0).getRestdays());
                            rst.put("checkdat",check);
                            rst.put("error",abNormal.getErrortype());
                            rst.put("dat","");
                            rst.put("can","yes");
                        }
                    }
                }
            rst.put("dat","");
            rst.put("can","no");
        } else if("PR013007".equals(abNormal.getErrortype())){
            List<restViewVo> list = annualLeaveMapper.getrest(abNormal.getUser_id());
            String dat="";
            for(restViewVo item:list){
                if(Double.parseDouble(item.getRestdays()) != 0){
                    if(lengths >= 0){
                        lengths = lengths - Double.parseDouble(item.getRestdays());
                        dat += dat + "," + item.getApplicationdate();
                        if(lengths <= 0){
                            String check = String.valueOf(list.get(0).getRestdays());
                            dat = dat.substring(1,dat.length());
                            rst.put("checkdat",check);
                            rst.put("error",abNormal.getErrortype());
                            rst.put("dat",dat);
                            rst.put("can","yes");
                            return rst;
                        }
                    }else{
                        String check = String.valueOf(list.get(0).getRestdays());
                        rst.put("checkdat",check);
                        rst.put("error",abNormal.getErrortype());
                        rst.put("dat","");
                        rst.put("can","yes");
                    }
                }
            }


            rst.put("dat","");
            rst.put("can","no");
        }

        return rst;
    }

    //代休添加
    public void updateReplacerest(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        Replacerest replacerest = new Replacerest();
        replacerest.setUser_id(abNormal.getUser_id());
        replacerest.setRecognitionstate("0");
        List<Replacerest> recruitlist = replacerestMapper.select(replacerest);
        for(Replacerest re : recruitlist){
            String strDuration = String.valueOf(Double.valueOf(re.getDuration()) - Double.valueOf(abNormal.getLengthtime()));
            replacerest.setDuration(strDuration);
            if(strDuration.equals("0")){
                replacerest.setDuration(null);
                replacerest.setRecognitionstate("1");
            }
            replacerest.preUpdate(tokenModel);
            replacerestMapper.updateByPrimaryKey(replacerest);
        }

    }
}
