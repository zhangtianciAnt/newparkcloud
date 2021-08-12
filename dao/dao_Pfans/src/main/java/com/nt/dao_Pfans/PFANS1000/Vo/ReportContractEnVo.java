package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportContractEnVo {

    //合同
    private Contractapplication contractapplication;
    //请求回数
    private List<Contractnumbercount> contractnumbercountList;
    //决裁书
    private List<Award> awardList;

    //复合合同
    private List<Contractcompound> contractcompoundList;
    //报价单
    private List<Quotation> quotationList;
    //契约书
    private List<Contract> contractList;
    //纳品书
    private List<Napalm> napalmList;
    //请求书
    private List<Petition> petitionList;

}
