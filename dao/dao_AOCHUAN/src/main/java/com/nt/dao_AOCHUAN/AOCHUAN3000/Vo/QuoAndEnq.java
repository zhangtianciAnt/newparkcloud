package com.nt.dao_AOCHUAN.AOCHUAN3000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoAndEnq {

    /*private Quotations quotations;

    private Enquiry enquiry;*/
    private String quotations_id;
    private String quotationsno;
    private String productch;
    private String producten;
    private String  account;
    private String quotedprice;
    private String  salesquotation;
    private String quote;
    //type = 0 销售询单录入 type = 1 采购待确认 type = 2 销售报价确认
    private int type;
}
