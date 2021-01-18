package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.service_pfans.PFANS1000.ThemeInforService;
import com.nt.service_pfans.PFANS1000.mapper.ThemeInforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ThemeInforServiceImpl implements ThemeInforService {
    @Autowired
    private ThemeInforMapper themeinformapper;
    @Override
    public List<ThemeInfor> list(ThemeInfor themeinfor) throws Exception {
        return themeinformapper.select(themeinfor);
    }
    @Override
    public void insert(ThemeInfor themeinfor, TokenModel tokenModel) throws Exception {
        themeinfor.preInsert(tokenModel);
        themeinfor.setThemeinfor_id(UUID.randomUUID().toString());
        themeinformapper.insert(themeinfor);
    }
    @Override
    public void upd(ThemeInfor themeinfor, TokenModel tokenModel) throws Exception {
        themeinfor.preUpdate(tokenModel);
        themeinformapper.updateByPrimaryKey(themeinfor);
    }
    @Override
    public ThemeInfor One(String themeinforid) throws Exception {
        return themeinformapper.selectByPrimaryKey(themeinforid);
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
            model.add("Theme名");
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
                ThemeInfor themeinfor = new ThemeInfor();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    themeinfor.setThemename(value.get(0).toString());
                    List<ThemeInfor> themeinforlist =   themeinformapper.select(themeinfor);
                    if(themeinforlist.size()>0){
                        error = error + 1;
                        Result.add("模板第" + (k-1) + "行的Theme名重复，请输入正确的Theme名，导入失败");
                        continue;
                    }

                }
                themeinfor.setThemeinfor_id(UUID.randomUUID().toString());
                themeinfor.preInsert(tokenModel);
                themeinformapper.insert(themeinfor);
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
