package com.nt.service_Assets.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.InventoryResults;
import com.nt.service_Assets.AssetsService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryResultsMapper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Autowired
    private InventoryResultsMapper assetsResultMapper;

    @Override
    public int scanOne(String code, TokenModel tokenModel) throws Exception {
        InventoryResults condition = new InventoryResults();
        condition.setBarcode(code);
        List<InventoryResults> rst = assetsResultMapper.select(condition);
        if(rst.size() > 0){
            InventoryResults inventoryResults = rst.get(0);
            inventoryResults.preUpdate(tokenModel);
            inventoryResults.setResult("2");
            assetsResultMapper.updateByPrimaryKey(inventoryResults);
            return 1;
        }
        return 0;
    }

    @Override
    public int scanList(List<String> code, TokenModel tokenModel) throws Exception {
        int rst = 0;
        for(String item:code){
            rst +=scanOne(item,tokenModel);
        }
        return rst;
    }

    @Override
    public List<Assets> list(Assets assets) throws Exception {
        return assetsMapper.select(assets);
    }

    @Override
    public void insert(Assets assets, TokenModel tokenModel) throws Exception {
        assets.preInsert(tokenModel);
        assets.setAssets_id(UUID.randomUUID().toString());
        assetsMapper.insert(assets);
    }

    @Override
    public void update(Assets assets, TokenModel tokenModel) throws Exception {
        assets.preUpdate(tokenModel);
        assetsMapper.updateByPrimaryKey(assets);
    }

    @Override
    public Assets One(String assetsid) throws Exception {
        return assetsMapper.selectByPrimaryKey(assetsid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Assets> listVo = new ArrayList<Assets>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("件名");
            model.add("类型");
            model.add("价格");
            model.add("购入时间");
            model.add("使用部门");
            model.add("设备负责人");
            model.add("资产状态");
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
                Assets assets = new Assets();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    if (value.size() > 1) {
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
                    assets.setFilename(value.get(0).toString());
                    assets.setTypeassets(value.get(1).toString());
                    assets.setPrice(value.get(2).toString());
                    assets.setPurchasetime(sf.parse(value.get(3).toString()));
                    assets.setUsedepartment(value.get(4).toString());
                    assets.setPrincipal(value.get(5).toString());
                    assets.setAssetstatus(value.get(6).toString());
                }
                assets.preInsert(tokenModel);
                assets.setAssets_id(UUID.randomUUID().toString());
                assetsMapper.insert(assets);
                listVo.add(assets);
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
