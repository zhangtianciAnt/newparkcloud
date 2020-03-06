package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "unusedevice")
public class Unusedevice extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 设备主键
     */
    @Id
    @Column(name = "UNUSEDEVICEID")
    private String unusedeviceid;

    /**
     * 決裁願外键
     */
    @Column(name = "JUDGEMENT_ID")
    private String judgement_id;

    /**
     * 设备名
     */
    @Column(name = "DEVICENAME")
    private String devicename;

    /**
     * 数量
     */
    @Column(name = "QUANTITY")
    private String quantity;

    /**
     * 单价（外汇）
     */
    @Column(name = "UNITPRICE")
    private String unitprice;

    /**
     * 价格
     */
    @Column(name = "PRICE")
    private String price;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
