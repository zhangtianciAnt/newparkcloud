package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Emailmessage;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component(value="EmailmessageMapper")
public interface EmailmessageMapper extends Mapper<Emailmessage> {
}