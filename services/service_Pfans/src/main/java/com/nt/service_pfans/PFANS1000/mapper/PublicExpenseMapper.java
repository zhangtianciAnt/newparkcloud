package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface PublicExpenseMapper extends MyMapper<PublicExpense> {
    Integer getInvoiceNo(String reimbursementDate);
//    add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-更改返回值类型-start
    Integer getAporGlNo(String reimbursementDate , @Param("AporGl") String AporGl);
//    add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-更改返回值类型-end
    List<PublicExpense> getpublicelist(@Param("code") String publicexpenseid);

    List<PublicExpense> getLoan(@Param("loan") String loan);

//    PSDCD_PFANS_20210519_BUG_006
    @Select("select publicexpense_id as publicexpenseid,loan from publicexpense where loan <> ''")
    List<PublicExpense> getEmpty();
//    PSDCD_PFANS_20210519_BUG_006
}
