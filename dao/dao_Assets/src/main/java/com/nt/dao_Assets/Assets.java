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

    /**
     * PC管理号
     */
    @Column(name = "PCNO")
    private String pcno;

    /**
     * 帐面净值
     */
    @Column(name = "REALPRICE")
    private String realprice;

    /**
     * 型号
     */
    @Column(name = "MODEL")
    private String model;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 序列号
     */
    @Column(name = "NO")
    private String no;

    /**
     * 启用日期
     */
    @Column(name = "ACTIVITIONDATE")
    private String activitiondate;

    /**
     * 地点
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * PSDCD_借还情况
     */
    @Column(name = "PSDCDDEBITSITUATION")
    private String psdcddebitsituation;

    /**
     * PSDCD_带出理由
     */
    @Column(name = "PSDCDBRINGOUTREASON")
    private String psdcdbringoutreason;

    /**
     * PSDCD_期间
     */
    @Column(name = "PSDCDPERIOD")
    private String psdcdperiod;

    /**
     * PSDCD_对方单位
     */
    @Column(name = "PSDCDCOUNTERPARTY")
    private String psdcdcounterparty;

    /**
     * PSDCD_责任人
     */
    @Column(name = "PSDCDRESPONSIBLE")
    private String psdcdresponsible;

    /**
     * PSDCD_归还确认
     */
    @Column(name = "PSDCDRETURNCONFIRMATION")
    private String psdcdreturnconfirmation;

    /**
     * 资产编号
     */
    @Column(name = "ASSETNUMBER")
    private String assetnumber;

    /**
     * 入荷確認_INVOICEと一致性
     */
    @Column(name = "INPARAMS1")
    private String inparams1;

    /**
     * 入荷確認_設備写真あるか
     */
    @Column(name = "INPARAMS2")
    private String inparams2;

    /**
     * 入荷確認_輸出部門担当者
     */
    @Column(name = "INPARAMS3")
    private String inparams3;

    /**
     * 入荷確認_実施日
     */
    @Column(name = "INPARAMS4")
    private Date inparams4;

    /**
     * 入荷確認_動作状況
     */
    @Column(name = "INPARAMS5")
    private String inparams5;

    /**
     * 入荷確認_現場担当者
     */
    @Column(name = "INPARAMS6")
    private String inparams6;

    /**
     * 入荷確認_実施日
     */
    @Column(name = "INPARAMS7")
    private Date inparams7;

    /**
     * 入荷確認_備考
     */
    @Column(name = "INPARAMS8")
    private String inparams8;

    /**
     * 出荷確認_動作状況
     */
    @Column(name = "OUTPARAMS1")
    private String outparams1;

    /**
     * 出荷確認_現場実施者
     */
    @Column(name = "OUTPARAMS2")
    private String outparams2;

    /**
     * 出荷確認_実施日
     */
    @Column(name = "OUTPARAMS3")
    private Date outparams3;

    /**
     * 出荷確認_INVOICEとの一致性
     */
    @Column(name = "OUTPARAMS4")
    private String outparams4;

    /**
     * 出荷確認_入荷写真との一致性
     */
    @Column(name = "OUTPARAMS5")
    private String outparams5;

    /**
     * 出荷確認_梱包状況
     */
    @Column(name = "OUTPARAMS6")
    private String outparams6;
    /**
     * 出荷確認_現場担当者
     */
    @Column(name = "OUTPARAMS7")
    private String outparams7;

    /**
     * 出荷確認_輸出部門担当者
     */
    @Column(name = "OUTPARAMS8")
    private String outparams8;

    /**
     * 出荷確認_実施日
     */
    @Column(name = "OUTPARAMS9")
    private Date outparams9;

    /**
     * 出荷確認_最終確認
     */
    @Column(name = "OUTPARAMS10")
    private String outparams10;

    /**
     * 出荷確認_現場TL
     */
    @Column(name = "OUTPARAMS11")
    private String outparams11;

    /**
     * 出荷確認_輸出部門TL
     */
    @Column(name = "OUTPARAMS12")
    private String outparams12;

    /**
     * 出荷確認_実施日
     */
    @Column(name = "OUTPARAMS13")
    private Date outparams13;

    /**
     * 出荷確認_備考
     */
    @Column(name = "OUTPARAMS14")
    private String outparams14;
}
