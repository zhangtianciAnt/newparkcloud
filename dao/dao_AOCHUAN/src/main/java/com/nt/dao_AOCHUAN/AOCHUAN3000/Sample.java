package com.nt.dao_AOCHUAN.AOCHUAN3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sample")
public class Sample extends BaseModel {
    @Id
    private String sample_id;

    private String sampleorder;

    private String productnamech;

    private String productnameen;

    private String specification;

    private String customername;

    private String amount;

    private String expressnumber;

    private String receivecondition;

    private String suppliername;

    private String samplenumber;

    private String saleresponsibility;

    private String productresponsibility;

    //type = 0 销售样品录入 type = 1 采购待确认 type = 2 销售待确认
    private int type;

    @Transient
    private boolean notice;

}
