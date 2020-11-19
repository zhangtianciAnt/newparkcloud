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
@Table(name = "applicationrecord")
public class Applicationrecord extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String applicationrecord_id;

    private String suppliername;

    private String supplierid;

    private BigDecimal realpay;

    private Date realdate;

    private String realamount;

    private Date paiddate;

    private String invoiceno;

    private String paymentstatus;

    private String transportgood_id;

    private String productid;

    private String unit;

    private String numbers1;

    private String unitprice1;

    private String currency1;
    //采购总金额
    private String sumamount;

    @Transient
    private String productname;
}
