package com.nt.dao_AOCHUAN.AOCHUAN3000;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//询价情况
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enquiry {

     private String supplier;
     private String iso;
     private String process;
     private String antecedents;
     private String note;
     private String profitpoint;
     private String drawback;
     private String addtax;
     private String salesquotation;
     private String productch;
     private String quotedprice; //报关价/采购报价
     private String flag; //区分
     private String exchangerate; //汇率
     private String tariffrate;  //关税率
     private String[] document;

}
