package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "coststatisticsdetail")
public class Coststatisticsdetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 費用集計详细
     */
    @Id
    @Column(name = "COSTSTATISTICSDETAIL_ID")
    private String coststatisticsdetail_id;

    /**
     * GROUPID
     */
    @Column(name = "GROUP_ID")
    private String groupid;

    /**
     * GROUPNAME
     */
    @Column(name = "GROUP_NAME")
    private String groupname;

    //供应商
    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinforid;

    //年月
    @Column(name = "DATES")
    private String dates;

    /**
     * 工数
     */
    @Column(name = "MANHOUR")
    private String manhour;

    /**
     * 费用
     */
    @Column(name = "cost")
    private String cost;
}
