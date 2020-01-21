package com.nt.dao_Pfans.PFANS2000;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recruitjudgement")
public class RecruitJudgement extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String recruitjudgement_id;

    private String center_id;

    private String group_id;

    private String team_id;

    private String name;

    private String sex;

    private Date birthday;

    private String education;

    private String specialty;

    private Date quityear;

    private String resume;

    private String resume_enclosure;

    private String identity;

    private String identity_enclosure;

    private String diploma;

    private String diploma_enclosure;

    private String experience;

    private String experience_enclosure;

    private String entry;

    private String entry_enclosure;

    private String report;

    private String report_enclosure;

    private String english;

    private String english_enclosure;

    private String janpanese;

    private String janpanese_enclosure;

    private String other1;

    private String other1_enclosure;

    private String other2;
    private String other2_enclosure;

    private String ticket;

    private String ticket_enclosure;

    private String health;

    private String health_enclosure;

    private String interview;

    private String level;

    private int giving;

    private String adoption;

    private String others;


    private String education1;
    private String specialty1;
    private Date quityear1;
    private String education2;
    private String specialty2;
    private Date quityear2;
    private String education3;
    private String specialty3;
    private Date quityear3;
    private String english_detail;
    private String janpanese_detail;
    private String other3;
}
