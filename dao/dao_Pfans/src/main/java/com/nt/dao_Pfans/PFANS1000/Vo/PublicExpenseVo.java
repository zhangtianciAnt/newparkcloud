package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicExpenseVo {

    /*
     * 分录传票
     * */
    private PublicExpense publicexpense;

    /*
     * 交通明细*/
    private List<TrafficDetails> trafficdetails;

    /*采购明细
     * */
    private List<PurchaseDetails> purchasedetails;
    /*
     * 其他费用明细
     * */
    private List<OtherDetails> otherdetails;
    /*
     * 发票信息
     * */
    private List<Invoice> invoice;
}
