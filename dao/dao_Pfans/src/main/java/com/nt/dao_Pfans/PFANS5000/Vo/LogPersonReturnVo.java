package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogPersonReturnVo {

    private String logmanagement_id;

    /**
     * 表皮：名字
     * */
    private String username;

    /**
     * 表皮：部门
     * */
    private String groupname;

    /**
     * 表皮：公司
     * */
    private String company;

    /**
     * 表皮：比率
     * */
    private String ratio;

    /**
     * 表皮：项目
     * */
    private String project;

    /**
     * 表皮：总工时
     * */
    private String general;

    /**
     * 表皮：调整
     * */
    private String adjust;

    /**
     * 表内容：日志
     * */
    List<LogPersonStatistics> logpersonstatistics;

}
