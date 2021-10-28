package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ruling")
public class Ruling extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * Id
     * */
    @Id
    @Column(name = "RULING_ID")
    private String ruling_id;

    /**
     * 年份
     * */
    @Column(name = "YEARS")
    private String years;

    /**
     * 部门
     * */
    @Column(name = "DEPART")
    private String depart;

    /**
     * 决裁种类code
     * */
    @Column(name = "CODE")
    private String code;

    /**
     * 计划消耗
     * */
    @Column(name = "PLANTOCONSUME")
    private String plantoconsume;

    /**
     * 申请中金额
     * */
    @Column(name = "APPLIOCCUTION")
    private BigDecimal applioccution;

    /**
     * 实际消耗
     * */
    @Column(name = "ACTUALCONSUMPTION")
    private BigDecimal actualconsumption;

    /**
     * 实际剩余
     * */
    @Column(name = "ACTUALRESIDUAL")
    private BigDecimal actualresidual;

    /**
     * 版本-锁
     * */
    @Column(name = "VERSION")
    private Long version;
}
