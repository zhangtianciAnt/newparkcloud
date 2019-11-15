package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "annualleave")
public class AnnualLeave extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 年度休假ID
     */
    private String annualleave_id;

    /**
     * 年度
     */
    private String years;

    /**
     * 姓名
     */
    private String user_id;

    /**
     * Center
     */
    private String center_id;

    /**
     * Group
     */
    private String group_id;

    /**
     * Team
     */
    private String team_id;

    /**
     * 法定年假(上年度)
     */
    private BigDecimal annual_leave_lastyear;

    /**
     * 福利年假(上年度)
     */
    private BigDecimal paid_leave_lastyear;

    /**
     * 法定年假扣除(上年度)
     */
    private BigDecimal deduct_annual_leave_lastyear;

    /**
     * 福利年假扣除(上年度)
     */
    private BigDecimal deduct_paid_leave_lastyear;

    /**
     * 法定年假剩余(上年度)
     */
    private BigDecimal remaining_annual_leave_lastyear;

    /**
     * 福利年假剩余(上年度)
     */
    private BigDecimal remaining_paid_leave_lastyear;

    /**
     * 法定年假(本年度)
     */
    private BigDecimal annual_leave_thisyear;

    /**
     * 福利年假(本年度)
     */
    private BigDecimal paid_leave_thisyear;

    /**
     * 法定年假扣除(本年度)
     */
    private BigDecimal deduct_annual_leave_thisyear;

    /**
     * 福利年假扣除(本年度)
     */
    private BigDecimal deduct_paid_leave_thisyear;

    /**
     * 法定年假剩余(本年度)
     */
    private BigDecimal remaining_annual_leave_thisyear;

    /**
     * 福利年假剩余(本年度)
     */
    private BigDecimal remaining_paid_leave_thisyear;

    /**
     * 代休剩余
     */
    private String remaining_transfer_holiday;

    /**
     * 妇女节剩余
     */
    private String remaining_women_day;

    /**
     * 青年节剩余
     */
    private String remaining_youth_day;

    /**
     * 家长会剩余
     */
    private String remaining_parent_teacher_meeting;

}
