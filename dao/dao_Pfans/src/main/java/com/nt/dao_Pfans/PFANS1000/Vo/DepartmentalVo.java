package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentalVo {

    /*部门*/
    private String department;

    /*合同号*/
    private String contractnumber;

    /*合同总金额*/
    private String claimamount;

    /*合同回数*/
    private String claimtype;

    /*合同状态*/
    private String entrycondition;

    /*合同明细金额*/
    private String contracatamountdetail;

    /*theme名称*/
    private String themename;

    /*委托元分类*/
    private String divide;

    /*委托元名称*/
    private String toolsorgs;

    /*项目分配金额*/
    private String contractamount;

    /*项目号*/
    private String numbers;

    /*员工工数*/
    private String staffnum;

    /*外驻工数*/
    private String outstaffnum;

    /*外驻社名*/
    private String outcompany;

    /*项目id*/
    private String companyprojects_id;

    /*monthcast*/
    private String monthcast;
}
