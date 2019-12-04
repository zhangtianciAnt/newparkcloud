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
@Table(name = "assetinformation")
public class Assetinformation extends BaseModel {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ASSETINFORMATION_ID")
    private String assetinformationid;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "FILENAME")
    private String filename;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "PROCESSINGMETHOD")
    private String processingmethod;

    @Column(name = "SALECONTRACT")
    private String salecontract;

    @Column(name = "CONTRACTNO")
    private String contractno;

    @Column(name = "NUMBERS")
    private String numbers;

    @Column(name = "TOTALVALUE")
    private String totalvalue;

    @Column(name = "TOTALNETWORTH")
    private String totalnetworth;

    @Column(name = "SALEQUOTATION")
    private String salequotation;

    @Column(name = "REASONSFORQUOTATION")
    private String reasonsforquotation;

    @Column(name = "UPLOADFILE")
    private String uploadfile;

}
