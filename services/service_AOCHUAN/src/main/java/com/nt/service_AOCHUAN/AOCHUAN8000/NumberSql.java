package com.nt.service_AOCHUAN.AOCHUAN8000;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberSql {
    public String selectCounts(String tableName){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        return  "select count(*) from "+tableName+" where status = 0 and  datediff(CREATEON,'"+date+"') = 0";
    }
}
