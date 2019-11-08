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
@Table(name = "shoppingdetailed")
public class ShoppingDetailed extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 千元以下物品购入项目ID
     */
    @Id
    @Column(name = "SHOPPINGDETAILED_ID")
    private String shoppingdetailed_id;

    /**
     * 所属千元以下物品购入事前申请
     */
    @Column(name = "PURCHASEAPPLY_ID")
    private String purchaseapply_id;

    /**
     * 項目
     */
    @Column(name = "PROJECTS")
    private String projects;

    /**
     * 単価
     */
    @Column(name = "UNITPRICE")
    private String unitprice;

    /**
     * 数量
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 金額
     */
    @Column(name = "AMOUNT")
    private String amount;

    /**
     * 備考
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
