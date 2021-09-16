package com.nt.dao_Pfans.PFANS5000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logpersonstatistics")
public class LogPersonStatistics extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 日志人别统计
     * */
    @Id
    @Column(name = "LOGPERSON_ID")
    private String logperson_id;

    /**
     * 日期
     */
    @Column(name = "LOG_DATE")
    private String log_date;

    /**
     * 姓名
     * */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 部门
     * */
    @Column(name = "DEPARTMENT")
    private String department;

    /**
     * 公司
     * */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 工作项目
     * */
    @Column(name = "PROJECT_ID")
    private String project_id;

    /**
     * 项目名称
     * */
    @Column(name = "PROJECT_NAME")
    private String project_name;

    /**
     * 时长
     * */
    @Column(name = "DURATION")
    private String duration;

    /**
     * 调整
     * */
    @Column(name = "ADJUST")
    private String adjust;

//    /**
//     * 积木报表：比率
//     * */
//    @Transient
//    private String ratios;


}
