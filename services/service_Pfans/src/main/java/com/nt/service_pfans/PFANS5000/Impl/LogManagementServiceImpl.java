package com.nt.service_pfans.PFANS5000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.service_pfans.PFANS5000.LogManagementService;
import com.nt.service_pfans.PFANS5000.mapper.LogManagementMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class LogManagementServiceImpl implements LogManagementService {

    @Autowired
    private LogManagementMapper logmanagementmapper;

    @Override
    public void insert(LogManagement logmanagement, TokenModel tokenModel) throws Exception {
        logmanagement.preInsert(tokenModel);
        logmanagement.setLogmanagement_id(UUID.randomUUID().toString());
        logmanagementmapper.insert(logmanagement);
    }


    @Override
    public List<LogManagement> getDataList(LogManagement logmanagement) throws Exception {

        return logmanagementmapper.select(logmanagement);
    }

    @Override
    public void update(LogManagement logmanagement, TokenModel tokenModel) throws Exception {
        logmanagement.preUpdate(tokenModel);
        logmanagementmapper.updateByPrimaryKey(logmanagement);
    }

    @Override
    public LogManagement One(String logmanagement_id) throws Exception {
        LogManagement log = logmanagementmapper.selectByPrimaryKey(logmanagement_id);
        return log;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<LogManagement> importUser(HttpServletRequest request, TokenModel tokenModel, String flag) throws Exception {
        try {
            List<LogManagement> listVo = new ArrayList<LogManagement>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            // MultipartFile转为File
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            // 解析excel
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();

                // 格式check
                List<LogManagement> error = importCheck(list);

                int k = 1;
                for (int i = 1; i < list.size(); i++) {
                    LogManagement logmanagement = new LogManagement();
                    List<Object> value = list.get(k);
                    k++;
                    if (value != null && !value.isEmpty()) {
                        if (value.get(0).toString().equals("")) {
                            continue;
                        }
                        String Log_date = value.get(5).toString();
                        String Time_start = value.get(6).toString();
                        String Time_end = value.get(7).toString();
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        logmanagement.setLog_date(sf1.parse(Log_date));
                        logmanagement.setTime_start(sf.parse(Time_start));
                        logmanagement.setTime_end(sf.parse(Time_end));
                        logmanagement.setWork_memo(value.get(8).toString());
                        if (logmanagement.getProject_id().length()>0) {
                            String s2="01";
                            logmanagement.setHas_project(s2);
                        } else {
                            String s3="02";
                            logmanagement.setHas_project(s3);
                        }
                    }
                    logmanagement.preInsert(tokenModel);
                    logmanagement.setLogmanagement_id(UUID.randomUUID().toString());
                    logmanagementmapper.insert(logmanagement);
                    listVo.add(logmanagement);
                }
            return listVo;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<LogManagement> importCheck(List<List<Object>> list) throws Exception {
        List<LogManagement> listVo = new ArrayList<LogManagement>();
        List<Object> model = new ArrayList<Object>();
        Map checkMobileMap = new HashMap();
        Map checkEmailMap = new HashMap();
        model.add("姓名");
        model.add("センター");
        model.add("グループ");
        model.add("チーム");
        model.add("项目");
        model.add("日志日期");
        model.add("开始时间");
        model.add("结束时间");
        model.add("工作备注");
        List<Object> key = list.get(0);
        try {
            // check字段名称是否正确
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            LogManagement logmanagement = new LogManagement();
            for (int i = 1; i < list.size(); i++) {
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    if (value.size() > 0) {
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        logmanagement.setLog_date(sf1.parse(value.get(5).toString()));
                        logmanagement.setTime_start(sf.parse(value.get(6).toString()));
                        logmanagement.setTime_end(sf.parse(value.get(7).toString()));
                        logmanagement.setWork_memo(value.get(8).toString());
                    }
                }
            }
            listVo.add(logmanagement);
            return listVo;
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

