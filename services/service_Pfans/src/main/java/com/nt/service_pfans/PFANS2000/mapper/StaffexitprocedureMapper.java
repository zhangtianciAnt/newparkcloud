package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface StaffexitprocedureMapper extends MyMapper<Staffexitprocedure> {


    @Select("UPDATE staffexitproce SET `STATUS` = '1' where STAFFEXITPROCEDURE_ID = {#staff_id}")
    void upStaffproe(@Param("staff_id") String staff_id);

    @Select("UPDATE todonotice SET `STATUS` = '1' WHERE DATAID IN " +
            "( SELECT STAFFEXITPROCE_ID FROM `staffexitproce` WHERE STAFFEXITPROCEDURE_ID = {#staff_id})")
    void upTodoNo(@Param("staff_id") String staff_id);
}
