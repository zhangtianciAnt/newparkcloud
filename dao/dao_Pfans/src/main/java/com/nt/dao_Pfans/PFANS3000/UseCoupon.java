package com.nt.dao_Pfans.PFANS3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usecoupon")
public class UseCoupon extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 使用券ID
     */
    @Id
    @Column(name = "USECOUPON_ID")
    private String usecouponid;

    /**
     * 日本出張のマンション予約ID
     */
    @Id
    @Column(name = "JAPANCONDOMINIUM_ID")
    private String japancondominiumid;

    /**
     * 卷类别
     */
    @Column(name = "COPUNTYPE")
    private String copuntype;

    /**
     * 残劵数
     */
    @Column(name = "COPUNNUMBER")
    private String copunnumber;

    /**
     * 卷价值
     */
    @Column(name = "COPUNVALUE")
    private String copunvalue;

    /**
     * 使用卷数
     */
    @Column(name = "COPUNUSENUMBER")
    private String copunusenumber;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
