package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Replacerest;
import com.nt.service_pfans.PFANS2000.ReplacerestService;
import com.nt.service_pfans.PFANS2000.mapper.ReplacerestMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.ss.formula.functions.EDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class ReplacerestServiceImpl implements ReplacerestService {

    @Autowired
    private ReplacerestMapper replacerestMapper;


    @Override
    public List<Replacerest> getReplacerest(Replacerest replacerest) throws Exception {
        return replacerestMapper.select(replacerest);
    }

    //系统服务（4月1日）
    @Scheduled(cron="5 * * * * ?")
    public void changestatus() throws Exception {
        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfxx = new SimpleDateFormat("EEE MMM dd 00:00:00 zzz yyyy", Locale.US);
        Date dateStart = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateStart);
        cal.add(Calendar.MONTH, -3);
        String data = sf1ymd.format(sdfxx.parse(sdfxx.format(cal.getTime())));
        Replacerest rep = new Replacerest();
        rep.setStatus("0");//状态可用
        rep.setType("0");//周末加班
        rep.setRecognitionstate("0");//可用状态
        rep.setApplication_date(data);//申请日期
        List<Replacerest> replacerestlist = replacerestMapper.select(rep);
            for (Replacerest replacerest : replacerestlist) {
                replacerest.setRecognitionstate("1");
                replacerestMapper.updateByPrimaryKey(replacerest);
            }
    }
}
