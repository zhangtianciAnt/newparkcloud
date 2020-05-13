package com.nt.dao_AOCHUAN.AOCHUAN1000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierproductrelationVo {
    //供应商或客户的主键
    private String baseinfor_id;

    private List<Supplierproductrelation> supplierproductrelationList;

}
