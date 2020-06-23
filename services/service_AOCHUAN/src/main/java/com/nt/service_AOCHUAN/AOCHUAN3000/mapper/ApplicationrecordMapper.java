package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Applicationrecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Receivablesrecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Saledetails;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ExportVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApplicationrecordMapper extends MyMapper<Applicationrecord> {
    int insertApplicationrecordList(@Param("list") List<Applicationrecord> EnquiryList);

    List<Applicationrecord> getApplicationRecord(@Param("id") String id);

    List<ExportVo> selectExportList(@Param("id") String ExportList_id);
}
