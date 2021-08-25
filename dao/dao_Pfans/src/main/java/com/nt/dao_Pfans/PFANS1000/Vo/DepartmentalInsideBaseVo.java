package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentalInsideBaseVo {
    /*themeid*/
    private String themeinfor_id;
    /*theme名称*/
    private String themename;
    /*theme分类*/
    private String divide;
    /*theme委托元*/
    private String toolsorgs;
    /*合同号*/
    private String contractnumber;
    /*合同总金额*/
    private String claimamount;
    /*合同转台*/
    private String entrycondition;
    /*合同明细金额*/
    private String contracatamountdetail;
    /*项目主键*/
    private String companyprojects_id;
    /*项目人员id*/
    private String name;
    /*项目人员rank*/
    private String rank;
}
