package com.nt.dao_AOCHUAN.AOCHUAN3000;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "saledetails")
public class Saledetails extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String saledetails_id;

    private String productid;

    private String productname;

    private String currency;

    private BigDecimal numbers;

    private String unit;

    private String supplierid;

    private String suppliername;

    private BigDecimal unitprice1;

    private BigDecimal unitprice;

    private BigDecimal salesamount;

    private String note;

    private String currency1;

    private BigDecimal numbers1;

    private BigDecimal purchaseamount;

    private Date deliverytime;

    private String deliverystatus;

    private String signback1;

    private String hscode;

    private String drawback;

    private String dangerous;

    private String marks;

    private String note1;

    private String transportgood_id;

    private String casnum;

}
