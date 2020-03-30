package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lunardetail")
public class Lunardetail extends BaseModel {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LUNARDETAIL_ID")
    private String lunardetail_id;


    @Column(name = "LUNARBONUS_ID")
    private String lunarbonus_id;


    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "RN")
    private String rn;

    @Column(name = "ENTERDAY")
    private String enterday;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "SALARY")
    private String salary;

    @Column(name = "WORKRATE")
    private String workrate;


    @Column(name = "BONUSSIGN")
    private String bonussign;


    @Column(name = "LASTSYMBOL")
    private String lastsymbol;


    @Column(name = "TATEBAI")
    private String tatebai;

    @Column(name = "SATOSHI")
    private String satoshi;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "SYSTEMATICS")
    private String systematics;

    @Column(name = "MANPOWER")
    private String manpower;

    @Column(name = "SCALE")
    private String scale;

    @Column(name = "ACHIEVEMENT")
    private String achievement;

    @Column(name = "DEGREE")
    private String degree;

    @Column(name = "ASSIGNMENT")
    private String assignment;

    @Column(name = "TEAMWORK")
    private String teamwork;

    @Column(name = "HUMANDEVELOPMENT")
    private String humandevelopment;

    @Column(name = "WORKATTITUDE")
    private String workattitude;

    @Column(name = "OVERALLSCORE")
    private String overallscore;

    @Column(name = "COMMENTARYRETURNS")
    private String commentaryreturns;

    @Column(name = "COMMENTARYRESULT")
    private String commentaryresult;

    @Column(name = "COMPREHENSIVEONE")
    private String comprehensiveone;

    @Column(name = "COMPREHENSIVETWO")
    private String comprehensivetwo;

    @Column(name = "FIRSTMONTH")
    private String firstmonth;

    @Column(name = "SECONDMONTH")
    private String secondmonth;

    @Column(name = "THIRDMONTH")
    private String thirdmonth;

    @Column(name = "SUBJECTMON")
    private String subjectmon;

    @Column(name = "EVALUATENUM")
    private String evaluatenum;

    @Column(name = "DIFFERENCE")
    private String difference;

    @Column(name = "EVALUATIONDAY")
    private String evaluationday;

    @Column(name = "EXAMINATIONOBJECT_ID")
    private String examinationobject_id;

    @Column(name = "OCCUPATIONTYPE")
    private String occupationtype;



}
