package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendancesetting")

public class AttendanceSetting extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 考勤设定ID
     */
    @Id
    @Column(name = "ATTENDANCESETTING_ID")
    private String attendancesetting_id;

    /**
     * 上班时间开始
     */
    @Column(name = "WORKSHIFT_START")
    private String workshift_start;

    /**
     * 上班时间结束
     */
    @Column(name = "WORKSHIFT_END")
    private String workshift_end;

    /**
     * 下班时间开始
     */
    @Column(name = "CLOSINGTIME_START")
    private String closingtime_start;

    /**
     * 下班时间结束
     */
    @Column(name = "CLOSINGTIME_END")
    private String closingtime_end;

    /**
     * 午休时间开始
     */
    @Column(name = "LUNCHBREAK_START")
    private String lunchbreak_start;

    /**
     * 午休时间结束
     */
    @Column(name = "LUNCHBREAK_END")
    private String lunchbreak_end;

    /**
     * 深夜加班开始
     */
    @Column(name = "NIGHTSHIFT_START")
    private String nightshift_start;

    /**
     * 深夜加班结束
     */
    @Column(name = "NIGHTSHIFT_END")
    private String nightshift_end;

    /**
     * 事假
     */
    @Column(name = "COMPASSIONATELEAVE")
    private String compassionateleave;

    /**
     * 旷工
     */
    @Column(name = "ABSENTEEISM")
    private String absenteeism;

    /**
     * 周末加班代休
     */
    @Column(name = "WEEKENDOVERTIME")
    private String weekendovertime;

    /**
     * 迟到/早退
     */
    @Column(name = "LATEEARLYLEAVE")
    private String lateearlyleave;

    /**
     * 加班
     */
    @Column(name = "OVERTIME")
    private String overtime;

    /**
     * 平日晚加班时间扣除
     */
    @Column(name = "WEEKDAYSOVERTIME")
    private String weekdaysovertime;

    /**
     * 末次打卡截止时间
     */
    @Column(name = "DEADLINE")
    private String deadline;

    /**
     * 每月考勤异常处理截止提醒日
     */
    @Column(name = "ABNORMALDEADLINE")
    private String abnormaldeadline;

    /**
     * 每月考勤异常处理截止提醒时间
     */
    @Column(name = "ABNORMALDEADLINETIME")
    private String abnormaldeadlinetime;

    /**
     * 病假超限提醒
     */
    @Column(name = "TRANSFINITEREMINDER")
    private String transfinitereminder;

    /**
     * 工作时间
     */
    @Column(name = "WORKINGHOURS")
    private String workinghours;
}
