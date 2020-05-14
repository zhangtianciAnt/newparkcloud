package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.DutySimt;
import com.nt.dao_BASF.VO.DutySimtVo;
import com.nt.service_BASF.DutySimtServices;
import com.nt.service_BASF.mapper.DutySimtMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: DutySimtServicesImpl
 * @Author: WXZ
 * @Description: DutySimtServicesImpl
 * @Date: 2019/12/19 15:09
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DutySimtServicesImpl implements DutySimtServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private DutySimtMapper dutySimtMapper;

    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取值班人列表
     * @Return void
     * @Date 2019/12/19 15:14
     */
    @Override
    public List<DutySimt> list() throws Exception {
        DutySimt dutySimt = new DutySimt();
        return dutySimtMapper.select(dutySimt);
    }

    /**
     * @param dutySimt
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新值班人
     * @Return void
     * @Date 2019/12/19 15：18
     */
    @Override
    public void update(DutySimt dutySimt, TokenModel tokenModel) throws Exception {
        dutySimt.preUpdate(tokenModel);
        dutySimtMapper.updateByPrimaryKey(dutySimt);
    }

    /**
     * @param
     * @Method selectByDay
     * @Author Wxz
     * @Version 1.0
     * @Description 查询某天值班人
     * @Return void
     * @Date 2019/12/19 17：25
     */
    @Override
    public DutySimtVo selectByDay() throws Exception {
        DutySimtVo dutySimtVo = new DutySimtVo();
        DutySimt dutySimt = new DutySimt();
        List<DutySimt> list = dutySimtMapper.select(dutySimt);
        //设置日期格式
//        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
//        // new Date()为获取当前系统时间为星期几
//        String currSun = dateFm.format(new Date());
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        String currSun = weekDays[w];

        if(list.size() == 1 && list.get(0) != null){
        if(currSun.equals("星期一")){
            dutySimtVo.setDuty(list.get(0).getMon());
            dutySimtVo.setBackup(list.get(0).getMonbackup());
            return dutySimtVo;
        }
        if(currSun.equals("星期二")){
            dutySimtVo.setDuty(list.get(0).getTue());
            dutySimtVo.setBackup(list.get(0).getTuebackup());
            return dutySimtVo;
        }
        if(currSun.equals("星期三")){
            dutySimtVo.setDuty(list.get(0).getWeb());
            dutySimtVo.setBackup(list.get(0).getWebbackup());
            return dutySimtVo;
        }
        if(currSun.equals("星期四")){
            dutySimtVo.setDuty(list.get(0).getThu());
            dutySimtVo.setBackup(list.get(0).getThubackup());
            return dutySimtVo;
        }
        if(currSun.equals("星期五")){
            dutySimtVo.setDuty(list.get(0).getFri());
            dutySimtVo.setBackup(list.get(0).getFirbackup());
            return dutySimtVo;
        }
        if(currSun.equals("星期六")){
            dutySimtVo.setDuty(list.get(0).getSat());
            dutySimtVo.setBackup(list.get(0).getSatbackup());
            return dutySimtVo;
        }
        if(currSun.equals("星期日")){
            dutySimtVo.setDuty(list.get(0).getSun());
            dutySimtVo.setBackup(list.get(0).getSunbackup());
            return dutySimtVo;
        }
        }
        return null;
    }

}
