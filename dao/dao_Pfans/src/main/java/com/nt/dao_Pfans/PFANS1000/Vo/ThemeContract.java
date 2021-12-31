package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemeContract implements Serializable {
    //验收月
    private String comyearmonth;
    //该当合同投入社员工数
    private String remark;
    //合同（合同号）
    private String contractnumber;
    //合同（合同号-第N回）
    private String contractnumbercon;
    //验收完了日
    private String completiondate;
    //合同回数金额
    private String claimamount;
    //合同币种
    private String currencyposition;
    private String actual04;
    private String actual05;
    private String actual06;
    private String actual07;
    private String actual08;
    private String actual09;
    private String actual10;
    private String actual11;
    private String actual12;
    private String actual01;
    private String actual02;
    private String actual03;
}
