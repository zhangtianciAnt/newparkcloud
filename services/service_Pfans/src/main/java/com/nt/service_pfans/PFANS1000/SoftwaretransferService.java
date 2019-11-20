package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SoftwaretransferService {

    //查看
    List<Softwaretransfer> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception;

    public Softwaretransfer One(String softwaretransferid) throws Exception;

    //修改
    public void updateSoftwaretransfer(Softwaretransfer softwaretransfer, TokenModel tokenModel) throws Exception;

    //创建
    public void insert(Softwaretransfer softwaretransfer, TokenModel tokenModel)throws Exception;

    //计算金额
    public List<Softwaretransfer> getSoftwaretransferList(Softwaretransfer softwaretransfer, HttpServletRequest request) throws Exception;

}
