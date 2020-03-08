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
@Table(name = "awarddetail")
public class AwardDetail extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "AWARDDETAIL_ID")
    private String awarddetail_id;

    @Column(name = "AWARD_ID")
    private String award_id;

    @Column(name = "BUDGETCODE")
    private String budgetcode;

    @Column(name = "DEPART")
    private String depart;

    @Column(name = "MEMBER")
    private String member;

    @Column(name = "COMMUNITY")
    private String community;

    @Column(name = "OUTSOURCE")
    private String outsource;

    @Column(name = "OUTCOMMUNITY")
    private String outcommunity;

    @Column(name = "WORKNUMBER")
    private String worknumber;

    @Column(name = "AWARDMONEY")
    private String awardmoney;

    @Column(name = "PROJECTS")
    private String projects;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
