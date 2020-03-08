package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nt.dao_Pfans.PFANS5000.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanagementStatusVo {

    /**
     * 日志管理
     */
    private List<LogManagement> logmanagement;

    /**
     * 人员
     */
    private String createby;

    /**
     * 时间
     */
    private String logdate;

    /**
     * 工时
     */
    private String timestart;

    /**
     * 承认状态
     */
    private String confirmstatus;

    /**
     * 开始时间
     */
    private String starttime;

    /**
     * 结束时间
     */
    private String endtime;

}
