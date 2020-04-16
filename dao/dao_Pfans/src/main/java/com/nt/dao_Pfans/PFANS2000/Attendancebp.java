package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendancebp")
public class Attendancebp extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 考勤管理BP
     */
    @Id
    @Column(name = "ATTENDANCEBP_ID")
    private String attendancebpid;

    /**
     * センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 年份
     */
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
    @Column(name = "USER_ID")
    private String user_id;
    /**
     * 日付
     */
    @Column(name = "DATES")
    private Date dates;

    /**
     * 正常
     */
    @Column(name = "NORMAL")
    private String normal;

    /**
     * 实际时间
     */
    @Column(name = "ACTUAL")
    private String actual;
    /**
     * 平日残業
     */
    @Column(name = "ORDINARYINDUSTRY")
    private String ordinaryindustry;
    /**
     * 平日深夜残業
     */
    @Column(name = "ORDINARYINDUSTRYNIGHT")
    private String ordinaryindustrynight;
    /**
     * 周末残業
     */
    @Column(name = "WEEKENDINDUSTRY")
    private String weekendindustry;
    /**
     * 周末深夜残業
     */
    @Column(name = "WEEKENDINDUSTRYNIGHT")
    private String weekendindustrynight;
    /**
     * 法定节假日残業
     */
    @Column(name = "STATUTORYRESIDUE")
    private String statutoryresidue;
    /**
     * 法定节假日深夜残業
     */
    @Column(name = "STATUTORYRESIDUENIGHT")
    private String statutoryresiduenight;
    /**
     * 一齐年休日出勤
     */
    @Column(name = "ANNUALRESTDAY")
    private String annualrestday;
    /**
     * 会社特别休日出勤
     */
    @Column(name = "SPECIALDAY")
    private String specialday;
    /**
     * 青年节出勤
     */
    @Column(name = "YOUTHDAY")
    private String youthday;
    /**
     * 妇女节出勤
     */
    @Column(name = "WOMENSDAY")
    private String womensday;
    /**
     * 正式短病假
     */
    @Column(name = "SHORTSICKLEAVE")
    private String shortsickleave;
    /**
     * 正式长病假
     */
    @Column(name = "LONGSICKLEAVE")
    private String longsickleave;
    /**
     * 正式事假
     */
    @Column(name = "COMPASSIONATELEAVE")
    private String compassionateleave;
    /**
     * 年休
     */
    @Column(name = "ANNUALREST")
    private String annualrest;
    /**
     * 代休
     */
    @Column(name = "DAIXIU")
    private String daixiu;
    /**
     * 产休护理假
     */
    @Column(name = "NURSINGLEAVE")
    private String nursingleave;
    /**
     * 福利假期
     */
    @Column(name = "WELFARE")
    private String welfare;
    /**
     * 正式迟到
     */
    @Column(name = "LATE")
    private String late;
    /**
     * 正式迟到时间
     */
    @Column(name = "LATETIME")
    private String latetime;
    /**
     * 正式早退
     */
    @Column(name = "LEAVEEARLY")
    private String leaveearly;
    /**
     * 正式早退时间
     */
    @Column(name = "LEAVEEARLYTIME")
    private String leaveearlytime;
    /**
     * 无故旷工
     */
    @Column(name = "ABSENTEEISM")
    private String absenteeism;
    /**
     * 承认状态
     */
    @Column(name = "RECOGNITIONSTATE")
    private String recognitionstate;

    /**
     * 试用短病假
     */
    @Column(name = "TSHORTSICKLEAVE")
    private String tshortsickleave;

    /**
     * 试用长病假
     */
    @Column(name = "TLONGSICKLEAVE")
    private String tlongsickleave;

    /**
     * 试用事假
     */
    @Column(name = "TCOMPASSIONATELEAVE")
    private String tcompassionateleave;

    /**
     * 试用迟到
     */
    @Column(name = "TLATE")
    private String tlate;

    /**
     * 试用迟到时间
     */
    @Column(name = "TLATETIME")
    private String tlatetime;

    /**
     * 试用早退
     */
    @Column(name = "TLEAVEEARLY")
    private String tleaveearly;

    /**
     * 试用早退时间
     */
    @Column(name = "TLEAVEEARLYTIME")
    private String tleaveearlytime;

    /**
     * 试用无故旷工
     */
    @Column(name = "TABSENTEEISM")
    private String tabsenteeism;

    /**
     * 全天实际外出时长
     */
    @Column(name = "OUTGOINGHOURS")
    private String outgoinghours;
}
