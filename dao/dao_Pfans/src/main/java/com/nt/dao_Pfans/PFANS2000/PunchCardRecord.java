package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class PunchCardRecord extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 打卡记录ID
     */
    @Id
    @Column(name = "PUNCHCARDRECORD_ID")
    private String punchcardrecord_id;

    /**
     * 姓名ID
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 所属グループID
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属チームID
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属センターID
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 日期
     */
    @Column(name = "PUNCHCARDRECORD_DATE")
    private Date punchcardrecord_date;

    /**
     * 首次打卡
     */
    @Column(name = "TIME_START")
    private Date time_start;

    /**
     * 末次打卡
     */
    @Column(name = "TIME_END")
    private String time_end;

}
