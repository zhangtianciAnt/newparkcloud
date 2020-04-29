package com.nt.service_AOCHUAN.AOCHUAN6000;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.SecrecyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelectSecrecy {

    @Autowired
    private SecrecyMapper secrecyMapper;


    //系统服务（4月1日）
    @Scheduled(cron="59 * * * * ?")
    public void changestatus() throws Exception {
//        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdfxx = new SimpleDateFormat("EEE MMM dd 00:00:00 zzz yyyy", Locale.US);
//        Date dateStart = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateStart);
//        //cal.add(Calendar.MONTH, -3);
//        String data = sf1ymd.format(sdfxx.parse(sdfxx.format(cal.getTime())));
        //  Secrecy ser=new Secrecy();
//        ser.setNo();
//        ser.setStatus();
//        ser.setFiletype();
//        ser.setCustomer();
//        ser.setResponsible();
//        ser.setDescribe1();
//        ser.setDuedate();
//        ser.setUpload();
//        ser.setUploaddate();
//        ser.setType("1");

        List<Secrecy> secrelist = secrecyMapper.selectsecrecy();

    }


}
