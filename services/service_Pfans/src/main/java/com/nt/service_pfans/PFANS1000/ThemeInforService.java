package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThemeInforService {

    void insert(ThemeInfor themeinfor, TokenModel tokenModel) throws Exception;

    //获取异常申请列表信息
    List<ThemeInfor> list(ThemeInfor themeinfor) throws Exception;

    void upd(ThemeInfor themeinfor, TokenModel tokenModel) throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    //根据id获取数据
    ThemeInfor One(String themeinforid) throws Exception;
}
