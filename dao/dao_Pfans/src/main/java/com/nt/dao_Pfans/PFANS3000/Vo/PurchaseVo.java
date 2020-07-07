package com.nt.dao_Pfans.PFANS3000.Vo;

import com.nt.dao_Pfans.PFANS3000.Purchase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseVo {

    /**
     * 采购申请List
     */
    private List<Purchase> purchase;

}
