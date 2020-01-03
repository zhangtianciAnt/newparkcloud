package com.nt.dao_Assets;

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
@Table(name = "assets")
public class Assets extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 资产ID
     */
    @Id
    @Column(name = "ASSETS_ID")
    private String assets_id;

    /**
     * 盘点计划ID
     */
    @Column(name = "INVENTORYPLAN_ID")
    private String inventoryplan_id;

    /**
     * 件名
     */
    @Column(name = "FILENAME")
    private String filename;

    /**
     * 类型
     */
    @Column(name = "TYPEASSETS")
    private String typeassets;

    /**
     * 价格
     */
    @Column(name = "PRICE")
    private String price;

    /**
     * 购入时间
     */
    @Column(name = "PURCHASETIME")
    private Date purchasetime;

    /**
     * 使用部门
     */
    @Column(name = "USEDEPARTMENT")
    private String usedepartment;

    /**
     * 设备负责人
     */
    @Column(name = "PRINCIPAL")
    private String principal;

    /**
     * 资产状态
     */
    @Column(name = "ASSETSTATUS")
    private String assetstatus;

    /**
     * 在库状态
     */
    @Column(name = "STOCKSTATUS")
    private String stockstatus;

    /**
     * 条形码
     */
    @Column(name = "BARCODE")
    private String barcode;

    /**
     * 结果
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 索引
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

    /**
     * RFIDCD
     */
    @Column(name = "RFIDCD")
    private String rfidcd;

    /**
     * 条码类型
     */
    @Column(name = "BARTYPE")
    private String bartype;
}
