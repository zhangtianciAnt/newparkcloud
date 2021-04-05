package com.nt.service_pfans.PFANS5000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementConfirmVo;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementVo2;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_pfans.PFANS5000.LogManagementService;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.LogManagementMapper;
import com.nt.service_pfans.PFANS5000.mapper.PersonalProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectsystemMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.MsgConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class LogManagementServiceImpl implements LogManagementService {

    @Override
    public void delete(LogManagement logmanagement) throws Exception {
        logmanagementmapper.delete(logmanagement);
    }

    @Autowired
    private LogManagementMapper logmanagementmapper;

    @Autowired
    private ProjectsystemMapper projectsystemMapper;

    @Autowired
    private PersonalProjectsMapper personalprojectsMapper;

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Override
    public void insert(LogManagement logmanagement, TokenModel tokenModel) throws Exception {
        logmanagement.preInsert(tokenModel);
        logmanagement.setLogmanagement_id(UUID.randomUUID().toString());
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(logmanagement.getCreateby()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null && customerInfo.getUserinfo() != null) {
            logmanagement.setJobnumber(customerInfo.getUserinfo().getJobnumber());
            if (logmanagement.getProject_id().equals("PP024001") || logmanagement.getProject_id().isEmpty()) {
                logmanagement.setGroup_id(customerInfo.getUserinfo().getGroupid());
            }
        }
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        expatriatesinfor.setAccount(logmanagement.getCreateby());
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
        if (expatriatesinforList.size() > 0) {
            if (logmanagement.getProject_id().equals("PP024001") || logmanagement.getProject_id().isEmpty()) {
                logmanagement.setGroup_id(expatriatesinforList.get(0).getGroup_id());
            }
        }
        logmanagement.setConfirmstatus("0");
        logmanagementmapper.insert(logmanagement);
    }

    //add-ws-01/05-优化接口
    @Override
    public List<LogManagement> sumlogdate(LogManagement logmanagement) throws Exception {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        String createby = logmanagement.getCreateby();
        String logdate = sf1.format(logmanagement.getLog_date());
        List<LogManagement> list = logmanagementmapper.selectsum(createby, logdate);
        BigDecimal sum = new BigDecimal("0");
        BigDecimal sum1 = new BigDecimal("0");
        for (LogManagement log : list) {
            sum = new BigDecimal(log.getTime_start());
            sum1 = sum1.add(sum);
        }
        LogManagement logmanag = new LogManagement();
        logmanag.setTime_start(String.valueOf(sum1));
        List<LogManagement> list2 = new ArrayList<>();
        list2.add(logmanag);
        return list2;
    }

    //add-ws-01/05-优化接口
    @Override
    public List<LogManagement> getDataList(LogManagement logmanagement) throws Exception {

        return logmanagementmapper.select(logmanagement);
    }

    //add ccm 1118 日志优化
    @Override
    public List<LogManagement> getDataListByLog_date(LogManagement logmanagement) throws Exception {
        String log_date = DateUtil.format(logmanagement.getLog_date(), "yyyy-MM");
        return logmanagementmapper.getDataListByLog_date(logmanagement.getOwners(), log_date);
    }
    //add ccm 1118 日志优化

    //add_fjl_0716_添加PL权限的人查看日志一览  start
    @Override
    public List<LogManagement> getDataListPL(TokenModel tokenModel) throws Exception {
        String owner = tokenModel.getUserId();
        List<LogManagement> list2 = logmanagementmapper.getListPLlogman(owner);
        return list2;
    }

    //add_fjl_0716_添加PL权限的人查看日志一览  end
    @Override
    public List<LogManagement> getLogDataList(LogManagement logmanagement, String startDate, String endDate) throws Exception {
        return logmanagementmapper.selectByDate(logmanagement.getOwners(), startDate, endDate);
    }

    @Override
    public List<LogManagement> getCheckList(LogManagement logmanagement) throws Exception {
        LogManagement logmanage = new LogManagement();
        logmanage.setCreateby(logmanagement.getCreateby());
        return logmanagementmapper.select(logmanage);
    }

    @Override
    public List<Projectsystem> CheckList(Projectsystem projectsystem, TokenModel tokenModel) throws Exception {
        projectsystem.setName(tokenModel.getUserId());
        List<Projectsystem> projectsystemlist = projectsystemMapper.select(projectsystem);
        return projectsystemlist;
    }

    @Override
    public List<LogmanagementConfirmVo> getProjectList(String StrFlg, String strDate, TokenModel tokenModel) throws Exception {
        //获取查看人的groupid
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        String Groupid = "";
        if (customerInfo != null) {
            Groupid = customerInfo.getUserinfo().getCenterid();
        }
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        expatriatesinfor.setGroup_id(Groupid);
        //groupid下所有外协人员
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
        if (expatriatesinforList.size() > 0) {
            List<String> ownerList = new ArrayList<String>();
            for (Expatriatesinfor ex : expatriatesinforList) {
                //ownerList.add(ex.getAccount());
                tokenModel.getOwnerList().add(ex.getAccount());
            }
        }
        List<LogmanagementConfirmVo> Result = new ArrayList<LogmanagementConfirmVo>();
        if (StrFlg.equals("1")) {
            Result = logmanagementmapper.getProjectList(tokenModel.getOwnerList(), tokenModel.getUserId());
        } else {
            Result = logmanagementmapper.getunProjectList(strDate, tokenModel.getOwnerList());
        }
        //add-ws-5/25-No.48
        for (LogmanagementConfirmVo listvo : Result) {
            String str_format = "";
            DecimalFormat df = new DecimalFormat("#.00");
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(listvo.getUnconfirm())) {
                BigDecimal bd = new BigDecimal(listvo.getUnconfirm());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                listvo.setUnconfirm(str_format);
            } else {
                listvo.setUnconfirm("0.00");
            }
        }
        //add-ws-5/25-No.48
        return Result;
    }

    @Override
    public List<LogmanagementVo2> getListcheck(LogManagement logmanagement, TokenModel tokenModel) throws Exception {
        List<LogmanagementVo2> Result = new ArrayList<LogmanagementVo2>();
        List<LogmanagementVo2> checkList = logmanagementmapper.getcheckList();
        if (checkList != null && checkList.size() > 0) {
            Result = logmanagementmapper.getcheckList3();
        } else {
            Result = logmanagementmapper.getcheckList2();
        }
//add-ws-No.179
        String project_id = logmanagement.getProject_id();
        Result = Result.stream()
                .filter(item -> (item.getCOMPANYPROJECTS_ID().equals(project_id)))
                .collect(Collectors.toList());
        //add-ws-No.179

        Result = Result.stream().sorted(Comparator.comparing(LogmanagementVo2::getClaimdatetime).reversed()).collect(Collectors.toList());


        return Result;
    }

    @Override
    public List<LogmanagementStatusVo> getTimestart(String project_id, String starttime, String endtime) throws Exception {
        return logmanagementmapper.getTimestart(project_id, starttime, endtime);
    }

    @Override
    public List<LogmanagementStatusVo> getGroupTimestart(List<String> createby, String starttime, String endtime) throws Exception {
        return logmanagementmapper.getGroupTimestart(createby, starttime, endtime);
    }

    @Override
    public void updateTimestart(LogmanagementStatusVo LogmanagementStatusVo, TokenModel tokenModel) throws Exception {
        List<LogManagement> loglist = LogmanagementStatusVo.getLogmanagement();
        String confirmstatus = LogmanagementStatusVo.getConfirmstatus();
        String starttime = LogmanagementStatusVo.getStarttime();
        String endtime = LogmanagementStatusVo.getEndtime();
        if (loglist.size() > 0) {
            for (int i = 0; i < loglist.size(); i++) {
                String createby = loglist.get(i).getCreateby();
                logmanagementmapper.updateTimestart(createby, confirmstatus, starttime, endtime);

                //拒绝之后发代办
                if (confirmstatus.equals("2")) {
                    ToDoNotice toDoNotice = new ToDoNotice();
                    toDoNotice.setTitle("您有一个项目日志填写被拒绝！");
                    toDoNotice.setType("2");//消息
                    toDoNotice.setInitiator(tokenModel.getUserId());
                    toDoNotice.setUrl("/PFANS5008View");
                    toDoNotice.preInsert(tokenModel);
                    toDoNotice.setOwner(createby);
                    toDoNoticeService.save(toDoNotice);
                }
            }
        }
    }

    @Override
    public List<LogManagement> gettlist() throws Exception {
        return logmanagementmapper.gettlist();
    }

    @Override
    public void update(LogManagement logmanagement, TokenModel tokenModel) throws Exception {
        String Confirmstatus = logmanagement.getConfirmstatus();
        logmanagement.setConfirmstatus(AuthConstants.DEL_FLAG_NORMAL);
        logmanagement.preUpdate(tokenModel);
        logmanagementmapper.updateByPrimaryKeySelective(logmanagement);
    }

    @Override
    public LogManagement One(String logmanagement_id) throws Exception {
        LogManagement log = logmanagementmapper.selectByPrimaryKey(logmanagement_id);
        return log;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("账号");
            model.add("姓名");
            model.add("项目");
            model.add("日志日期");
            model.add("时长");
            model.add("工作备注");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }

            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                LogManagement logmanagement = new LogManagement();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                    if (value.size() > 3) {
                        String date = value.get(3).toString();
                        String date1 = value.get(3).toString();
                        date = date.substring(5, 7);
                        date1 = date1.substring(8, 10);
                        if (Integer.parseInt(date1) > 31) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的日期格式错误，请输入正确的日子，导入失败");
                            continue;
                        }
                        if (Integer.parseInt(date) > 12) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的日期格式错误，请输入正确的月份，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 6) {
                        if (value.get(6).toString().length() > 50) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的工作备注长度超出范围，请输入长度为50位之内的工作备注，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String jobnumber = value.get(0).toString();
                    query.addCriteria(Criteria.where("userinfo.adfield").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        logmanagement.setCreateby(customerInfo.getUserid());
                        logmanagement.setOwner(customerInfo.getUserid());
                        logmanagement.setJobnumber(value.get(0).toString());
                    }
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的账号字段没有找到，请输入正确的账号，导入失败");
                        continue;
                    }
                    //确认状态
                    logmanagement.setConfirmstatus("0");
                    logmanagement.preInsert(tokenModel);
                    PersonalProjects personalprojects = new PersonalProjects();
                    List<PersonalProjects> personalprojectsList = personalprojectsMapper.select(personalprojects);
                    for (PersonalProjects projects : personalprojectsList) {
                        if (projects.getProject_name().equals(value.get(2).toString())) {
                            logmanagement.setProject_id(projects.getProject_id());
                        }
                    }
                    logmanagement.setLog_date(sf1.parse(value.get(3).toString()));
                    logmanagement.setTime_start(value.get(4).toString());
                    logmanagement.setWork_memo(value.get(5).toString());
                }
                logmanagement.setLogmanagement_id(UUID.randomUUID().toString());
                logmanagementmapper.insert(logmanagement);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String downloadUserModel() throws LogicalException {
        try {
            String repoId = "";
            String url = "http://FILESYSTEM/userfile/getFolderList";
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ApiResult> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    ApiResult.class);
            ArrayList folderList = (ArrayList) response.getBody().getData();

            for (int i = 0; i < folderList.size(); i++) {
                String res = folderList.get(i).toString();
                String[] str = res.split(",");
                for (int j = 2; j < 3; j++) {
                    if (str[j].trim().equals("name=模板")) {
                        String str1 = res.substring(res.indexOf("type=repo, id="), res.indexOf("modifier_name"));
                        String id = str1.substring(str1.indexOf("id=") + 3);
                        repoId = id.replace(",", "").trim();
                        break;
                    }
                }
            }
            if (repoId.equals("")) {
                throw new LogicalException("用户模板不存在，请通知管理员");
            }
            List<LogManagement> logmanagementList = new ArrayList<LogManagement>();
            LogManagement logmanagement = new LogManagement();
            logmanagement.setProject_id(repoId + "/导入用户模版.xlsx");
            logmanagementList.add(logmanagement);
            url = "http://FILESYSTEM/userfile/downloadUrl";
            headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            JSONArray array = new JSONArray();
            array.add(logmanagementList);
            HttpEntity<String> formEntity = new HttpEntity<String>(array.toString(), headers);
            ApiResult address = restTemplate.postForObject(url, formEntity, ApiResult.class);
            ArrayList list = (ArrayList) address.getData();
            String downloadUrl = (String) list.get(0);
            return downloadUrl;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

}

