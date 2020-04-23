package com.nt.service_AOCHUAN.AOCHUAN5000;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FinCrdlInfoService {

    //获取凭证信息
    List<CredentialInformation> getCrdlInfo(CredentialInformation credentialInformation) throws  Exception;

    //获取分录-辅助项目数据
    List<AccountingRule> getAcctgEntrInfoByCrdl_id(CredentialInformation credentialInformation) throws  Exception;

    //更新
    Boolean updateKisCrdlNo(CredentialInformation credentialInformation, TokenModel tokenModel) throws Exception;

    //删除
    Boolean delete(CrdlInfo crdlInfo, TokenModel tokenModel) throws Exception;

    //新增
    Boolean insert(CrdlInfo crdlInfo, TokenModel tokenModel) throws  Exception;
}
