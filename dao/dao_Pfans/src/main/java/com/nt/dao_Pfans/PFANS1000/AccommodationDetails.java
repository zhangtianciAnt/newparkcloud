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
@Table(name = "accommodationdetails")
public class AccommodationDetails extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 住宿费明细ID
     */
    @Id
    @Column(name = "ACCOMMODATIONDETAILS_ID")
    private String accommodationdetails_id;

    /**
     * 境内/外出差精算ID
     */
    @Column(name = "EVECTION_ID")
    private String evectionid;

    /**
     * 日期
     */
    @Column(name = "ACCOMMODATIONDATE")
    private Date accommodationdate;

    /**
     * 活动内容
     */
    @Column(name = "ACTIVITYCONTENT")
    private String activitycontent;

    /**
     * 交通工具
     */
    @Column(name = "VEHICLE")
    private String vehicle;

    /**
     * 移动时间
     */
    @Column(name = "MOVEMENTTIME")
    private Date movementtime;

    /**
     * 城市
     */
    @Column(name = "CITY")
    private String city;

    /**
     * 设施类型
     */
    @Column(name = "FACILITYTYPE")
    private String facilitytype;

    /**
     * 设施名字
     */
    @Column(name = "FACILITYNAME")
    private String facilityname;

    /**
     * 住宿津贴
     */
    @Column(name = "ACCOMMODATIONALLOWANCE")
    private String accommodationallowance;

    /**
     * 出差津贴
     */
    @Column(name = "TRAVELALLOWANCE")
    private String travelallowance;

    /**
     * 自宅/亲属家津贴
     */
    @Column(name = "RELATIVES")
    private String relatives;

    /**
     * 火车硬座（夜间6小时以上）
     */
    @Column(name = "TRAIN")
    private String train;

    /**
     * 附件号
     */
    @Column(name = "ANNEX")
    private String annex;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;


}
