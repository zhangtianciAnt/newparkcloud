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
    @Column(name = "JAPANCONDOMINIUM_ID")
    private String japancondominiumid;

    /**
     * 卷类别
     */
    @Column(name = "COUPONTYPE")
    private String coupontype;

    /**
     * 残劵数
     */
    @Column(name = "COUPONNUMBER")
    private String couponnumber;

    /**
     * 卷价值
     */
    @Column(name = "COUPONVALUE")
    private String couponvalue;

    /**
     * 使用卷数
     */
    @Column(name = "COUPONUSENUMBER")
    private String couponusenumber;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
