package com.nt.service_BASF;

import com.nt.dao_BASF.Alarmreceipt;

import java.util.List;


public interface BASF10108Services {

    //获取所有报警单信息
	List<Alarmreceipt> getList(Alarmreceipt alarmreceipt) throws Exception;
}
