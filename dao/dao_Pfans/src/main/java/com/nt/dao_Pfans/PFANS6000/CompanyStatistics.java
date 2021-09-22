package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companystatistics")
public class CompanyStatistics extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COMPANYSTATISTICS_ID")
    private String companystatistics_id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "BPCOMPANYID")
    private String bpcompanyid;

    @Column(name = "BPCOMPANY")
    private String bpcompany;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR4")
    private BigDecimal manhour4;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST4")
    private BigDecimal cost4;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR5")
    private BigDecimal manhour5;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST5")
    private BigDecimal cost5;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR6")
    private BigDecimal manhour6;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST6")
    private BigDecimal cost6;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR7")
    private BigDecimal manhour7;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST7")
    private BigDecimal cost7;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR8")
    private BigDecimal manhour8;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST8")
    private BigDecimal cost8;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR9")
    private BigDecimal manhour9;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST9")
    private BigDecimal cost9;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR10")
    private BigDecimal manhour10;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST10")
    private BigDecimal cost10;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR11")
    private BigDecimal manhour11;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST11")
    private BigDecimal cost11;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR12")
    private BigDecimal manhour12;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST12")
    private BigDecimal cost12;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR1")
    private BigDecimal manhour1;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST1")
    private BigDecimal cost1;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR2")
    private BigDecimal manhour2;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST2")
    private BigDecimal cost2;

    /**
     * 作業工数
     * 構内工数
     * 構内人数
     */
    @Column(name = "MANHOUR3")
    private BigDecimal manhour3;

    /**
     * 外注費用
     * 構外工数
     */
    @Column(name = "COST3")
    private BigDecimal cost3;

    /**
     * 作業工数(合计)
     * 構内工数(合计)
     * 構内人数(合计)
     */
    @Column(name = "TOTALMANHOURS")
    private BigDecimal totalmanhours;

    /**
     * 外注費用 (合计)
     * 構外工数 (合计)
     */
    @Column(name = "TOTALCOST")
    private BigDecimal totalcost;

//    @Column(name = "TYPE")
//    private String type;
//
//    @Column(name = "STATE")
//    private String state;
//
//    @Column(name = "ROWINDEX")
//    private String rowindex;
//  region add_qhr_20210907  bp社统计每月加入实际费用列
    /**
     * 4月工数
     */
    @Column(name = "MANHOUR4F")
    private BigDecimal manhour4f;
    /**
     * 4月費用
     */
    @Column(name = "COST4F")
    private BigDecimal cost4f;

    /**
     * 5月工数
     */
    @Column(name = "MANHOUR5F")
    private BigDecimal manhour5f;

    /**
     * 5月費用
     */
    @Column(name = "COST5F")
    private BigDecimal cost5f;

    /**
     * 6月工数
     */
    @Column(name = "MANHOUR6F")
    private BigDecimal manhour6f;

    /**
     * 6月費用
     */
    @Column(name = "COST6F")
    private BigDecimal cost6f;

    /**
     * 7月工数
     */
    @Column(name = "MANHOUR7F")
    private BigDecimal manhour7f;

    /**
     * 7月費用
     */
    @Column(name = "COST7F")
    private BigDecimal cost7f;

    /**
     * 8月工数
     */
    @Column(name = "MANHOUR8F")
    private BigDecimal manhour8f;

    /**
     * 8月費用
     */
    @Column(name = "COST8F")
    private BigDecimal cost8f;

    /**
     * 9月工数
     */
    @Column(name = "MANHOUR9F")
    private BigDecimal manhour9f;

    /**
     * 9月費用
     */
    @Column(name = "COST9F")
    private BigDecimal cost9f;

    /**
     * 10月工数
     */
    @Column(name = "MANHOUR10F")
    private BigDecimal manhour10f;

    /**
     * 10月費用
     */
    @Column(name = "COST10F")
    private BigDecimal cost10f;

    /**
     * 11月工数
     */
    @Column(name = "MANHOUR11F")
    private BigDecimal manhour11f;

    /**
     * 11月費用
     */
    @Column(name = "COST11F")
    private BigDecimal cost11f;

    /**
     * 12月工数
     */
    @Column(name = "MANHOUR12F")
    private BigDecimal manhour12f;

    /**
     * 12月費用
     */
    @Column(name = "COST12F")
    private BigDecimal cost12f;

    /**
     * 1月工数
     */
    @Column(name = "MANHOUR1F")
    private BigDecimal manhour1f;

    /**
     * 1月費用
     */
    @Column(name = "COST1F")
    private BigDecimal cost1f;

    /**
     * 2月工数
     */
    @Column(name = "MANHOUR2F")
    private BigDecimal manhour2f;

    /**
     * 2月費用
     */
    @Column(name = "COST2F")
    private BigDecimal cost2f;

    /**
     * 3月工数
     */
    @Column(name = "MANHOUR3F")
    private BigDecimal manhour3f;

    /**
     * 3月費用
     */
    @Column(name = "COST3F")
    private BigDecimal cost3f;

    /**
     * 费用合计工数
     */
    @Column(name = "TOTALMANHOURF")
    private BigDecimal totalmanhourf;

    /**
     * 费用合计费用
     */
    @Column(name = "TOTALCOSTF")
    private BigDecimal totalcostf;
//    endregion add_qhr_20210907  bp社统计每月加入实际费用列
}
