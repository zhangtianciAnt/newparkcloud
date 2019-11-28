package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.dao_Pfans.PFANS1000.Vo.AssetinformationVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
public interface AssetinformationService {

    List<Assetinformation> getAssetinformation(Assetinformation assetinformation) throws Exception;

    public AssetinformationVo selectById(String assetinformationid) throws Exception;

    void updateAssetinformation(AssetinformationVo assetinformationVo, TokenModel tokenModel) throws Exception;

    public void insert(AssetinformationVo assetinformationVo, TokenModel tokenModel)throws Exception;

}
