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
@Table(name = "bpnewsagency")
public class CompanyStatistics extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BPNEWSAGENCY_ID")
    private String bpnewsagency_id;

    @Column(name = "BPCOMPANY")
    private String bpcompany;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR4")
    private String manhour4;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST4")
    private String cost4;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR5")
    private String manhour5;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST5")
    private String cost5;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR6")
    private String manhour6;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST6")
    private String cost6;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR7")
    private String manhour7;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST7")
    private String cost7;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR8")
    private String manhour8;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST8")
    private String cost8;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR9")
    private String manhour9;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST9")
    private String cost9;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR10")
    private String manhour10;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST10")
    private String cost10;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR11")
    private String manhour11;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST11")
    private String cost11;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR12")
    private String manhour12;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST12")
    private String cost12;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR1")
    private String manhour1;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST1")
    private String cost1;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR2")
    private String manhour2;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST2")
    private String cost2;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR3")
    private String manhour3;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST3")
    private String cost3;

    /**
     * 作業工数(合计)
     * 構内工数(合计)
     * 構内人数(合计)
     */
    @Column(name = "TOTALMANHOURS")
    private String totalmanhours;

    /**
     * 外注費用 (合计)
     * 構外工数 (合计)
     */
    @Column(name = "TOTALCOST")
    private String totalcost;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ROWINDEX")
    private String rowindex;
//  region add_qhr_20210907  bp社统计每月加入实际费用列
    /**
     * 4月費用
     */
    @Column(name = "COST4F")
    private Double cost4f;

    /**
     * 5月工数
     */
    @Column(name = "MANHOUR5F")
    private Double manhour5f;

    /**
     * 5月費用
     */
    @Column(name = "COST5F")
    private Double cost5f;

    /**
     * 6月工数
     */
    @Column(name = "MANHOUR6F")
    private Double manhour6f;

    /**
     * 6月費用
     */
    @Column(name = "COST6F")
    private Double cost6f;

    /**
     * 7月工数
     */
    @Column(name = "MANHOUR7F")
    private Double manhour7f;

    /**
     * 7月費用
     */
    @Column(name = "COST7F")
    private Double cost7f;

    /**
     * 8月工数
     */
    @Column(name = "MANHOUR8F")
    private Double manhour8f;

    /**
     * 8月費用
     */
    @Column(name = "COST8F")
    private Double cost8f;

    /**
     * 9月工数
     */
    @Column(name = "MANHOUR9F")
    private Double manhour9f;

    /**
     * 9月費用
     */
    @Column(name = "COST9F")
    private Double cost9f;

    /**
     * 10月工数
     */
    @Column(name = "MANHOUR10F")
    private Double manhour10f;

    /**
     * 10月費用
     */
    @Column(name = "COST10F")
    private Double cost10f;

    /**
     * 11月工数
     */
    @Column(name = "MANHOUR11F")
    private Double manhour11f;

    /**
     * 11月費用
     */
    @Column(name = "COST11F")
    private Double cost11f;

    /**
     * 12月工数
     */
    @Column(name = "MANHOUR12F")
    private Double manhour12f;

    /**
     * 12月費用
     */
    @Column(name = "COST12F")
    private Double cost12f;

    /**
     * 1月工数
     */
    @Column(name = "MANHOUR1F")
    private Double manhour1f;

    /**
     * 1月費用
     */
    @Column(name = "COST1F")
    private Double cost1f;

    /**
     * 2月工数
     */
    @Column(name = "MANHOUR2F")
    private Double manhour2f;

    /**
     * 2月費用
     */
    @Column(name = "COST2F")
    private Double cost2f;

    /**
     * 3月工数
     */
    @Column(name = "MANHOUR3F")
    private Double manhour3f;

    /**
     * 3月費用
     */
    @Column(name = "COST3F")
    private Double cost3f;

    /**
     * 费用合计工数
     */
    @Column(name = "TOTALMANHOURF")
    private Double totalmanhourf;

    /**
     * 费用合计费用
     */
    @Column(name = "TOTALCOSTF")
    private Double totalcostf;
//    endregion add_qhr_20210907  bp社统计每月加入实际费用列
}
