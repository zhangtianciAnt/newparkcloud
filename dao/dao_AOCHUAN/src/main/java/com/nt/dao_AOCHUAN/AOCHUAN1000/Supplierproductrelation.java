package com.nt.dao_AOCHUAN.AOCHUAN1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "supplierproductrelation")
public class Supplierproductrelation extends BaseModel {
    @Id
    private String supplierproductrelation_id;

    private String supplierbaseinfor_id;

    private String products_id;
}
