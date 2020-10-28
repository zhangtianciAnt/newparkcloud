package com.nt.service_BASF;

import com.nt.dao_BASF.Existtrainarchives;
import com.nt.dao_BASF.VO.ExisttrainarchivesVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface ExisttrainarchivesServices {

    //获取所有报警单信息
    List<ExisttrainarchivesVo> getAllList() throws Exception;

    //获取所有报警单信息
    ExisttrainarchivesVo getExisttrainarchivesInfoById(String id) throws Exception;

    //创建报警单信息
    void delete(String id, TokenModel tokenModel) throws Exception;

    //更新报警单信息
    void update(Existtrainarchives existtrainarchives, TokenModel tokenModel) throws Exception;

    //execl导入
    List<String> insert(HttpServletRequest request, TokenModel tokenModel) throws Exception;
}
