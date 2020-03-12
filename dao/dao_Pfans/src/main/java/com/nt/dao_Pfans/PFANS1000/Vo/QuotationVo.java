package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationVo {
    /**
     *見積書(受託)
     */
    private Quotation quotation;

    /**
     *見積書受託（基本情报1）
     */
    private List<Basicinformation> basicinformation;

    /**
     *見積書受託（人件費）
     */
    private List<Personfee> personfee;

    /**
     *見積書受託（其他費用）
     */
    private List<Othpersonfee> othpersonfee;

    /**
     *見積書受託（成果物）
     */
    private List<Fruit> fruit;

    private List<Contractnumbercount> numbercounts;

}
