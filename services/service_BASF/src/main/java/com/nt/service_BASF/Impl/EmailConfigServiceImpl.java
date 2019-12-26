package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.EmailConfig;
import com.nt.service_BASF.EmailConfigServices;
import com.nt.service_BASF.mapper.EmailConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmailConfigServiceImpl implements EmailConfigServices {

    @Autowired
    private EmailConfigMapper emailConfigMapper;

    @Override
    public List<EmailConfig> get() throws Exception {
        EmailConfig emailConfig = new EmailConfig();
        return emailConfigMapper.select(emailConfig);
    }
}
