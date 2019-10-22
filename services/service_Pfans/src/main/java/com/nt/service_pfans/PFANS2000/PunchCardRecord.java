package com.nt.service_pfans.PFANS2000;

import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PunchCardRecord {
    //查看
    List<PunchCardRecord> getPunchCardRecord(PunchCardRecord punchcardrecord) throws Exception;

    public PunchCardRecord One(String overtimeid) throws Exception;
    //增加
    public void insertPunchCardRecord(PunchCardRecord punchcardrecord, TokenModel tokenModel)throws Exception;
    //修改
    public void updatePunchCardRecord(PunchCardRecord punchcardrecord, TokenModel tokenModel)throws Exception;

}
