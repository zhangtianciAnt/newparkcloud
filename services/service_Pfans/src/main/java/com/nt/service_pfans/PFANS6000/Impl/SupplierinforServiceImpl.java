package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
import com.nt.service_pfans.PFANS6000.mapper.SupplierinforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierinforServiceImpl implements SupplierinforService {

    @Autowired
    private SupplierinforMapper supplierinforMapper;


    @Override
    public List<Supplierinfor> getsupplierinfor(Supplierinfor supplierinfor) throws Exception {
        return supplierinforMapper.select(supplierinfor);
    }

    @Override
    public Supplierinfor getsupplierinforApplyOne(String supplierinfor_id) throws Exception {
        return supplierinforMapper.selectByPrimaryKey(supplierinfor_id);
    }

    @Override
    public void updatesupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception {
        supplierinforMapper.updateByPrimaryKeySelective(supplierinfor);
    }

    @Override
    public void createsupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception {
        supplierinfor.preInsert(tokenModel);
        supplierinfor.setSupplierinfor_id(UUID.randomUUID().toString());
        supplierinforMapper.insert(supplierinfor);
    }

    @Override
    public List<Supplierinfor> getSupplierNameList(Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        return supplierinforMapper.select(supplierinfor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> supimport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Supplierinfor> listVo = new ArrayList<Supplierinfor>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("temp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("供应商名称(中文)");
            model.add("供应商名称(日文)");
            model.add("供应商名称(英文)");
            model.add("简称");
            model.add("负责人");
            model.add("项目联络人(中文)");
            model.add("项目联络人(日文)");
            model.add("项目联络人(英文)");
            model.add("联系电话");
            model.add("邮箱地址");
            model.add("共通事务联络人");
            model.add("联系电话");
            model.add("邮箱地址");
            model.add("地址(中文)");
            model.add("地址(日文)");
            model.add("地址(英文)");
            model.add("人员规模");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LoginException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Supplierinfor supplierinfor = new Supplierinfor();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    supplierinfor.setSupchinese(value.get(0).toString());
                    supplierinfor.setSupjapanese(value.get(1).toString());
                    supplierinfor.setSupenglish(value.get(2).toString());
                    supplierinfor.setAbbreviation(value.get(3).toString());
                    supplierinfor.setLiableperson(value.get(4).toString());
                    supplierinfor.setProchinese(value.get(5).toString());
                    supplierinfor.setProjapanese(value.get(6).toString());
                    supplierinfor.setProenglish(value.get(7).toString());
                    supplierinfor.setProtelephone(value.get(8).toString());
                    supplierinfor.setProtemail(value.get(9).toString());
                    supplierinfor.setCommontperson(value.get(10).toString());
                    supplierinfor.setComtelephone(value.get(11).toString());
                    supplierinfor.setComnemail(value.get(12).toString());
                    supplierinfor.setAddchinese(value.get(13).toString());
                    supplierinfor.setAddjapanese(value.get(14).toString());
                    supplierinfor.setAddenglish(value.get(15).toString());
                    supplierinfor.setPerscale(value.get(16).toString());
                }
                supplierinfor.preInsert();
                supplierinfor.setSupplierinfor_id(UUID.randomUUID().toString());
                supplierinforMapper.insert(supplierinfor);
                listVo.add(supplierinfor);
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
