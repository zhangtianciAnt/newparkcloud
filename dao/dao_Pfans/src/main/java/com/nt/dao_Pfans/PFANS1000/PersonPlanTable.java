package com.nt.dao_Pfans.PFANS1000;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonPlanTable implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private String code;
    private String money46;
    private String money73;
    private String payhour;
    private String overtimehour;

    private int amount1 = 0;
    private BigDecimal workinghour1;
    private BigDecimal pay1;
    private BigDecimal giving1;

    private int amount2 = 0;
    private BigDecimal workinghour2;
    private BigDecimal pay2;
    private BigDecimal giving2;

    private int amount3 = 0;
    private BigDecimal workinghour3;
    private BigDecimal pay3;
    private BigDecimal giving3;

    private int amount4;
    private BigDecimal workinghour4;
    private BigDecimal pay4;
    private BigDecimal giving4;

    private int amount5 = 0;
    private BigDecimal workinghour5;
    private BigDecimal pay5;
    private BigDecimal giving5;

    private int amount6 = 0;
    private BigDecimal workinghour6;
    private BigDecimal pay6;
    private BigDecimal giving6;

    private int amount7 = 0;
    private BigDecimal workinghour7;
    private BigDecimal pay7;
    private BigDecimal giving7;

    private int amount8 = 0;
    private BigDecimal workinghour8;
    private BigDecimal pay8;
    private BigDecimal giving8;

    private int amount9 = 0;
    private BigDecimal workinghour9;
    private BigDecimal pay9;
    private BigDecimal giving9;

    private int amount10 = 0;
    private BigDecimal workinghour10;
    private BigDecimal pay10;
    private BigDecimal giving10;

    private int amount11 = 0;
    private BigDecimal workinghour11;
    private BigDecimal pay11;
    private BigDecimal giving11;

    private int amount12 = 0;
    private BigDecimal workinghour12;
    private BigDecimal pay12;
    private BigDecimal giving12;

    private int amountfirst;
    private BigDecimal workinghourfirst;
    private BigDecimal payfirst;
    private BigDecimal givingfirst;

    private int amountsecond;
    private BigDecimal workinghoursecond;
    private BigDecimal paysecond;
    private BigDecimal givingsecond;

    private int amounttotal;
    private BigDecimal workinghourtotal;
    private BigDecimal paytotal;
    private BigDecimal givingtotal;

}
