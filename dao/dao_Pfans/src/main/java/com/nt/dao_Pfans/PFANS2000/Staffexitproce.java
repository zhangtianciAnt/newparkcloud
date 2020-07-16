package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staffexitproce extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    private String staffexitproce_id;
    private String staffexitprocedure_id;
    private String user_id;
    private String center_id;
    private String group_id;
    private String team_id;
    private Date application_date;
//    private Date hope_exit_date;
//    private String reason;
//    private String address;
//    private String fixed_phone;
//    private String cellphone;
//    private String email;
//    private Date delivery_sheet_date;
    private String jpwork_delivery;
//    private String jpwork_delivery_list;
    private String sex;
    private Date entry_time;
    private String position;
    private String educational_background;
    private String reporter;
    private Date report_date;
    private Date resignation_date;
    private String social_evaluation;
    private String external_evaluation;
    private String reason2;
//    private String impact_resignation;
    private String influence_information_security;
    private String retirement_strategy;
    private  String impact_resignation_external;
    private String impact_resignation_internal;
//    private String phase;
//    private String stage;
    private String condate;
    private String starank;
    private String checkedgm;
    private String checkedcenter;
    private String userdata;
    private String userworkfolw;
    private String remarkdata;
    private String remarkworkfolw;
//    private Date newhope_exit_date;
//    private String newreason;
}
