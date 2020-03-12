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
@Table(name = "lunarbonus")
public class Lunarbonus extends BaseModel {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LUNARBONUS_ID")
    private String lunarbonus_id;


    @Column(name = "GROUP_ID")
    private String group_id;


    @Column(name = "EVALUATIONDAY")
    private Date evaluationday;


    @Column(name = "SUBJECTMON")
    private String subjectmon;


    @Column(name = "SUBJECT")
    private String subject;


    @Column(name = "EVALUATENUM")
    private String evaluatenum;


    @Column(name = "USER_ID")
    private String user_id;




}
