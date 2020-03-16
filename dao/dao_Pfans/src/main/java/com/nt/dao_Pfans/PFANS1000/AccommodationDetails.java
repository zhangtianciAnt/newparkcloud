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

    @Column(name = "NEXTDAY")
    private String nextday;

    @Column(name = "BUDGETCODING")
    private String budgetcoding;

    @Column(name = "SUBJECTNUMBER")
    private String subjectnumber;

    /**
     * 日期
     */
    @Column(name = "ACCOMMODATIONDATE")
    private String accommodationdate;

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
    private String movementtime;

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
     * 住宿津贴（人民币）
     */
    @Column(name = "ACCOMMODATIONALLOWANCE")
    private String accommodationallowance;

    /**
     * 住宿津贴（外币）
     */
    @Column(name = "ACCOMMODATION")
    private String accommodation;

    /**
     * 出差津贴（人民币）
     */
    @Column(name = "TRAVELALLOWANCE")
    private String travelallowance;

    /**
     * 出差津贴（外币）
     */
    @Column(name = "TRAVEL")
    private String travel;

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
     * 飞机过夜津贴（夜间6小时以上）（外币）
     */
    @Column(name = "PLANE")
    private String plane;

    /*
     * 出張地域
     * */
    @Column(name = "REGION")
    private String region;


    /**
     * 附件号
     */
    @Column(name = "ANNEXNO")
    private String annexno;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

    @Column(name = "INVOICENUMBER")
    private String invoicenumber;

    @Column(name = "DEPARTMENTNAME")
    private String departmentname;

    @Column(name = "COSTITEM")
    private String costitem;

    @Column(name = "TAXES")
    private String  taxes;
}
