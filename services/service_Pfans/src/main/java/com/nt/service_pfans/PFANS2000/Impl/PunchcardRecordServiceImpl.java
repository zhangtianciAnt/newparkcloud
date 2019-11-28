package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<PunchcardRecord> listVo = new ArrayList<PunchcardRecord>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("工号");
            model.add("姓名");
            model.add("日期");
            model.add("首次打卡");
            model.add("末次打卡");
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
                PunchcardRecord punchcardrecord = new PunchcardRecord();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String Time_start = value.get(3).toString();
                    String Time_end = value.get(4).toString();
                    int result1 = Time_start.compareTo(Time_end);
                    if (result1 >= 0) {
                        error = error + 1;
                        Result.add("模板第" + (k-1) + "行的时间格式错误，开始时间不可以大于或等于结束时间，导入失败");
                        continue;
                    }
                    if (value.size() > 1) {
                        String date = value.get(2).toString();
                        String date1 = value.get(2).toString();
                        date = date.substring(5, 7);
                        date1 = date1.substring(8, 10);
                        if (Integer.parseInt(date1) > 31) {
                            error = error + 1;
                            Result.add("模板第" + (k-1) + "行的日期格式错误，请输入正确的日子，导入失败");
                            continue;
                        }
                        if (Integer.parseInt(date) > 12) {
                            error = error + 1;
                            Result.add("模板第" + (k-1) + "行的日期格式错误，请输入正确的月份，导入失败");
                            continue;
                        }
                    }
                    Query query = new Query();
                    String jobnumber = value.get(0).toString();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        punchcardrecord.setUser_id(customerInfo.getUserid());
                    }
                    punchcardrecord.setJobnumber(value.get(0).toString());
                    punchcardrecord.setCenterid(customerInfo.getUserinfo().getCentername());
                    punchcardrecord.setGroupid(customerInfo.getUserinfo().getGroupname());
                    punchcardrecord.setTeamid(customerInfo.getUserinfo().getTeamname());
                    String Punchcardrecord_date = value.get(2).toString();
                    punchcardrecord.setPunchcardrecord_date(sf1.parse(Punchcardrecord_date));
                    punchcardrecord.setTime_start(sf.parse(Time_start));
                    punchcardrecord.setTime_end(sf.parse(Time_end));
                }
                punchcardrecord.preInsert(tokenModel);
                punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                punchcardrecordMapper.insert(punchcardrecord);
                listVo.add(punchcardrecord);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
