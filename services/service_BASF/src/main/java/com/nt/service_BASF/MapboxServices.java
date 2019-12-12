package com.nt.service_BASF;

import com.nt.dao_BASF.Mapbox;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF20201Services
 * @Author: SUN
 * @Description: BASF大屏接警指挥
 * @Date: 2019/11/14 16:09
 * @Version: 1.0
 */
public interface MapboxServices {
    //获取
    Mapbox get(Mapbox mapbox) throws Exception;
}
