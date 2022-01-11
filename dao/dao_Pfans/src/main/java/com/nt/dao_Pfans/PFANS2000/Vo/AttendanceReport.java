package com.nt.dao_Pfans.PFANS2000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceReport {

    /**
     * id
     */
    private String user_id;
    /**
     * 姓名
     */
    private String user_name;
    /**
     * Centr
     */
    private String center_id;
    /**
     * Group
     */
    private String group_id;
    /**
     * 正常
     */
    private String normal;
    /**
     * 年休
     */
    private String annualrest;
    /**
     * 代休
     */
    private String daixiu;
    /**
     * 其他福利休假
     */
    private String welfare;
    /**
     * 日付
     */
    private String dates;
    /**
     * 平日残業
     */
    private String ordinaryindustry;
    /**
     * 周末残業
     */
    private String weekendindustry;
    /**
     * 法定节假日残業
     */
    private String statutoryresidue;
    /**
     * 一齐年休日出勤
     */
    private String annualrestday;
    /**
     * 会社特别休日出勤
     */
    private String specialday;
    /**
     * 青年节出勤
     */
    private String youthday;
    /**
     * 妇女节出勤
     */
    private String womensday;
    /**
     * 正式短病假
     */
    private String shortsickleave;
    /**
     * 正式长病假
     */
    private String longsickleave;
    /**
     * 正式事假
     */
    private String compassionateleave;
    /**
     * 产休护理假
     */
    private String nursingleave;
    /**
     * 无故旷工
     */
    private String absenteeism;

    /**
     * 试用短病假
     */
    private String tshortsickleave;

    /**
     * 试用长病假
     */
    private String tlongsickleave;

    /**
     * 试用事假
     */
    private String tcompassionateleave;

    /**
     * 试用无故旷工
     */
    private String tabsenteeism;

    /**
     * 育儿假
     */
    @Column(name = "PARENTING")
    private String parenting;

    /**
     * 父母照料假
     */
    @Column(name = "PARENTALCARE")
    private String parentalcare;

}
