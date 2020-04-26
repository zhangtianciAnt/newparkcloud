package com.nt.service_AOCHUAN.AOCHUAN8000.Impl;

import com.nt.service_AOCHUAN.AOCHUAN8000.mapper.NumberMapper;
import com.nt.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ContractNumber {
    @Autowired
    private NumberMapper numberMapper;

    public String getContractNumber(String code,String tableName) throws Exception{
        if(!StringUtils.isNotBlank(tableName)){
            throw new Exception("表名为空");
        }
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMonthDay = new SimpleDateFormat("MMdd");
        String year = dateFormatYear.format(new Date());
        String monthDay = dateFormatMonthDay.format(new Date());
        StringBuffer sb = new StringBuffer(year);
        int count = numberMapper.selectCounts(tableName) + 1;
        String str = String.format("%02d", count);
        sb.append(code).append(monthDay).append(str);
         return sb.toString();
    }
}
