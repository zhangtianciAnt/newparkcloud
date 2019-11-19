package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface OtherTwoService {

    void insert(OtherTwo othertwo, TokenModel tokenModel) throws Exception;


    List<OtherTwo> list(OtherTwo othertwo) throws Exception;




}
