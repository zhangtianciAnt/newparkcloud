package com.nt.service_AOCHUAN.AOCHUAN1000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.utils.MyMapper;

import java.util.List;

public interface SupplierbaseinforMapper extends MyMapper<Supplierbaseinfor> {
    /**
     * 获取不在项目表中的供应商
     * @return
     */
    public List<Supplierbaseinfor> getSuppliersExceptUnique();
}
