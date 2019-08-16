package com.nt.service_WorkFlow.impl;

import com.mysql.jdbc.StringUtils;
import com.nt.service_WorkFlow.WorkflowstepServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class WorkflowstepServicesImpl implements WorkflowstepServices {

	private static Logger log = LoggerFactory.getLogger(WorkflowstepServicesImpl.class);

}
