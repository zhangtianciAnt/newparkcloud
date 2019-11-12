package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
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
import com.nt.dao_Org.CustomerInfo;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import com.mysql.jdbc.StringUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class PunchcardRecordServiceImpl implements PunchcardRecordService {
    @Autowired
    private PunchcardRecordMapper punchcardrecordMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PunchcardRecord> list(PunchcardRecord punchcardrecord) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<PunchcardRecord> importUser(HttpServletRequest request, TokenModel tokenModel, String flag) throws Exception {
        try {
            List<PunchcardRecord> listVo = new ArrayList<PunchcardRecord>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");

            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);

            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            if (StringUtils.isNullOrEmpty(flag)) {
                // 格式check
                List<PunchcardRecord> error = importCheck(list);
                return error;
            } else {
                int k = 1;
                for (int i = 1; i < list.size(); i++) {
                    PunchcardRecord punchcardrecord = new PunchcardRecord();
                    List<Object> value = list.get(k);
                    k++;
                    if (value != null && !value.isEmpty()) {
                        if (value.get(0).toString().equals("")) {
                            continue;
                        }
                        Query query = new Query();
                        String customername = value.get(0).toString();
                        query.addCriteria(Criteria.where("userinfo.customername").is(customername));
                        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        punchcardrecord.setUser_id(customerInfo.getUserid());
                        punchcardrecord.setCenterid(value.get(1).toString());
                        punchcardrecord.setGroupid(value.get(2).toString());
                        punchcardrecord.setTeamid(value.get(3).toString());
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String Punchcardrecord_date = value.get(4).toString();
                        String Time_start = value.get(5).toString();
                        String Time_end = value.get(6).toString();
                        punchcardrecord.setPunchcardrecord_date(sf1.parse(Punchcardrecord_date));
                        punchcardrecord.setTime_start(sf.parse(Time_start));
                        punchcardrecord.setTime_end(sf.parse(Time_end));
                    }
                    punchcardrecord.preInsert(tokenModel);
                    punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                    punchcardrecordMapper.insert(punchcardrecord);
                    listVo.add(punchcardrecord);
                }
            }
            return listVo;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<PunchcardRecord> importCheck(List<List<Object>> list) throws Exception {
        List<PunchcardRecord> listVo = new ArrayList<PunchcardRecord>();
        List<Object> model = new ArrayList<Object>();
        Map checkMobileMap = new HashMap();
        Map checkEmailMap = new HashMap();
        model.add("姓名");
        model.add("センター");
        model.add("グループ");
        model.add("チーム");
        model.add("日期");
        model.add("首次打卡");
        model.add("末次打卡");
        List<Object> key = list.get(0);
        try {
            // check字段名称是否正确
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            PunchcardRecord punchcardrecord = new PunchcardRecord();
            for (int i = 1; i < list.size(); i++) {
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    if (value.size() > 0) {
                        punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                        punchcardrecord.setUser_id(value.get(0).toString());
                        punchcardrecord.setCenterid(value.get(1).toString());
                        punchcardrecord.setGroupid(value.get(2).toString());
                        punchcardrecord.setTeamid(value.get(3).toString());
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        punchcardrecord.setPunchcardrecord_date(sf1.parse(value.get(4).toString()));
                        punchcardrecord.setTime_start(sf.parse(value.get(5).toString()));
                        punchcardrecord.setTime_end(sf.parse(value.get(6).toString()));
                    }
                }
            }
            listVo.add(punchcardrecord);
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
            List<PunchcardRecord> punchcardRecordList = new ArrayList<PunchcardRecord>();
            PunchcardRecord punchcardrecord = new PunchcardRecord();
            punchcardrecord.setUser_id(repoId + "/导入用户模版.xlsx");
            punchcardRecordList.add(punchcardrecord);
            url = "http://FILESYSTEM/userfile/downloadUrl";
            headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            JSONArray array = new JSONArray();
            array.add(punchcardRecordList);
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
