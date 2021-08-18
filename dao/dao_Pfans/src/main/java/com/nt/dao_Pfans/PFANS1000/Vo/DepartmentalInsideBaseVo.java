package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentalInsideBaseVo {
    /*themeid*/
    private String THEMEINFOR_ID;
    /*theme名称*/
    private String THEMENAME;
    /*theme分类*/
    private String DIVIDE;
    /*theme委托元*/
    private String TOOLSORGS;
    /*合同号*/
    private String CONTRACTNUMBER;
    /*合同总金额*/
    private String CLAIMAMOUNT;
    /*合同转台*/
    private String ENTRYCONDITION;
    /*合同明细金额*/
    private String CONTRACATAMOUNTDETAIL;
    /*项目主键*/
    private String COMPANYPROJECTS_ID;
    /*项目人员id*/
    private String NAME;
    /*项目人员rank*/
    private String RANK;
}
