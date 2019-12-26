package com.nt.service_BASF;

import com.nt.dao_BASF.SendEmail;
import com.nt.utils.dao.TokenModel;

public interface SendEmailServices {

    Boolean sendmail(TokenModel tokenModel, SendEmail sendemail) throws Exception;


}
