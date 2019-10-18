package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "annualleave")
public class AnnualLeave extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String annualleave_id;

    private String years;

    private String user_id;

    private String center_id;

    private String group_id;

    private String team_id;

    private String annual_leave_lastyear;

    private String paid_leave_lastyear;

    private String deduct_annual_leave_lastyear;

    private String deduct_paid_leave_lastyear;

    private String remaining_annual_leave_lastyear;

    private String remaining_paid_leave_lastyear;

    private String annual_leave_thisyear;

    private String paid_leave_thisyear;

    private String deduct_annual_leave_thisyear;

    private String deduct_paid_leave_thisyear;

    private String remaining_annual_leave_thisyear;

    private String remaining_paid_leave_thisyear;

    private String remaining_transfer_holiday;

    private String remaining_women_day;

    private String remaining_youth_day;

    private String remaining_parent_teacher_meeting;


}
