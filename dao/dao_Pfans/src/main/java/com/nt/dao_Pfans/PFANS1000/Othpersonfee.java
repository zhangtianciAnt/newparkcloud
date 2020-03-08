package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "othpersonfee")
public class Othpersonfee extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "OTHPERSONFEE_ID")
    private String othpersonfeeid;

    @Id
    @Column(name = "QUOTATION_ID")
    private String quotationid;

    @Column(name = "DETAILED1")
    private String detailed1;

    @Column(name = "COST1")
    private String cost1;

    @Column(name = "UNIT1")
    private String unit1;

    @Column(name = "AMOUNT1")
    private String amount1;

    @Column(name = "DETAILED2")
    private String detailed2;

    @Column(name = "COST2")
    private String cost2;

    @Column(name = "UNIT2")
    private String unit2;

    @Column(name = "AMOUNT2")
    private String amount2;

    @Column(name = "DETAILED3")
    private String detailed3;

    @Column(name = "COST3")
    private String cost3;

    @Column(name = "UNIT3")
    private String unit3;

    @Column(name = "AMOUNT3")
    private String amount3;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
