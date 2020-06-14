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
}
