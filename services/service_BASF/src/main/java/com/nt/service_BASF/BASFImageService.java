package com.nt.service_BASF;

import com.nt.dao_BASF.BASFImage;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASFImageService
 * @Author: LXY
 * @Description: 首页超链接模块接口
 * @Date: 2019/11/22
 * @Version: 1.0
 */
public interface BASFImageService {
    //list
    List<BASFImage> get(BASFImage image) throws Exception;

    void insert(TokenModel tokenModel, BASFImage image) throws Exception;

    void update(TokenModel tokenModel, BASFImage image) throws Exception;

    void del(TokenModel tokenModel, BASFImage image) throws Exception;
}
