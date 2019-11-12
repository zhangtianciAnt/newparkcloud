package com.nt.service_BASF;

import com.nt.dao_BASF.Firealarm;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:
 * @Author: Wxz
 * @Description:
 * @Date: 2019/11/12 10:36
 * @Version: 1.0
 */
public interface FirealarmServices {

    //获取报警单列表
    List<Firealarm> list() throws Exception;

    //创建报警单
    void insert(Firealarm firealarm, TokenModel tokenModel)throws Exception;

    //删除报警单
    void delete(Firealarm firealarm)throws Exception;

    //获取报警单详情
    Firealarm one(String firealarmid) throws Exception;

    //更新报警单
    void update(Firealarm firealarm, TokenModel tokenModel)throws Exception;

}
