package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.Data;

import java.util.Date;

@Data
public class ContractVo {

    private String PROJECT_NAME;

    private String PROJECT_NAMEJP;

    private String DEPLOYMENT;

    private String CLAIMTYPE;

    private Date DELIVERYDATE;

    private Date COMPLETIONDATE;

    private Date CLAIMDATE;

    private Date SUPPORTDATE;

    private String CLAIMAMOUNT;

    private String CURRENCYPOSITION;
}
