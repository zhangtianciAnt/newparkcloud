package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface WagesService {

    List<Wages> select(TokenModel tokenModel);
}


