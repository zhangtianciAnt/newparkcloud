package com.nt.service_BASF;

import com.nt.dao_BASF.Chemicalsds;
import com.nt.dao_BASF.Emergencyplan;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: ChemicalsdsServices
 * @Author: Y
 * @Description: ChemicalsdsServices
 * @Date: 2019/11/18 17:11
 * @Version: 1.0
 */
public interface ChemicalsdsServices {

    //获取应急预案
    List<Chemicalsds> list() throws Exception;

    //创建应急预案
    void insert(Chemicalsds chemicalsds, TokenModel tokenModel) throws Exception;

    //删除应急预案
    void delete(Chemicalsds chemicalsds) throws Exception;

    //获取应急预案详情
    Chemicalsds one(String chemicalsds) throws Exception;

    //更新应急预案数据
    void update(Chemicalsds chemicalsds, TokenModel tokenModel) throws Exception;

}
