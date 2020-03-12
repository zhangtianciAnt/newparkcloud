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
@Table(name = "appreciation")
public class Appreciation extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "APPRECIATION_ID")
    private String appreciation_id;

    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "JOBNUMBER")
    private String jobnumber;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "OTHER1")
    private String other1;

    @Column(name = "OTHER2")
    private String other2;

    @Column(name = "OTHER3")
    private String other3;

    @Column(name = "OTHER4")
    private String other4;

    @Column(name = "OTHER5")
    private String other5;

    @Column(name = "COMMENTARY")
    private String commentary;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

    @Column(name = "YEARS")
    private String years;

    @Column(name = "MONTHS")
    private String months;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;
}
