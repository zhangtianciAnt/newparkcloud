package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "travelcontent")
public class TravelContent extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 出差内容ID
     */
    @Id
    @Column(name = "TRAVELCONTENT_ID")
    private String travelcontent_id;

    /**
     * 出张申请ID
     */
    @Column(name = "BUSINESS_ID")
    private String businessid;

    /**
     * 申请开始日期
     */
    @Column(name = "DURINGDATE1")
    private String duringdate1;

    /**
     * 开始活动内容
     */
    @Column(name = "CONTENT1")
    private String content1;

    /**
     * 申请日期区间
     */
    @Column(name = "DURINGDATE2")
    private String duringdate2;

    /**
     * 活动内容
     */
    @Column(name = "CONTENT2")
    private String content2;

    /**
     * 申请结束日期
     */
    @Column(name = "DURINGDATE3")
    private String duringdate3;

    /**
     * 结束活动内容
     */
    @Column(name = "CONTENT3")
    private String content3;


    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;


}
