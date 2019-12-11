package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance")
public class Attendance extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 考勤管理
     */
    @Id
    @Column(name = "ATTENDANCE_ID")
    private String attendanceid;

    /**
     * 年份
     */
    @Id
    @Column(name = "YEARS")
    private String years;

    /**
     * 月份
     */
    @Column(name = "MONTHS")
    private String months;

    /**
     * 姓名
     */
    @Id
    @Column(name = "USER_ID")
    private String user_id;
    /**
     * 日付
     */
    @Id
    @Column(name = "DATES")
    private Date dates;

    /**
     * 正常
     */
    @Id
    @Column(name = "NORMAL")
    private String normal;

    /**
     * 实际时间
     */
    @Id
    @Column(name = "ACTUAL")
    private String actual;
    /**
     * 平日残業
     */
    @Id
    @Column(name = "ORDINARYINDUSTRY")
    private String ordinaryindustry;
    /**
     * 平日深夜残業
     */
    @Id
    @Column(name = "ORDINARYINDUSTRYNIGHT")
    private String ordinaryindustrynight;
    /**
     * 周末残業
     */
    @Id
    @Column(name = "WEEKENDINDUSTRY")
    private String weekendindustry;
    /**
     * 周末深夜残業
     */
    @Id
    @Column(name = "WEEKENDINDUSTRYNIGHT")
    private String weekendindustrynight;
    /**
     * 法定节假日残業
     */
    @Id
    @Column(name = "STATUTORYRESIDUE")
    private String statutoryresidue;
    /**
     * 法定节假日深夜残業
     */
    @Id
    @Column(name = "STATUTORYRESIDUENIGHT")
    private String statutoryresiduenight;
    /**
     * 一齐年休日出勤
     */
    @Id
    @Column(name = "ANNUALRESTDAY")
    private String annualrestday;
    /**
     * 会社特别休日出勤
     */
    @Id
    @Column(name = "SPECIALDAY")
    private String specialday;
    /**
     * 青年节出勤
     */
    @Id
    @Column(name = "YOUTHDAY")
    private String youthday;
    /**
     * 妇女节出勤
     */
    @Id
    @Column(name = "WOMENSDAY")
    private String womensday;
    /**
     * 短病假
     */
    @Id
    @Column(name = "SHORTSICKLEAVE")
    private String shortsickleave;
    /**
     * 长病假
     */
    @Id
    @Column(name = "LONGSICKLEAVE")
    private String longsickleave;
    /**
     * 事假
     */
    @Id
    @Column(name = "COMPASSIONATELEAVE")
    private String compassionateleave;
    /**
     * 年休
     */
    @Id
    @Column(name = "ANNUALREST")
    private String annualrest;
    /**
     * 代休
     */
    @Id
    @Column(name = "DAIXIU")
    private String daixiu;
    /**
     * 产休护理假
     */
    @Id
    @Column(name = "NURSINGLEAVE")
    private String nursingleave;
    /**
     * 福利假期
     */
    @Id
    @Column(name = "WELFARE")
    private String welfare;
    /**
     * 迟到
     */
    @Id
    @Column(name = "LATE")
    private String late;
    /**
     * 早退
     */
    @Id
    @Column(name = "LEAVEEARLY")
    private String leaveearly;
    /**
     * 无故旷工
     */
    @Id
    @Column(name = "ABSENTEEISM")
    private String absenteeism;
    /**
     * 承认状态
     */
    @Id
    @Column(name = "RECOGNITIONSTATE")
    private String recognitionstate;
}
