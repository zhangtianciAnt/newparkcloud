package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanagementVoimport {
    /**
     * 卡号
     */
    private String jobnumber;
    /**
     * 姓名
     */
    private String username;
    /**
     * 项目id
     */
    private String project_id;
    /**
     * 项目
     */
    private String project_name;
    /**
     * 期间
     */
    private String log_datefromend;
    /**
     * 合同号
     */
    private String contract;
    /**
     * 总工时
     */
    private Double work_memo_sum;

}
