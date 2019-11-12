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
@Table(name = "punchcardrecord")
public class PunchcardRecord extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PUNCHCARDRECORD_ID")
    private String punchcardrecord_id;


    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "CENTER_ID")
    private String centerid;

    @Column(name = "GROUP_ID")
    private String groupid;

    @Column(name = "TEAM_ID")
    private String teamid;

    @Column(name = "PUNCHCARDRECORD_DATE")
    private Date punchcardrecord_date;

    @Column(name = "TIME_START")
    private Date time_start;


    @Column(name = "TIME_END")
    private Date time_end;


}
