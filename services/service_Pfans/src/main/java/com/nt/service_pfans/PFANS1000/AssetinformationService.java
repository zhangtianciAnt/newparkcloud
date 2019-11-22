package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
public interface AssetinformationService {

    //查看
    List<Assetinformation> getAssetinformation(Assetinformation assetinformation) throws Exception;

    public Assetinformation selectById(String assetinformationid) throws Exception;

    //修改
    public void updateAssetinformation(Assetinformation assetinformation, TokenModel tokenModel) throws Exception;

    //创建
    public void insert(Assetinformation assetinformation, TokenModel tokenModel)throws Exception;

    //计算金额
    public List<Assetinformation> getAssetinformationList(Assetinformation assetinformation, HttpServletRequest request) throws Exception;

}
