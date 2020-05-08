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
@Table(name = "costcarryforward")
public class CostCarryForward extends BaseModel {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COSTCARRYFORWARD_ID")
    private String costcarryforward_id;
    //    年
    @Column(name = "YEAR")
    private String year;
    //    月
    @Column(name = "REGION")
    private String region;
    //    GM部门
    @Column(name = "GROUP_ID")
    private String group_id;
//    詳細
    @Column(name = "PJ")
    private String pj;

    //    项目ID
   @Column(name = "PJ1")
    private String pj1;

//    技術開発・海外役務
    @Column(name = "OUTST1")
    private String outst1;
//    国内役務（6%税込み）
    @Column(name = "TAXYW")
    private String taxyw;
    //   国内販売（13%税込み）
    @Column(name = "TAXSA")
    private String taxsa;
//    内部受託
    @Column(name = "INST")
    private String inst;
//    部門売上合計
    @Column(name = "CENTERINTOTAL")
    private String centerintotal;
//    売上合計
    @Column(name = "INTOTAL")
    private String intotal;
//    人件費
    @Column(name = "PEOCOST")
    private String peocost;
//    厚生費
    @Column(name = "TWOCOST")
    private String twocost;
//    人件費小計
    @Column(name = "PEOCOSTSUM")
    private String peocostsum;
//    減価償却費（設備）
    @Column(name = "DEPRECIATIONSOFT")
    private String depreciationsoft;
//    減価償却費（ソフト）
    @Column(name = "DEPRECIATIONEQUIPMENT")
    private String depreciationequipment;
//    オフィス家賃
    @Column(name = "RENT")
    private String rent;
//    リース費
    @Column(name = "LEASECOST")
    private String leasecost;
//    出向者賃借料
    @Column(name = "TEMPORARYRENT")
    private String temporaryrent;
//    その他(固定費)
    @Column(name = "OTHER")
    private String other;
    //    固定資産費用小計
    @Column(name = "COSTSUBTOTAL")
    private String costsubtotal;
    //    研究材料費
    @Column(name = "RESEARCHCOST")
    private String researchcost;
    //    調査費
    @Column(name = "SURVEYFEE")
    private String surveyfee;
    //    内部委託支出
    @Column(name = "INWETUO")
    private String inwetuo;
    //    外注費
    @Column(name = "OUTCOST")
    private String outcost;
    //    その他(ソフト費)
    @Column(name = "OTHERSOFTWAREFREE")
    private String othersoftwarefree;
    //    研究開発費・ソフト費用小計
    @Column(name = "DEPARTMENTTOTAL")
    private String departmenttotal;
    //    管理・共通部門配賦
    @Column(name = "EXPENSESSUBTOTAL")
    private String expensessubtotal;
    //    振替１
    @Column(name = "TRANSFERONE")
    private String transferone;
    //    振替２
    @Column(name = "TRANSFERTWO")
    private String transfertwo;
    //    配賦部門費小計
    @Column(name = "ALLOCATIONSUM")
    private String allocationsum;
    //    原動費
    @Column(name = "YUANQINCOST")
    private String yuanqincost;
    //    旅費
    @Column(name = "TRAVALCOST")
    private String travalcost;
    //    通信費
    @Column(name = "CALLCOST")
    private String callcost;
    //    消耗費
    @Column(name = "CONCOST")
    private String concost;
    //    会議費/交際費/研修費
    @Column(name = "THREEFREE")
    private String threefree;
    //    共同事務費
    @Column(name = "COMMONFEE")
    private String commonfee;
    //    ブランド使用料
    @Column(name = "BRANDCOST")
    private String brandcost;
    //    その他経費
    @Column(name = "OTHEREXPENSES")
    private String otherexpenses;
    //    仕掛品
    @Column(name = "PROCESS")
    private String process;
    //    その他利益
    @Column(name = "OTHERINCOME")
    private String otherincome;
    //    その他諸経費小計
    @Column(name = "OTHEREXPENTOTAL")
    private String otherexpentotal;
    //    部門共通按分
    @Column(name = "DEPARTMENTCOM")
    private String departmentcom;
    //    合計
    @Column(name = "COSTTOTAL")
    private String costtotal;
    //    営業利益
    @Column(name = "OPERATING")
    private String Operating;
    //    金利
    @Column(name = "INTERESTRATE")
    private String interestrate;
    //    為替
    @Column(name = "EXCHANGES")
    private String exchanges;
    //    営業外損益
    @Column(name = "OPERATINGPROFIT")
    private String operatingprofit;
    //    税引前利益
    @Column(name = "PRETAXPROFIT")
    private String pretaxprofit;
    //    税金引当金
    @Column(name = "TAXALLOWANCE")
    private String taxallowance;
    //    税引後利益
    @Column(name = "POSTTAXBENEFIT")
    private String posttaxbenefit;
    //    営業利益率
    @Column(name = "OPERATINGMARGIN")
    private String operatingmargin;
    //    構外外注(工数)
    @Column(name = "OUTSOURCINGHOURS")
    private String outsourcinghours;
    //    構内外注（名）
    @Column(name = "OUTSOURCINGNAME")
    private String outsourcingname;
    //    社員（名）
    @Column(name = "EMPLOYEENAME")
    private String employeename;
    //    外注（構外∔構内）PJ工数
    @Column(name = "OUTSOURCINGPJHOURS")
    private String outsourcingpjhours;
    //    外注（構外∔構内）稼働工数
    @Column(name = "OUTSOURCING")
    private String outsourcing;
    //    社員PJ工数
    @Column(name = "EMHOURS")
    private String emhours;
    //    社員稼働工数
    @Column(name = "EMPLOYEEUPTIME")
    private String employeeuptime;
    //    外注PJ稼働率
    @Column(name = "EXTERNALPJRATE")
    private String externalpjrate;
    //    外注稼働率
    @Column(name = "EXTERNALINJECTIONRATE")
    private String externalinjectionrate;
    //    社員PJ稼働率
    @Column(name = "MEMBERPJRATE")
    private String memberpjrate;
    //    社員稼働率
    @Column(name = "MEMBERSHIPRATE")
    private String membershiprate;
    //    全員PJ稼働率
    @Column(name = "PJRATEEMPLOYEES")
    private String pjrateemployees;
    //    全員稼働率
    @Column(name = "STAFFINGRATE")
    private String staffingrate;
}
