package com.nt.dao_Pfans.PFANS1000.Vo;

import com.alibaba.fastjson.JSONObject;
import com.nt.utils.BigDecimalUtils;
import lombok.Data;

import java.math.BigDecimal;


public class BusinessTableOBase extends JSONObject {
    private String programme;
    private String sprogramme;
    private Integer price;
    private String type;
    private Integer number4;
    private Integer number5;
    private Integer number6;
    private Integer number7;
    private Integer number8;
    private Integer number9;
    private Integer number10;
    private Integer number11;
    private Integer number12;
    private Integer number1;
    private Integer number2;
    private Integer number3;
    private String numbertotal;
    private Boolean disableEdit;
    private String moneytotal;
    private String money4;
    private String money5;
    private String money6;
    private String money7;
    private String money8;
    private String money9;
    private String money10;
    private String money11;
    private String money12;
    private String money1;
    private String money2;
    private String money3;
    private String numberfirst;
    private String numbersecond;
    private String moneyfirst;
    private String moneysecond;

    public String getSprogramme(){
        return sprogramme;
    }


//    public String getNumberFirstAnt(){
//        return String.valueOf(this.getNumber4() + this.getNumber5() + this.getNumber6() + this.getNumber7() + this.getNumber8() + this.getNumber9());
////        return BigDecimalUtils.sum(
////                String.valueOf(this.getNumber4()), String.valueOf(this.getNumber5()), String.valueOf(this.getNumber6()),
////                String.valueOf(this.getNumber7()), String.valueOf(this.getNumber8()), String.valueOf(this.getNumber9())
////        );
//    }
//    public String getNumberSecondAnt(){
//        return String.valueOf(this.getNumber10() + this.getNumber11() + this.getNumber12() + this.getNumber1() + + this.getNumber2() + + this.getNumber3());
////        return BigDecimalUtils.sum(
////                String.valueOf(this.getNumber10()),  String.valueOf(this.getNumber11()),  String.valueOf(this.getNumber12()),
////                String.valueOf(this.getNumber1()),  String.valueOf(this.getNumber2()),  String.valueOf(this.getNumber3())
////        );
//    }
//    public String getMoneyFirstAnt(){
//        return BigDecimalUtils.sum(
//                this.getMoney4(), this.getMoney5(), this.getMoney6(),
//                this.getMoney7(), this.getMoney8(), this.getMoney9()
//        );
//    }
//    public String getMoneySecondAnt(){
//        return BigDecimalUtils.sum(
//                this.getMoney10(), this.getMoney11(), this.getMoney12(),
//                this.getMoney1(), this.getMoney2(), this.getMoney3()
//        );
//    }
//    public String getNumberTotal(){
//        return BigDecimalUtils.sum(
//                this.getNumberFirstAnt(), this.getNumberSecondAnt()
//        );
//    }
//    public String getMoneyTotal(){
//        return BigDecimalUtils.sum(
//                this.getMoneyFirstAnt(), this.getMoneySecondAnt()
//        );
//    }
}
