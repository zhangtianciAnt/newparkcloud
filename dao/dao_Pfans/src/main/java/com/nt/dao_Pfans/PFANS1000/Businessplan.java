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

    /**
     * チーム名ID
     */
    @Column(name = "TEAM_ID")
    private String team_id;

}
