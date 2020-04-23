package com.nt.dao_AOCHUAN.AOCHUAN5000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrdlInfo{

    /**
     * 凭证信息主表
     */
    private CredentialInformation credentialInformation;

    /**
     * 分录规则-辅助项目
     */
    private List<AccountingRule> accountingRuleList;
}
