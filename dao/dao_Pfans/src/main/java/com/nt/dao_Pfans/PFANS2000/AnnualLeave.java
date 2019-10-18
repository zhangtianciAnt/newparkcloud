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

    private float annual_leave_lastyear;

    private float paid_leave_lastyear;

    private float deduct_annual_leave_lastyear;

    private float deduct_paid_leave_lastyear;

    private float remaining_annual_leave_lastyear;

    private float remaining_paid_leave_lastyear;

    private float annual_leave_thisyear;

    private float paid_leave_thisyear;

    private float deduct_annual_leave_thisyear;

    private float deduct_paid_leave_thisyear;

    private float remaining_annual_leave_thisyear;

    private float remaining_paid_leave_thisyear;

    private float remaining_transfer_holiday;

    private float remaining_women_day;

    private float remaining_youth_day;

    private float remaining_parent_teacher_meeting;


}
