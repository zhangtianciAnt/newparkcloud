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
@Table(name = "inventoryplan")
public class Inventoryplan extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 盘点计划ID
     */
    @Id
    @Column(name = "INVENTORYPLAN_ID")
    private String inventoryplan_id;

    /**
     * 资产
     */
    @Column(name = "ASSETS_ID")
    private String assets_id;

    /**
     * 盘点周期
     */
    @Column(name = "INVENTORYCYCLE")
    private String inventorycycle;

    /**
     * 总设备数
     */
    @Column(name = "TOTALNUMBER")
    private String totalnumber;

    /**
     * 盘点到数量
     */
    @Column(name = "INQUANTITY")
    private String inquantity;

    /**
     * 未盘点到数量
     */
    @Column(name = "UNQUANTITY")
    private Date unquantity;

    /**
     * 负责人
     */
    @Column(name = "USERID")
    private String userid;

}
