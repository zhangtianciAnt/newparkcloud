package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coststatistics")
public class Coststatistics extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 費用集計
     */
    @Id
    @Column(name = "COSTSTATISTICS_ID")
    private String coststatistics_id;

    /**
     * BP名前
     */
    @Column(name = "BPNAME")
    private String bpname;

    /**
     * BP会社名
     */
    @Column(name = "BPCOMPANY")
    private String bpcompany;

    /**
     * 単価
     */
    @Column(name = "UNITPRICE")
    private Double unitprice;

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
     * 支払月次6月
     */
    @Column(name = "SUPPORT6")
    private String support6;

    /**
     * 合計工数6月
     */
    @Column(name = "TOTALMANHOURS6")
    private String totalmanhours6;

    /**
     * 合計費用6月
     */
    @Column(name = "TOTALCOST6")
    private String totalcost6;

    /**
     * 経費6月
     */
    @Column(name = "EXPENSE6")
    private String expense6;

    /**
     * 契約金額6月
     */
    @Column(name = "CONTRACT6")
    private String contract6;

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
     * 支払月次9月
     */
    @Column(name = "SUPPORT9")
    private String support9;

    /**
     * 合計工数9月
     */
    @Column(name = "TOTALMANHOURS9")
    private String totalmanhours9;

    /**
     * 合計費用9月
     */
    @Column(name = "TOTALCOST9")
    private String totalcost9;

    /**
     * 経費9月
     */
    @Column(name = "EXPENSE9")
    private String expense9;

    /**
     * 契約金額9月
     */
    @Column(name = "CONTRACT9")
    private String contract9;

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
     * 支払月次12月
     */
    @Column(name = "SUPPORT12")
    private String support12;

    /**
     * 合計工数12月
     */
    @Column(name = "TOTALMANHOURS12")
    private String totalmanhours12;

    /**
     * 合計費用12月
     */
    @Column(name = "TOTALCOST12")
    private String totalcost12;

    /**
     * 経費12月
     */
    @Column(name = "EXPENSE12")
    private String expense12;

    /**
     * 契約金額12月
     */
    @Column(name = "CONTRACT12")
    private String contract12;

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
     * 支払月次3月
     */
    @Column(name = "SUPPORT3")
    private String support3;

    /**
     * 合計工数3月
     */
    @Column(name = "TOTALMANHOURS3")
    private String totalmanhours3;

    /**
     * 合計費用3月
     */
    @Column(name = "TOTALCOST3")
    private String totalcost3;

    /**
     * 契約金額3月
     */
    @Column(name = "CONTRACT3")
    private String contract3;

    /**
     * 経費3月
     */
    @Column(name = "EXPENSE3")
    private String expense3;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;


}
