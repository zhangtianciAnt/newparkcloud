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
     * BP会社ID
     */
    @Column(name = "BPCOMPANYID")
    private String bpcompanyid;

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
     * 4月工数
     */
    @Column(name = "MANHOUR4F")
    private Double manhour4f;

    /**
     * 4月費用
     */
    @Column(name = "COST4")
    private Double cost4;

    /**
     * 4月費用
     */
    @Column(name = "COST4F")
    private Double cost4f;

    /**
     * 5月工数
     */
    @Column(name = "MANHOUR5")
    private Double manhour5;

    /**
     * 5月工数
     */
    @Column(name = "MANHOUR5F")
    private Double manhour5f;

    /**
     * 5月費用
     */
    @Column(name = "COST5")
    private Double cost5;

    /**
     * 5月費用
     */
    @Column(name = "COST5F")
    private Double cost5f;

    /**
     * 6月工数
     */
    @Column(name = "MANHOUR6")
    private Double manhour6;

    /**
     * 6月工数
     */
    @Column(name = "MANHOUR6F")
    private Double manhour6f;

    /**
     * 6月費用
     */
    @Column(name = "COST6")
    private Double cost6;

    /**
     * 6月費用
     */
    @Column(name = "COST6F")
    private Double cost6f;

    /**
     * 7月工数
     */
    @Column(name = "MANHOUR7")
    private Double manhour7;

    /**
     * 7月工数
     */
    @Column(name = "MANHOUR7F")
    private Double manhour7f;

    /**
     * 7月費用
     */
    @Column(name = "COST7")
    private Double cost7;

    /**
     * 7月費用
     */
    @Column(name = "COST7F")
    private Double cost7f;

    /**
     * 8月工数
     */
    @Column(name = "MANHOUR8")
    private Double manhour8;

    /**
     * 8月工数
     */
    @Column(name = "MANHOUR8F")
    private Double manhour8f;

    /**
     * 8月費用
     */
    @Column(name = "COST8")
    private Double cost8;

    /**
     * 8月費用
     */
    @Column(name = "COST8F")
    private Double cost8f;

    /**
     * 9月工数
     */
    @Column(name = "MANHOUR9")
    private Double manhour9;

    /**
     * 9月工数
     */
    @Column(name = "MANHOUR9F")
    private Double manhour9f;

    /**
     * 9月費用
     */
    @Column(name = "COST9")
    private Double cost9;

    /**
     * 9月費用
     */
    @Column(name = "COST9F")
    private Double cost9f;

    /**
     * 10月工数
     */
    @Column(name = "MANHOUR10")
    private Double manhour10;

    /**
     * 10月工数
     */
    @Column(name = "MANHOUR10F")
    private Double manhour10f;

    /**
     * 10月費用
     */
    @Column(name = "COST10")
    private Double cost10;

    /**
     * 10月費用
     */
    @Column(name = "COST10F")
    private Double cost10f;

    /**
     * 11月工数
     */
    @Column(name = "MANHOUR11")
    private Double manhour11;

    /**
     * 11月工数
     */
    @Column(name = "MANHOUR11F")
    private Double manhour11f;

    /**
     * 11月費用
     */
    @Column(name = "COST11")
    private Double cost11;

    /**
     * 11月費用
     */
    @Column(name = "COST11F")
    private Double cost11f;

    /**
     * 12月工数
     */
    @Column(name = "MANHOUR12")
    private Double manhour12;

    /**
     * 12月工数
     */
    @Column(name = "MANHOUR12F")
    private Double manhour12f;

    /**
     * 12月費用
     */
    @Column(name = "COST12")
    private Double cost12;

    /**
     * 12月費用
     */
    @Column(name = "COST12F")
    private Double cost12f;

    /**
     * 1月工数
     */
    @Column(name = "MANHOUR1")
    private Double manhour1;

    /**
     * 1月工数
     */
    @Column(name = "MANHOUR1F")
    private Double manhour1f;

    /**
     * 1月費用
     */
    @Column(name = "COST1")
    private Double cost1;

    /**
     * 1月費用
     */
    @Column(name = "COST1F")
    private Double cost1f;

    /**
     * 2月工数
     */
    @Column(name = "MANHOUR2")
    private Double manhour2;

    /**
     * 2月工数
     */
    @Column(name = "MANHOUR2F")
    private Double manhour2f;

    /**
     * 2月費用
     */
    @Column(name = "COST2")
    private Double cost2;

    /**
     * 2月費用
     */
    @Column(name = "COST2F")
    private Double cost2f;

    /**
     * 3月工数
     */
    @Column(name = "MANHOUR3")
    private Double manhour3;

    /**
     * 3月工数
     */
    @Column(name = "MANHOUR3F")
    private Double manhour3f;

    /**
     * 3月費用
     */
    @Column(name = "COST3")
    private Double cost3;

    /**
     * 3月費用
     */
    @Column(name = "COST3F")
    private Double cost3f;

    /**
     * 费用合计工数
     */
    @Column(name = "TOTALMANHOUR")
    private Double totalmanhour;

    /**
     * 费用合计工数
     */
    @Column(name = "TOTALMANHOURF")
    private Double totalmanhourf;

    /**
     * 费用合计费用
     */
    @Column(name = "TOTALCOST")
    private Double totalcost;

    /**
     * 费用合计费用
     */
    @Column(name = "TOTALCOSTF")
    private Double totalcostf;

}
