package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface PublicExpenseMapper extends MyMapper<PublicExpense> {
    Integer getInvoiceNo(String reimbursementDate);
    //    add-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-更改返回值类型-start
    String getAporGlNo(String reimbursementDate , @Param("AporGl") String AporGl);
    //    add-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-更改返回值类型-end
    List<PublicExpense> getpublicelist(@Param("code") String publicexpenseid);

    List<PublicExpense> getLoan(@Param("loan") String loan);
}
