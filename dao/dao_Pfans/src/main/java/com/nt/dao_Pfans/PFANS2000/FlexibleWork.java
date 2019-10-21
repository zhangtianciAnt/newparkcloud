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
@Table(name = "flexiblework")
public class FlexibleWork extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "FLEXIBLEWORK_ID")
    private String flexibleworkid;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "APPLICATION_DATE")
    private String application_date;

    @Column(name = "WORKTIME")
    private String worktime;

    @Column(name = "IMPLEMENT_DATE")
    private String implement_date;


}
