package com.nt.dao_Pfans.PFANS8000;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "monthlyrate")
public class MonthlyRate  extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MONTHLYRATEID")
    private String monthlyrate_id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "MONTH")
    private String month;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BUSINESSPLANEXCHANGERATE")
    private String businessplanexchangerate;

    @Column(name = "ACCOUNTINGEXCHANGERATE")
    private String accountingexchangerate;

    @Column(name = "EXCHANGERATE")
    private String exchangerate;

    @Column(name = "RMARKS")
    private String rmarks;

    @Column(name = "INDEXDATA")
    private Integer indexdata;

}
