package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Applicationrecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.DocumentExportVo;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.PurchaseExportVo;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.SalesExportVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApplicationrecordMapper extends MyMapper<Applicationrecord> {
    int insertApplicationrecordList(@Param("list") List<Applicationrecord> EnquiryList);

    List<Applicationrecord> getApplicationRecord(@Param("id") String id);

    List<SalesExportVo> selectExportList(@Param("id") String SelectExportList_id);

    List<PurchaseExportVo> purchaseexportList(@Param("id") String purchaseExportList_id);

    List<DocumentExportVo> documentexportList(@Param("id") String documentExportList_id);

    String dictionaryExportList(@Param("id") String dictionary_id);

}
