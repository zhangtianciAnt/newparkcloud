package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.PurchaseApply;
import com.nt.dao_Pfans.PFANS1000.ShoppingDetailed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseApplyVo {
    /**
     * 千元以下物品购入事前申请
     */
    private PurchaseApply purchaseApply;

    /**
     * 千元以下物品购入项目
     */
    private List<ShoppingDetailed> shoppingDetailed;
}
