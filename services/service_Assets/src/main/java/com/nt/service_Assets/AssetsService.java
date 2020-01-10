package com.nt.service_Assets;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.InventoryResults;
import com.nt.dao_Assets.Vo.AssetsVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AssetsService {

    InventoryResults scanOne(String code, TokenModel tokenModel) throws Exception;

    int scanList(String code, TokenModel tokenModel) throws Exception;

    List<Assets> list(Assets assets) throws Exception;

    void insert(Assets assets, TokenModel tokenModel) throws Exception;

    void insertLosts(AssetsVo assetsVo, TokenModel tokenModel) throws Exception;

    void update(Assets assets, TokenModel tokenModel) throws Exception;

    Assets One(String assetsid) throws Exception;

    List<String> importDate(HttpServletRequest request, TokenModel tokenModel) throws Exception ;
}
