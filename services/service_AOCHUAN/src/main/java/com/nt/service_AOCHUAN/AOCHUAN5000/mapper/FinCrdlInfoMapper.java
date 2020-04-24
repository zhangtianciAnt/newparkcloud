package com.nt.service_AOCHUAN.AOCHUAN5000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Objects;

public interface FinCrdlInfoMapper extends MyMapper<CredentialInformation> {
    //更新
    void updateKisCrdlNo(@Param("modifyby") String modifyby, @Param("crdlkis_num") String crdlkis_num,@Param("push_status") String push_status, @Param("crdlinfo_id") String crdlinfo_id);

    //删除
    void delCrdlInfoById(@Param("modifyby") String modifyby,@Param("crdlinfo_id") String crdlinfo_id);

    //存在Check
    public List<CredentialInformation> existCheck(@Param("id") String id);

    //唯一性Check
    public List<CredentialInformation> uniqueCheck(@Param("id") String id,@Param("crdl_num") String crdl_num);
}
