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
/**
 * pl成本转接表
 */
public class Pltab extends BaseModel {

	private static final long serialVersionUID = 1L;

    private String pj;//项目-
    private String pj1;//共同项目-

    private String outst1;//外部受託-
     private String outst2;
     private String outst3;

    private String tax;//税金

    private String inst;//内部受託-

    private String intotal;//収入合計

    private String emhours;//社員工数-

    private String inhours;//外注工数(构内)-

    private String outhours;//外注工数(构外)-

    private String peocost;//人件費(工资没有暂时没绑)

    private String outcost;//外注費

    private String inwetuo;//内部委託

    private String researchcost;//研究材料費-

    private String departmentcom;//部門共通按分（A是共通费用）

    private String yuanqincost;//原動費

    private String travalcost;//旅費-

    private String concost;//消耗费-

    private String callcost;//通信費-

    private String brandcost;//ブランド料-

    private String rent;//家賃-

    private String other;//その他-

    private String departmenttotal;//部門共通費用合計

    private String allocation;//配賦費用

    private String costtotal;//支出合計

    private String process;//仕掛品処理

    private String marginal;//限界利益

    private String Operating;//営業利益
    private String centerintotal;
    private String twocost;
    private String peocostsum;
    private String depreciationsoft;
    private String depreciationequipment;
    private String leasecost;
    private String temporaryrent;
    private String costsubtotal;
    private String surveyfee;
    private String othersoftwarefree;
    private String expensessubtotal;
    private String transferone;
    private String transfertwo;
    private String allocationsum;
    private String threefree;
    private String commonfee;
    private String otherexpenses;
    private String otherincome;
    private String otherexpentotal;
    private String interestrate;
    private String operatingprofit;
    private String pretaxprofit;
    private String taxallowance;
    private String posttaxbenefit;
    private String operatingmargin;
    private String outsourcinghours;
    private String outsourcingname;
    private String employeename;
    private String outsourcingpjhours;
    private String employeepjhours;
    private String employeeuptime;
    private String externalpjrate;
    private String externalinjectionrate;
    private String memberpjrate;
    private String membershiprate;
    private String pjrateemployees;
    private String staffingrate;


}
