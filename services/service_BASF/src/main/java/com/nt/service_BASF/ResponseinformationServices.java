package com.nt.service_BASF;

import com.nt.dao_BASF.Responseinformation;
import com.nt.utils.dao.TokenModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: ResponseinformationServices
 * @Author: Newtouch
 * @Description: 应急预案响应信息接口类
 * @Date: 2019/12/19 16:21
 * @Version: 1.0
 */
public interface ResponseinformationServices {

    //获取响应信息列表
    List<Responseinformation> list() throws Exception;

    //获取响应信息
    Responseinformation getone(String responseinformationid) throws Exception;

    //添加响应信息
    void insert(TokenModel tokenModel, Responseinformation responseinformation) throws Exception;

    //编辑响应信息
    void update(TokenModel tokenModel, Responseinformation responseinformation) throws Exception;

    //删除响应信息
    void delete(TokenModel tokenModel, Responseinformation responseinformation) throws Exception;

}
