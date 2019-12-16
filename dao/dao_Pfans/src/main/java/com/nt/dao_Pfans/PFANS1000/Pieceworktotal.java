package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pieceworktotal")
public class Pieceworktotal extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 人件費計画合計ID
     */
    @Id
    @Column(name = "Pieceworktotal_ID")
    private String pieceworktotal_id;

    /**
     * 事业计划ID
     */
    @Column(name = "BUSINESSPLAN_ID")
    private String businessplanid;

    @Column(name = "PAY4")
    private String pay4;

    @Column(name = "OVERTIMEPAY4")
    private String overtimepay4;

    @Column(name = "SUBTOTAL4")
    private String subtotal4;

    @Column(name = "SUBTOTALTHOUSAND4")
    private String subtotalthousand4;

    @Column(name = "PAY5")
    private String pay5;

    @Column(name = "OVERTIMEPAY5")
    private String overtimepay5;

    @Column(name = "SUBTOTAL5")
    private String subtotal5;

    @Column(name = "SUBTOTALTHOUSAND5")
    private String subtotalthousand5;

    @Column(name = "PAY6")
    private String pay6;

    @Column(name = "OVERTIMEPAY6")
    private String overtimepay6;

    @Column(name = "SUBTOTAL6")
    private String subtotal6;

    @Column(name = "SUBTOTALTHOUSAND6")
    private String subtotalthousand6;

    @Column(name = "PAY7")
    private String pay7;

    @Column(name = "OVERTIMEPAY7")
    private String overtimepay7;

    @Column(name = "SUBTOTAL7")
    private String subtotal7;

    @Column(name = "SUBTOTALTHOUSAND7")
    private String subtotalthousand7;

    @Column(name = "PAY48")
    private String pay8;

    @Column(name = "OVERTIMEPAY8")
    private String overtimepay8;

    @Column(name = "SUBTOTAL8")
    private String subtotal8;

    @Column(name = "SUBTOTALTHOUSAND8")
    private String subtotalthousand8;

    @Column(name = "PAY9")
    private String pay9;

    @Column(name = "OVERTIMEPAY9")
    private String overtimepay9;

    @Column(name = "SUBTOTAL9")
    private String subtotal9;

    @Column(name = "SUBTOTALTHOUSAND10")
    private String subtotalthousand10;

    @Column(name = "PAY11")
    private String pay11;

    @Column(name = "OVERTIMEPAY11")
    private String overtimepay11;

    @Column(name = "SUBTOTAL11")
    private String subtotal11;

    @Column(name = "SUBTOTALTHOUSAND11")
    private String subtotalthousand11;

    @Column(name = "PAY12")
    private String pay12;

    @Column(name = "OVERTIMEPAY12")
    private String overtimepay12;

    @Column(name = "SUBTOTAL12")
    private String subtotal12;

    @Column(name = "SUBTOTALTHOUSAND12")
    private String subtotalthousand12;

    @Column(name = "PAY1")
    private String pay1;

    @Column(name = "OVERTIMEPAY1")
    private String overtimepay1;

    @Column(name = "SUBTOTAL1")
    private String subtotal1;

    @Column(name = "SUBTOTALTHOUSAND1")
    private String subtotalthousand1;

    @Column(name = "PAY2")
    private String pay2;

    @Column(name = "OVERTIMEPAY2")
    private String overtimepay2;

    @Column(name = "SUBTOTAL2")
    private String subtotal2;

    @Column(name = "SUBTOTALTHOUSAND2")
    private String subtotalthousand2;

    @Column(name = "PAY3")
    private String pay3;

    @Column(name = "OVERTIMEPAY3")
    private String overtimepay3;

    @Column(name = "SUBTOTAL3")
    private String subtotal3;

    @Column(name = "SUBTOTALTHOUSAND3")
    private String subtotalthousand3;

    @Column(name = "PAYUPPER")
    private String payupper;

    @Column(name = "OVERTIMEPAYUPPER")
    private String overtimepayupper;

    @Column(name = "SUBTOTALUPPER")
    private String subtotalupper;

    @Column(name = "SUBTOTALTHOUSANDUPPER")
    private String subtotalthousandupper;

    @Column(name = "PAYLOWER")
    private String paylower;

    @Column(name = "OVERTIMEPAYLOWER")
    private String overtimepaylower;

    @Column(name = "SUBTOTALPAYLOWER")
    private String subtotalpaylower;

    @Column(name = "SUBTOTALTHOUSANDLOWER")
    private String subtotalthousandlower;

    @Column(name = "PAYYEAR")
    private String payyear;

    @Column(name = "OVERTIMEPAYYEAR")
    private String overtimepayyear;

    @Column(name = "SUBTOTALYEAR")
    private String subtotalyear;

    @Column(name = "SUBTOTALTHOUSANDYEAR")
    private String subtotalthousandyear;

    /**
     * 排序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
