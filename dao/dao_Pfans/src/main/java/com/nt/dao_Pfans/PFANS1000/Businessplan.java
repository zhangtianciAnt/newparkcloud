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

    @Column(name = "TABLEP")
    private String tableP;

    @Column(name = "BUSINESS")
    private String business;

    @Column(name = "GROUPA1")
    private String groupA1;

    @Column(name = "GROUPA2")
    private String groupA2;

    @Column(name = "GROUPB1")
    private String groupB1;

    @Column(name = "GROUPB2")
    private String groupB2;

    @Column(name = "GROUPB3")
    private String groupB3;

    @Column(name = "GROUPC")
    private String groupC;

    @Column(name = "TABLEO1")
    private String tableO1;

    @Column(name = "TABLEO2")
    private String tableO2;

    @Column(name = "TABLEO3")
    private String tableO3;

    @Column(name = "TABLEO")
    private String tableO;

    @Column(name = "YEAR")
    private String year;

}
