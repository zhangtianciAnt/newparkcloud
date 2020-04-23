package com.nt.service_AOCHUAN.AOCHUAN5000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinAcctgRulMapper extends MyMapper<AccountingRule> {

    //获取分录规则+辅助项目
    public List<AccountingRule> getAcctgEntrInfoByCrdl_id(@Param("crdlinfo_id") String crdlinfo_id);

    //删除
    void delAcctgRulByFid(@Param("modifyby") String modifyby,@Param("crdlinfo_fid") String crdlinfo_fid);
    void delAuxAcctgByFid(@Param("modifyby") String modifyby,@Param("acctgrul_fid") String acctgrul_fid);

    //存在Check
    int existCheckAcc(@Param("id") String id);
    int existCheckAux(@Param("id") String id);
}
