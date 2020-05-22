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
@Table(name = "ticketsdetails")

public class Ticketsdetails extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 机票明细主键
     */
    @Id
    @Column(name = "TICKETSDETAILID")
    private String ticketsdetailid;

    /**
     * 机票主表主键
     */
    @Id
    @Column(name = "TICKETS_ID")
    private String tickets_id;

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
     * 序号
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;


}
