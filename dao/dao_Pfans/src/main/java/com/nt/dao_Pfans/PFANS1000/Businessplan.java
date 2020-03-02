package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "businessplan")
public class Businessplan extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 事业计划ID
     */
    @Id
    @Column(name = "BUSINESSPLAN_ID")
    private String businessplanid;

    /**
     * 计划者ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * センターID
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 計画収支単位名ID
     */
    @Column(name = "GROUP_ID")
    private String group_id;

//    /**
//     * チーム名ID
//     */
//    @Column(name = "TEAM_ID")
//    private String team_id;

    @Column(name = "EQUIPMENT_NEWYEAR")
    private String equipment_newyear;

    @Column(name = "EQUIPMENT_LASTYEAR")
    private String equipment_lastyear;

    @Column(name = "EQUIPMENT_OTHER")
    private String equipment_other;

    @Column(name = "ASSETS_NEWYEAR")
    private String assets_newyear;

    @Column(name = "ASSETS_LASTYEAR")
    private String assets_lastyear;

    @Column(name = "ASSETS_OTHER")
    private String assets_other;

    @Column(name = "TRANSPORTATION")
    private String transportation;

    @Column(name = "TRUSTB1")
    private String trustb1;

    @Column(name = "TRUSTB2")
    private String trustb2;

    @Column(name = "TRUSTB3")
    private String trustb3;

    @Column(name = "SPECIAL_FUNDS")
    private String special_funds;


}
