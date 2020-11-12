package com.nt.dao_AOCHUAN.AOCHUAN3000;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receivablesrecord")
public class Receivablesrecord extends BaseModel {

    private static final long serialVersionUID = 1L;
    @Id
    private String receivablesrecord_id;

    private String customername;

    private BigDecimal receamount;

    private Date receduedate;

    private String realamount;

    private Date paybackdate;

    private String paybackstatus;

    private String customerid;

    private String transportgood_id;

    private String productid;

    private String currency;//币种
    private String numbers;//数量
    private String unit;//单位
    private String unitprice;//单价

    @Transient
    private String productname;
}
