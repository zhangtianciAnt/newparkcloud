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
@Table(name = "business")
public class Business extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 出张申请ID
     */
    @Id
    @Column(name = "BUSINESS_ID")
    private String businessid;

    /**
     * 出差详细时间（移動記載必要）
     */
    @Column(name = "TIME")
    private Date time;

    /**
     * 出差详细地点
     */
    @Column(name = "PLACE")
    private String place;

    /**
     * 活动内容
     */
    @Column(name = "CONTENT")
    private String content;


}
