package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bonus")
public class Bonus extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BONUS_ID")
    private String bonus_id;

    @Column(name = "YEARS")
    private String years;

    @Column(name = "BENAME")
    private String bename;

    @Column(name = "TOTALBONUS1")
    private String totalbonus1;

    @Column(name = "PAYMENTMET")
    private String paymentmet;

    @Column(name = "PAYABLE")
    private String payable;

    @Column(name = "TOTALBONUS2")
    private String totalbonus2;

    @Column(name = "SALARY")
    private String salary;

    @Column(name = "TAXPAYABLE")
    private String taxpayable;

    @Column(name = "MONTHLYAVERAGE")
    private String monthlyaverage;

    @Column(name = "TAXRATE")
    private String taxrate;

    @Column(name = "QUICKDEDUCTION")
    private String quickdeduction;

    @Column(name = "BONUSTAX")
    private String bonustax;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "REMARKS")
    private String remarks;


}
