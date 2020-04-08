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
@Table(name = "punchcardrecordbp")
public class PunchcardRecordbp extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PUNCHCARDRECORDBP_ID")
    private String punchcardrecordbp_id;

    @Column(name = "JOBNUMBER")
    private String jobnumber;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "PUNCHCARDRECORD_DATE")
    private Date punchcardrecord_date;

    @Column(name = "TIME_START")
    private Date time_start;

    @Column(name = "TIME_END")
    private Date time_end;

    @Column(name = "WORKTIME")
    private String worktime;

    @Column(name = "ABSENTEEISMAM")
    private String absenteeismam;

    @Column(name = "REGION")
    private String region;

    @Column(name = "ABSENTEEISM")
    private String absenteeism;

}
