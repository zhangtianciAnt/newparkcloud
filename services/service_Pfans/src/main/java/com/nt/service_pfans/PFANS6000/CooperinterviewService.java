package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Cooperinterview;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CooperinterviewService {

    List<Cooperinterview> getcooperinterview(Cooperinterview cooperinterview) throws Exception;

    public Cooperinterview cooperinterviewApplyOne(String cooperinterview_id) throws Exception;

    public void updatecooperinterviewApply(Cooperinterview cooperinterview, TokenModel tokenModel) throws Exception;

    public void createcooperinterviewApply(Cooperinterview cooperinterview, TokenModel tokenModel) throws Exception;

}
