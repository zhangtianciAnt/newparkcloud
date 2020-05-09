package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.ThemeInfo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThemeInfoService {

    //获取异常申请列表信息
    List<ThemeInfo> list(ThemeInfo themeInfo) throws Exception;

    //根据id获取数据
    ThemeInfo One(String themeInfoId) throws Exception;

    void insert(ThemeInfo themeInfo, TokenModel tokenModel) throws Exception;

    void upd(ThemeInfo themeInfo, TokenModel tokenModel) throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

}
