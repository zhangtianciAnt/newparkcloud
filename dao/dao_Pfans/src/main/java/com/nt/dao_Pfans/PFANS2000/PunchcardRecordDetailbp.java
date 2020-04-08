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
@Table(name = "punchcardrecorddetailbp")
public class PunchcardRecordDetailbp extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PUNCHCARDRECORDDETAILBP_ID")
    private String punchcardrecorddetailbp_id;

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

    @Column(name = "REGION")
    private String region ;

    @Column(name = "EVENTNO")
    private String eventno ;
}
