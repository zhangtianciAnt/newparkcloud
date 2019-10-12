package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AnnualLeave")
public class AnnualLeave extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String annual_leave_id;

    private String year;

    private String user_id;

    private String center_id;

    private String group_id;

    private Date team_id;

    private float annual_leave;

    private float paid_leave;

    private float  deduct_annual_leave;

    private float  deduct_paid_leave;

    private float  remaining_annual_leave;

    private float  remaining_paid_leave;

}
