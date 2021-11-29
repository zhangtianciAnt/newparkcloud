package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.ExpatriatesinforDetail;
import com.nt.utils.dao.TableDataInfo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ExpatriatesinforService {

    List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception;

    //增加分页 ztc fr
    List<Expatriatesinfor> getexpatrFliter(Expatriatesinfor expatriatesinfor) throws Exception;
    //增加分页 ztc to

//    dialog优化分页 ztc fr
    TableDataInfo getforSysDiaLog(int currentPage, int pageSize) throws Exception;
    //    dialog优化分页 ztc to
    public Expatriatesinfor getexpatriatesinforApplyOne(String expatriatesinfor_id) throws Exception;

    List<ExpatriatesinforDetail> getGroupexpDetail(String expatriatesinfor_id) throws Exception;

    public void updateinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception;

    public void updateexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception;


    public void createexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception;


    List<String> expimport(HttpServletRequest request, TokenModel tokenModel) throws Exception;

    void crAccount(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel ) throws Exception;

    void crAccount2(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel ) throws Exception;
}
