package com.nt.service_BASF;

import com.nt.dao_BASF.Emailmessage;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface EmailMessageService {
    List<Emailmessage> get(Emailmessage emailmessage) throws Exception;

    int insert(TokenModel tokenModel, Emailmessage emailmessage) throws Exception;

    int update(TokenModel tokenModel, Emailmessage emailmessage) throws Exception;
}
