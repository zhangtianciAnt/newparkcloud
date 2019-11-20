package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
import com.nt.service_pfans.PFANS2000.mapper.OtherTwoMapper;
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
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class OtherTwoServiceImpl implements OtherTwoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Override
    public List<OtherTwo> list(OtherTwo othertwo) throws Exception {
        return othertwoMapper.select(othertwo);
    }

    @Override
    public void insert(OtherTwo othertwo, TokenModel tokenModel) throws Exception {
        othertwo.preInsert(tokenModel);
        othertwo.setOthertwo_id(UUID.randomUUID().toString());
        int rowundex = 0;
        rowundex = rowundex + 1;
        othertwo.setRowindex(rowundex);
        othertwoMapper.insert(othertwo);
    }

    @Override
    public void deletete(OtherTwo othertwo, TokenModel tokenModel) throws Exception{
        othertwoMapper.delete(othertwo);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<OtherTwo> importUser(HttpServletRequest request, TokenModel tokenModel, String flag) throws Exception {
        try {
            List<OtherTwo> listVo = new ArrayList<OtherTwo>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            if (StringUtils.isNullOrEmpty(flag)) {
                List<OtherTwo> error = importCheck(list);
                return error;
            } else {
                int k = 1;
                for (int i = 1; i < list.size()-1; i++) {
                    OtherTwo othertwo = new OtherTwo();
                    List<Object> value = list.get(k);
                    k++;
                    if (value != null && !value.isEmpty()) {
                        if (value.get(0).toString().equals("")) {
                            continue;
                        }
                        Query query = new Query();
                        String customername = value.get(1).toString();
                        query.addCriteria(Criteria.where("userinfo.customername").is(customername));
                        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        othertwo.setUser_id(customerInfo.getUserid());
                        othertwo.setMoneys(value.get(2).toString());
                        othertwo.setRootknot(value.get(3).toString());
                    }
                    int rowundex = i;
                    othertwo.setRowindex(rowundex);
                    othertwo.setType("1");
                    othertwo.preInsert(tokenModel);
                    othertwo.setOthertwo_id(UUID.randomUUID().toString());
                    othertwoMapper.insert(othertwo);
                    listVo.add(othertwo);
                }
            }
            return listVo;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<OtherTwo> importCheck(List<List<Object>> list) throws Exception {
        List<OtherTwo> listVo = new ArrayList<OtherTwo>();
        List<Object> model = new ArrayList<Object>();
        Map checkMobileMap = new HashMap();
        Map checkEmailMap = new HashMap();
        model.add("No.");
        model.add("名字");
        model.add("金額");
        model.add("根拠");
        List<Object> key = list.get(0);
        try {
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            OtherTwo othertwo = new OtherTwo();
            for (int i = 1; i < list.size(); i++) {
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    if(value.size() > 2) {
                        if (value.get(2).toString().length()>20) {
                            throw new LogicalException("导入失败，第" + (k) + "行第" + (i + 1) + "列金额长度超出范围，请输入长度为20位之内的金额");
                        }
                    }
                    if(value.size() > 3) {
                        if (value.get(3).toString().length()>20) {
                            throw new LogicalException("导入失败，第" + (k) + "行第" + (i + 1) + "列根拠长度超出范围，请输入长度为20位之内的根拠");
                        }
                    }
                }
                listVo.add(othertwo);
            }
            return listVo;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
