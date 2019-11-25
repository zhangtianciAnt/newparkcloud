package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.dao_Pfans.PFANS1000.Salesdetails;
import com.nt.dao_Pfans.PFANS1000.Scrapdetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetinformationVo {
    /**
     * 固定资产信息
     */
    private Assetinformation assetinformation;

    /**
     * 报废资产明细
     */
    private List<Salesdetails> salesdetails;

    /**
     * 出售资产明细
     */
    private List<Scrapdetails> scrapdetails;
}
