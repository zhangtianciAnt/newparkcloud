package com.nt.service_BASF;

import com.nt.dao_BASF.Trainmail;
import com.nt.dao_BASF.VO.TrainmailVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;


public interface TrainmailServices {

    //获取培训邮件设置
    List<TrainmailVo> getAllList() throws Exception;

    //获取单条培训邮件设置
    TrainmailVo getone(String trainmailid) throws Exception;

    //更新培训邮件设置
    void update(Trainmail trainmail, TokenModel tokenModel) throws Exception;

    //新建培训邮件设置
    void insert(Trainmail trainmail, TokenModel tokenModel) throws Exception;
}
