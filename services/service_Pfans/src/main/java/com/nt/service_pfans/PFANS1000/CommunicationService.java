package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Communication;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CommunicationService {

    //查看
    List<Communication> getCommunication(Communication communication)throws Exception;

    public Communication One(String communication_id)throws  Exception;

    //创建
    public void insert(Communication communication, TokenModel tokenModel)throws  Exception;

    //修改
    public void updateCommunication(Communication communication,TokenModel tokenModel)throws  Exception;

}
