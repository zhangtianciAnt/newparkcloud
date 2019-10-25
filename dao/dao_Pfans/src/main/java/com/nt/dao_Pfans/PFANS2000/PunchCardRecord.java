package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "punchcardrecord")

public class PunchCardRecord extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 打卡记录ID
     */
    @Id
    @Column(name = "PUNCHCARDRECORD_ID")
    private String punchcardrecordid;

    /**
     * 姓名ID
     */
    @Column(name = "USER_ID")
    private String userid;

    /**
     * 所属グループID
     */
    @Column(name = "CENTER_ID")
    private String centerid;

    /**
     * 所属チームID
     */
    @Column(name = "GROUP_ID")
    private String groupid;

    /**
     * 所属センターID
     */
    @Column(name = "TEAM_ID")
    private String teamid;

    /**
     * 日期
     */
    @Column(name = "PUNCHCARDRECORD_DATE")
    private Date punchcardrecorddate;

    /**
     * 首次打卡
     */
    @Column(name = "TIME_START")
    private Date timestart;

    /**
     * 末次打卡
     */
    @Column(name = "TIME_END")
    private String timeend;

}
