package com.nt.dao_Pfans.PFANS3000;

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
@Table(name = "tickets")

public class Tickets extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 机票ID
     */
    @Id
    @Column(name = "TICKETS_ID")
    private String tickets_id;

    /**
     * Center
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * Group
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * Team
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 类别
     */
    @Column(name = "TICKETSTYPE")
    private String ticketstype;

    /**
     * 身份证号码
     */
    @Column(name = "IDCARD")
    private String idcard;

    /**
     * パスポート
     */
    @Column(name = "PASSPORT")
    private String passport;

    /**
     * 有効期
     */
    @Column(name = "EFFECTIVEDATE")
    private Date effectivedate;

    /**
     * 罗马字
     */
    @Column(name = "ROMANID")
    private String romanid;

    /**
     * 内線番号
     */
    @Column(name = "EXTENSIONNUMBER")
    private String extensionnumber;

    /**
     * 予算番号
     */
    @Column(name = "BUDGETNUMBER")
    private String budgetnumber;

    /**
     * 携帯電話
     */
    @Column(name = "MOBILEPHONE")
    private String mobilephone;

    /**
     * 出張地点
     */
    @Column(name = "TRIPPOINT")
    private String trippoint;

    /**
     * 行き
     */
    @Column(name = "GOING")
    private String going;

    /**
     * 航空番号
     */
    @Column(name = "GOAIRLINENUMBER")
    private String goairlinenumber;

    /**
     * 出発時間
     */
    @Column(name = "GODEPARTUREDATE")
    private Date godeparturedate;

    /**
     * 到着時間
     */
    @Column(name = "GOARRIVALDATE")
    private Date goarrivaldate;

    /**
     * 帰り
     */
    @Column(name = "BACK")
    private String back;

    /**
     * 航空番号
     */
    @Column(name = "REAIRLINENUMBER")
    private String reairlinenumber;

    /**
     * 出発時間
     */
    @Column(name = "REDEPARTUREDATE")
    private Date redeparturedate;

    /**
     * 到着時間
     */
    @Column(name = "REARRIVALDATE")
    private Date rearrivaldate;

    /**
     * 発券時間
     */
    @Column(name = "TICKETINGDATE")
    private Date ticketingdate;

    /**
     * 出張开始時間
     */
    @Column(name = "TRIPSTART")
    private Date tripstart;

    /**
     * 出張结束時間
     */
    @Column(name = "TRIPEND")
    private Date tripend;

}
