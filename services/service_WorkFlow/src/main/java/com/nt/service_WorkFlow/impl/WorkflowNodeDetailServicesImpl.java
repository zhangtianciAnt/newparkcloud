package com.nt.service_WorkFlow.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.nt.service_WorkFlow.WorkflowNodeDetailServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mysql.jdbc.StringUtils;


@Service
@Transactional(rollbackFor=Exception.class)
public class WorkflowNodeDetailServicesImpl implements WorkflowNodeDetailServices {

	private static Logger log = LoggerFactory.getLogger(WorkflowNodeDetailServicesImpl.class);

}
