package com.nt.service_Org;

import com.nt.dao_Org.Earlyvacation;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface EarlyvacationService {

    public void insert(Earlyvacation earlyvacation, TokenModel tokenModel)throws  Exception;

}
