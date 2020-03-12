package com.nt.dao_Pfans.PFANS6000;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delegainformation")
public class Delegainformation {
    private static final long serialVersionUID = 1L;

    /**
     * 委托信息
     */
    @Id
    @Column(name = "DELEGAINFORMATION_ID")
    private String delegainformation_id;

    /**
     * pj操作起案（外键）
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;


    /**
     * 协力公司
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 外注人员ID
     */
    @Column(name = "SUPPLIERNAMEID")
    private String suppliernameid;


}
