package com.nt.service_AOCHUAN.AOCHUAN1000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SupplierbaseinforMapper extends MyMapper<Supplierbaseinfor> {
    /**
     * 获取不在项目表中的供应商
     *
     * @return
     */
    public List<Supplierbaseinfor> getSuppliersExceptUnique();

    @Select("select SUPPLIERBASEINFOR_ID,suppliernamecn,industryinvolved,address1,linkman,mobilephone from supplierbaseinfor  ORDER BY createon DESC")
    List<Supplierbaseinfor> allSelect();
    // add-ws-10/13-禅道任务459
    @Select("select SUPPLIERBASEINFOR_ID,suppliernamecn,industryinvolved,address1,linkman,mobilephone from supplierbaseinfor where date_format(supplierbaseinfor.CREATEON, '%Y%m') = #{createon} order by createon desc")
    public List<Supplierbaseinfor>  selectlist(@Param("createon") String createon);
    // add-ws-10/13-禅道任务459
}
