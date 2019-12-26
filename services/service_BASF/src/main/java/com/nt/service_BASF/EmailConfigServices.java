package com.nt.service_BASF;

import com.nt.dao_BASF.EmailConfig;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface EmailConfigServices {
    List<EmailConfig> get() throws Exception;
}
