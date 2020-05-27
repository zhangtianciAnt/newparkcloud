package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Reg_Record;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Reg_RecordMapper extends MyMapper<Reg_Record> {

    //根据注册id查询
    public List<Reg_Record> selectByRegId(@Param("id") String id);
    //存在check
    public Integer existCheck(@Param("id") String id);
    //根据注册表id删除
    void deleteByRegId(@Param("id") String id, @Param("modifyby") String modifyby);
    //根据主键删除
    void deleteByRecordId(@Param("id") String id, @Param("modifyby") String modifyby);
}
