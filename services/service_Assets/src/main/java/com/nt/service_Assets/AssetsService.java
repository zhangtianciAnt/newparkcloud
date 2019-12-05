package com.nt.service_Assets;

import com.nt.dao_Assets.Assets;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AssetsService {

    List<Assets> list(Assets assets) throws Exception;

    void insert(Assets assets, TokenModel tokenModel) throws Exception;

    void update(Assets assets, TokenModel tokenModel) throws Exception;

    Assets One(String assetsid) throws Exception;


}
