package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ToString
public class CompanyProjectsReport extends CompanyProjects {
    // 合同号
    private String contract;

    // 合同金额
    private String contractamount;

    // 实际时间
    List<MonthTime> logMonthTime;


    public Map<String, String> toLogMonthTimeMap() {
        if ( logMonthTime != null ) {
            return logMonthTime.stream().collect(Collectors.toMap(t -> t.getLogMonth() + "_" + t.getType() + "_" + t.getName() , MonthTime::getTime));
        }
        return new HashMap<>();
    }
}


@Data
class MonthTime {
    private String logMonth;
    private String time;
    private String name;
    private String type;
}
