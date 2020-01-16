package com.nt.service_Assets.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.InventoryResults;
import com.nt.dao_Assets.Vo.AssetsVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Assets.AssetsService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryResultsMapper;
import com.nt.service_Org.DictionaryService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Autowired
    private InventoryResultsMapper assetsResultMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public InventoryResults scanOne(String code, TokenModel tokenModel) throws Exception {
        InventoryResults condition = new InventoryResults();
        condition.setRfidcd(code);
        List<InventoryResults> rst = assetsResultMapper.select(condition);
        if (rst.size() > 0) {
            InventoryResults inventoryResults = rst.get(0);
            inventoryResults.preUpdate(tokenModel);
            inventoryResults.setResult("2");
            assetsResultMapper.updateByPrimaryKey(inventoryResults);
            return inventoryResults;
        }
        return new InventoryResults();
    }

    @Override
    public int scanList(String code, TokenModel tokenModel) throws Exception {
        int rst = 0;
        String[] codes = code.split(";");
        for (String item : codes) {
            InventoryResults inventoryResults = scanOne(item, tokenModel);
            if (StrUtil.isNotBlank(inventoryResults.getInventoryresults_id())) {
                rst++;
            }
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
        if (StrUtil.isNotBlank(assets.getBarcode())) {
            assets.setBarcode(assets.getBarcode());
        } else {
            assets.setBarcode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
        }
        assets.setRfidcd(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
        assets.setAssets_id(UUID.randomUUID().toString());
        assetsMapper.insert(assets);
    }

    @Override
    public void insertLosts(AssetsVo assetsVo, TokenModel tokenModel) throws Exception {

        for (int i = 0; i < assetsVo.getSum(); i++) {
            Assets assets = new Assets();
            assets.setBartype(assetsVo.getBartype());
            assets.setTypeassets(assetsVo.getTypeassets());
            insert(assets, tokenModel);
        }
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
    public List<String> importDate(HttpServletRequest request, TokenModel tokenModel) throws Exception {
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
            model.add("工号");
            model.add("条形码");
            model.add("条码类型");
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

                if (StrUtil.isNotBlank(value.get(6).toString())) {
                    Assets condition = new Assets();
                    condition.setBarcode(value.get(6).toString());
                    List<Assets> ls = assetsMapper.select(condition);
                    if (ls.size() > 0) {
                        assets = ls.get(0);
                    }
                }
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
                    List<Dictionary> diclist = dictionaryService.getForSelect("PA001");
                    List<Dictionary> dicIds = diclist.stream().filter(item -> (item.getValue1().equals(value.get(1).toString()))).collect(Collectors.toList());
                    if (dicIds.size() > 0) {
                        assets.setTypeassets(dicIds.get(0).getCode());
                    }
                    assets.setPrice(value.get(2).toString());
                    assets.setPurchasetime(sf.parse(value.get(3).toString()));
                    assets.setUsedepartment(value.get(4).toString());
                    Query query = new Query();
                    String jobnumber = value.get(5).toString();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        assets.setPrincipal(customerInfo.getUserid());
                    }
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的工号字段没有找到，请输入正确的工号，导入失败");
                        continue;
                    }

                    diclist = dictionaryService.getForSelect("PA003");
                    dicIds = diclist.stream().filter(item -> (item.getValue1().equals(value.get(8).toString()))).collect(Collectors.toList());
                    if (dicIds.size() > 0) {
                        assets.setAssetstatus(dicIds.get(0).getCode());
                    }

                    diclist = dictionaryService.getForSelect("PA004");
                    dicIds = diclist.stream().filter(item -> (item.getValue1().equals(value.get(7).toString()))).collect(Collectors.toList());
                    if (dicIds.size() > 0) {
                        assets.setBartype(dicIds.get(0).getCode());
                    }
                }
                if (StrUtil.isNotBlank(assets.getAssets_id())) {
                    assets.preUpdate(tokenModel);
                    assetsMapper.updateByPrimaryKey(assets);
                } else {
                    if (StrUtil.isNotBlank(value.get(6).toString())) {
                        assets.setBarcode(value.get(6).toString());
                    } else {
                        assets.setBarcode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
                    }
                    assets.setRfidcd(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
                    assets.preInsert(tokenModel);
                    assets.setAssets_id(UUID.randomUUID().toString());
                    assetsMapper.insert(assets);
                }

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

    @Override
    public List<Assets> getAssetsnameList(Assets assets, HttpServletRequest request) throws Exception {
        return assetsMapper.select(assets);
    }


}
