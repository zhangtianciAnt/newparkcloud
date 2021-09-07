package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "bpcompanycost")
public class BpCompanyCost extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "BPCOMPANYCOST_ID")
    private String bpcompanycost_id;

    /**
     * 年度
     */
    @Column(name = "YEAR")
    private String year;

    /**
     * GROUP_ID
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * BP会社名
     */
    @Column(name = "BPCOMPANY")
    private String bpcompany;

    /**
     * 4月工数
     */
    @Column(name = "MANHOUR4")
    private Double manhour4;

    /**
     * 4月費用
     */
    @Column(name = "COST4")
    private Double cost4;

    /**
     * 5月工数
     */
    @Column(name = "MANHOUR5")
    private Double manhour5;

    /**
     * 5月費用
     */
    @Column(name = "COST5")
    private Double cost5;

    /**
     * 6月工数
     */
    @Column(name = "MANHOUR6")
    private Double manhour6;

    /**
     * 6月費用
     */
    @Column(name = "COST6")
    private Double cost6;

    /**
     * 7月工数
     */
    @Column(name = "MANHOUR7")
    private Double manhour7;

    /**
     * 7月費用
     */
    @Column(name = "COST7")
    private Double cost7;

    /**
     * 8月工数
     */
    @Column(name = "MANHOUR8")
    private Double manhour8;

    /**
     * 8月費用
     */
    @Column(name = "COST8")
    private Double cost8;

    /**
     * 9月工数
     */
    @Column(name = "MANHOUR9")
    private Double manhour9;

    /**
     * 9月費用
     */
    @Column(name = "COST9")
    private Double cost9;

    /**
     * 10月工数
     */
    @Column(name = "MANHOUR10")
    private Double manhour10;

    /**
     * 10月費用
     */
    @Column(name = "COST10")
    private Double cost10;

    /**
     * 11月工数
     */
    @Column(name = "MANHOUR11")
    private Double manhour11;

    /**
     * 11月費用
     */
    @Column(name = "COST11")
    private Double cost11;

    /**
     * 12月工数
     */
    @Column(name = "MANHOUR12")
    private Double manhour12;

    /**
     * 12月費用
     */
    @Column(name = "COST12")
    private Double cost12;

    /**
     * 1月工数
     */
    @Column(name = "MANHOUR1")
    private Double manhour1;

    /**
     * 1月費用
     */
    @Column(name = "COST1")
    private Double cost1;

    /**
     * 2月工数
     */
    @Column(name = "MANHOUR2")
    private Double manhour2;

    /**
     * 2月費用
     */
    @Column(name = "COST2")
    private Double cost2;

    /**
     * 3月工数
     */
    @Column(name = "MANHOUR3")
    private Double manhour3;

    /**
     * 3月費用
     */
    @Column(name = "COST3")
    private Double cost3;

    /**
     * 费用合计工数
     */
    @Column(name = "TOTALMANHOUR")
    private Double totalmanhour;

    /**
     * 费用合计费用
     */
    @Column(name = "TOTALCOST")
    private Double totalcost;

}
