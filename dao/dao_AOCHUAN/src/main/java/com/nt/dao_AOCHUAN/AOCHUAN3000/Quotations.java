package com.nt.dao_AOCHUAN.AOCHUAN3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quotations")
public class Quotations extends BaseModel {
    @Id
    private String quotations_id;

    private Date inquirydate;

    private String producten;

    private String producttype;

    private String account;

    private String saleresponsibility;

    private String targetmarket;

    private String purpose;

    private String terminal;

    private String others;

    private String quote;

    private String sample;

    private String productch;

    private String cas;

    private String productresponsibility;

    private String quotationsno;

    //type = 0 销售询单录入 type = 1 采购待确认 type = 2 销售报价确认
    private Integer type;

    private String productsid;

    private String accountid;

    @Transient
    private List<Enquiry> enquiry;

    @Transient
    private boolean notice;
}
