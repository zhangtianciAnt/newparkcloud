package com.nt.utils;

public class NumberSql {

    public String selectCounts(){
       //and datediff(CREATEON,'2020-04-26') = 0
        return  "select count(*) from quotations where status = 0 ";
    }
}
