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
}
