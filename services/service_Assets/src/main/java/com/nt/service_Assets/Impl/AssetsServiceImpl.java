package com.nt.service_Assets.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.InventoryResults;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Vo.AssetsVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Assets.AssetsService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryResultsMapper;
import com.nt.service_Assets.mapper.InventoryplanMapper;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Autowired
    private InventoryResultsMapper assetsResultMapper;

    @Autowired
    private InventoryplanMapper inventoryplanMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public Assets confirm(String code, TokenModel tokenModel) throws Exception {
        Assets assets = new Assets();
        assets.setRfidcd(code);
        List<Assets> rst = assetsMapper.select(assets);
        if(rst.size() > 0){
            return rst.get(0);
        }else{
            return new Assets();
        }
    }

    @Override
    public InventoryResults scanOne(String code, TokenModel tokenModel) throws Exception {
        Inventoryplan cond = new Inventoryplan();
        cond.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<Inventoryplan> list = inventoryplanMapper.select(cond);
        for (Inventoryplan item:
                list ) {
            if(item.getInventorycycle().split("~").length > 1){

                String st = item.getInventorycycle().split("~")[0].trim();
                String ed = item.getInventorycycle().split("~")[1].trim();
                String now = DateUtil.today();
                if(st.compareTo(now) <= 0 && now.compareTo(ed) <= 0){
                    InventoryResults condition = new InventoryResults();
                    condition.setInventoryplan_id(item.getInventoryplan_id());
                    condition.setRfidcd(code);
                    List<InventoryResults> rst = assetsResultMapper.select(condition);
                    if (rst.size() > 0) {
                        InventoryResults inventoryResults = rst.get(0);
                        inventoryResults.preUpdate(tokenModel);
                        inventoryResults.setResult("2");
                        assetsResultMapper.updateByPrimaryKey(inventoryResults);
                        return inventoryResults;
                    }
                }
            }

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

        checkBarCode(assets);

        assets.preInsert(tokenModel);
        if (StrUtil.isNotBlank(assets.getBarcode())) {
            assets.setBarcode(assets.getBarcode());
        } else {
            assets.setBarcode(getBarCode(assets));
//            assets.setBarcode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
        }
        assets.setRfidcd(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
        assets.setAssets_id(UUID.randomUUID().toString());
        assetsMapper.insert(assets);
    }

    private void checkBarCode(Assets assets)throws Exception{

        if(StrUtil.isNotBlank(assets.getBarcode())){
            Assets conditon = new Assets();
            conditon.setBarcode(assets.getBarcode());
            List<Assets> rst = assetsMapper.select(conditon);
            if(rst.size() > 0){
                for(Assets item:rst){
                    if(!"PA003002".equals(assets.getAssetstatus())){
                        if(!item.getAssets_id().equals(assets.getAssets_id())){
                            throw new LogicalException("资产编号重复！");
                        }
                    }
                }
            }
        }

    }

    private String getBarCode(Assets assets){
        String barCode = assetsMapper.getMaxCode(assets.getTypeassets(),assets.getBartype());
        return barCode;
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
        checkBarCode(assets);
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
            String[] qitazican = "名称,资产类型,工号,资产编号,条码类型,资产状态,在库状态,通関資料管理番号,型号,价格,HS编码,輸入日付,延期返却期限,备注,客户,管理番号,機材名称,INVOICEと一致性,设备写真あるか,輸出部門担当者,実施日,動作状況,現場担当者,実施日,備考,動作状況,現場実施者,実施日,INVOICEとの一致性,入荷写真との一致性,梱包状況,現場担当者,輸出部門担当者,実施日,最終確認,現場TL,輸出部門TL,実施日,備考,部门".split(",");
            String[] gudingzichan = "资产类型,资产编号,名称,资产状态,管理者,使用部门,部门代码,条码类型,在库状态,购入时间,价格,帐面净值,型号,PC管理号,PSDCD_借还情况,PSDCD_带出理由,PSDCD_带出开始日,PSDCD_预计归还日,PSDCD_是否逾期,PSDCD_对方单位,PSDCD_责任人,PSDCD_归还确认,备注".split(",");
            String[] jieruzichan = "资产类型,资产编号,名称,担当者,使用部门,部门代码,型号,借出单位,担当者,联系电话,借用合同,借用合同编号,借用开始日,预定返还日,实际返还日,备注1,备注2,备注3".split(",");

            List<Object> header = list.get(0);
            int typeLength = header.size();
            if ( typeLength == qitazican.length ) {
                for ( int i = 0; i<typeLength; i++ ) {
                    if (!header.get(i).toString().trim().equals(qitazican[i])) {
                        throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + qitazican[i]);
                    }
                }
            } else if ( typeLength == gudingzichan.length ) {
                for ( int i = 0; i<typeLength; i++ ) {
                    if (!header.get(i).toString().trim().equals(gudingzichan[i])) {
                        throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + gudingzichan[i]);
                    }
                }
            } else if (typeLength == jieruzichan.length) {
                for ( int i = 0; i<typeLength; i++ ) {
                    if (!header.get(i).toString().trim().equals(jieruzichan[i])) {
                        throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + jieruzichan[i]);
                    }
                }
            } else {
                throw new LogicalException("错误的标题。");
            }

            Map<String, String> PA001Map = getDictionaryMap("PA001");
            Map<String, String> PA002Map = getDictionaryMap("PA002");
            Map<String, String> PA003Map = getDictionaryMap("PA003");
            Map<String, String> PA004Map = getDictionaryMap("PA004");

            int successCount = 0;
            int error = 0;

            for (int lineNo = 1; lineNo < list.size(); lineNo++) {
                Assets assets = new Assets();

                List<Object> value = list.get(lineNo);
                //固定资产导入用
                if (value != null && !value.isEmpty() && ("固定资产".equals(value.get(0).toString())
                        || "簿外_SD卡/U盘/硬盘/光盘/手机/平板电脑/路由器".equals(value.get(0).toString())
                        || "簿外_电脑".equals(value.get(0).toString())
                        || "簿外_其他".equals(value.get(0).toString())
                        || "无形资产".equals(value.get(0).toString()))) {
                    if (!StringUtils.isEmpty(trim(value.get(1)))) {
                        Assets condition = new Assets();
                        checkBarCod(trim(value.get(1)));
                        condition.setBarcode(value.get(1).toString());
                        List<Assets> ls = assetsMapper.select(condition);
                        if (ls.size() > 0) {
                            assets = ls.get(0);
                        } else {
                            assets.setBarcode(value.get(1).toString());
                        }
                    }
                    // 资产类型
                    if (!StringUtils.isEmpty(trim(value.get(0)))) {
                        String tValue = trim(value.get(0));
                        if (PA001Map.containsKey(tValue)) {
                            assets.setTypeassets(PA001Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的资产类型没有找到，导入失败");
                            continue;
                        }
                    } else {
                        error++;
                        Result.add("模板第" + lineNo + "行的资产类型不能为空，导入失败");
                        continue;
                    }
                    //名称
                    if (StringUtils.isEmpty(value.get(2))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    assets.setFilename(trim(value.get(2)));
                    // 资产状态
                    if (!StringUtils.isEmpty(trim(value.get(3)))) {
                        String tValue = trim(value.get(3));
                        if (PA003Map.containsKey(tValue)) {
                            assets.setAssetstatus(PA003Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的资产状态没有找到，导入失败");
                            continue;
                        }
                    }
                    // 管理者
                    if (!StringUtils.isEmpty(trim(value.get(4)))) {
                        CustomerInfo customerInfo = this.getCustomerInfoPer(value.get(4).toString());
                        if (customerInfo != null) {
                            assets.setPrincipal(value.get(4).toString());
                        }
                        if (customerInfo == null) {
                            error = error + 1;
                            Result.add("模板第" + lineNo + "行的管理者的个人MEGAS编码未找到，请输入正确的个人MEGAS编码，导入失败");
                            continue;
                        }
                    }
                    //使用部门
                    if (StringUtils.isEmpty(value.get(5))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    assets.setUsedepartment(trim(value.get(5)));
                    //部门代码
                    if (StringUtils.isEmpty(value.get(6))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    assets.setDepartmentcode(trim(value.get(6)));
//                    if (value.size() > 1) {
//                        int dateCheck = isDate(trim(value.get(9)));
//                        if (dateCheck != 0) {
//                            Result.add(getDateErrMsg(dateCheck, lineNo));
//                            error = error + 1;
//                            continue;
//                        }
//                    }
                    //条码类型,在库状态,购入时间,价格,帐面净值,型号,PC管理号,PSDCD_借还情况,PSDCD_带出理由,PSDCD_带出开始日,PSDCD_预计归还日,PSDCD_是否逾期,PSDCD_对方单位,PSDCD_责任人,PSDCD_归还确认,备注
                    String[] gudingCols = "bartype,stockstatus,purchasetime,price,realprice,model,pcno,psdcddebitsituation,psdcdbringoutreason,psdcdperiod,psdcdreturndate,psdcdisoverdue,psdcdcounterparty,psdcdresponsible,psdcdreturnconfirmation,remarks".split(",");
                    int start = 7;
                    setOrderedValues(start, assets, gudingCols, value);
//                    }
                }
                //借入用导入模板
                if (value != null && !value.isEmpty() && "借用設備".equals(value.get(0).toString())) {
                    if (!StringUtils.isEmpty(trim(value.get(1)))) {
                        Assets condition = new Assets();
                        checkBarCod(trim(value.get(1)));
                        condition.setBarcode(value.get(1).toString());
                        List<Assets> ls = assetsMapper.select(condition);
                        if (ls.size() > 0) {
                            assets = ls.get(0);
                        } else {
                            assets.setBarcode(value.get(1).toString());
                        }
                    }
                    // 资产类型
                    if (!StringUtils.isEmpty(trim(value.get(0)))) {
                        String tValue = trim(value.get(0));
                        if (PA001Map.containsKey(tValue)) {
                            assets.setTypeassets(PA001Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的资产类型没有找到，导入失败");
                            continue;
                        }
                    } else {
                        error++;
                        Result.add("模板第" + lineNo + "行的资产类型不能为空，导入失败");
                        continue;
                    }
                    //名称
                    if (StringUtils.isEmpty(value.get(2))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    assets.setFilename(trim(value.get(2)));

                    // 管理者
                    if (!StringUtils.isEmpty(trim(value.get(3)))) {
                        CustomerInfo customerInfo = this.getCustomerInfoPer(value.get(3).toString());
                        if (customerInfo != null) {
                            assets.setPrincipal(value.get(3).toString());
                        }
                        if (customerInfo == null) {
                            error = error + 1;
                            Result.add("模板第" + lineNo + "行的管理者的个人MEGAS编码未找到，请输入正确的个人MEGAS编码，导入失败");
                            continue;
                        }
                    }
                    //使用部门
                    if (StringUtils.isEmpty(value.get(4))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    assets.setUsedepartment(trim(value.get(4)));
                    //部门代码
                    if (StringUtils.isEmpty(value.get(5))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    assets.setDepartmentcode(trim(value.get(5)));
                    //型号,借出单位,担当者,联系电话,借用合同,借用合同编号,借用开始日,预定返还日,实际返还日,备注1,备注2,备注3
                    String[] gudingCols = "model,remarks".split(","); //0605--与客户确认字段
                    int start = 6;
                    setOrderedValues(start, assets, gudingCols, value);
//                    }
                }
                //对外资产用模板
                if (value != null && !value.isEmpty() && ("加工贸易".equals(value.get(1).toString())
                        || "无偿借用".equals(value.get(1).toString()))) {
                    if (!StringUtils.isEmpty(trim(value.get(3)))) {
                        Assets condition = new Assets();
                        condition.setBarcode(value.get(3).toString());
                        List<Assets> ls = assetsMapper.select(condition);
                        if (ls.size() > 0) {
                            assets = ls.get(0);
                        }
                    }

                    if (StringUtils.isEmpty(value.get(0))) {
                        error++;
                        Result.add("模板第" + lineNo + "行的名称不能为空，导入失败");
                        continue;
                    }
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    assets.setFilename(trim(value.get(0)));
                    List<Dictionary> dicIds = new ArrayList<Dictionary>();
                    // 资产类型
                    if(!StringUtils.isEmpty(trim(value.get(1)))){
                        String tValue = trim(value.get(1));
                        if ( PA001Map.containsKey(tValue) ) {
                            assets.setTypeassets(PA001Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的资产类型没有找到，导入失败");
                            continue;
                        }
                    }else {
                        error++;
                        Result.add("模板第" + lineNo + "行的资产类型不能为空，导入失败");
                        continue;
                    }

                    // 工号
                    // todo 用户信息未导入，此段先注掉
                    if(!StringUtils.isEmpty(trim(value.get(2)))){
                        CustomerInfo customerInfo = this.getCustomerInfo(value.get(2).toString());
                        if (customerInfo != null) {
                            assets.setPrincipal(customerInfo.getUserid());
                        }
                        if (customerInfo == null) {
                            error = error + 1;
                            Result.add("模板第" + lineNo + "行的工号字段没有找到，请输入正确的工号，导入失败");
                            continue;
                        }
//                        assets.setPrincipal(trim(value.get(2)));
                    }

                    // 条码类型
                    if(!StringUtils.isEmpty(trim(value.get(4)))){
                        String tValue = trim(value.get(4));
                        if ( PA004Map.containsKey(tValue) ) {
                            assets.setBartype(PA004Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的条码类型没有找到，导入失败");
                            continue;
                        }

                    } else {
                        error++;
                        Result.add("模板第" + lineNo + "行的条码类型不能为空，导入失败");
                        continue;
                    }

                    // 资产状态
                    if(!StringUtils.isEmpty(trim(value.get(5)))){
                        String tValue = trim(value.get(5));
                        if ( PA003Map.containsKey(tValue) ) {
                            assets.setAssetstatus(PA003Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的资产状态没有找到，导入失败");
                            continue;
                        }

                    }

                    // 在库状态
                    if(!StringUtils.isEmpty(trim(value.get(6)))){
                        String tValue = trim(value.get(6));
                        if ( PA002Map.containsKey(tValue) ) {
                            assets.setStockstatus(PA002Map.get(tValue));
                        } else {
                            error++;
                            Result.add("模板第" + lineNo + "行的在库状态没有找到，导入失败");
                            continue;
                        }
                    }

                    // start by zy
//                   if ("加工贸易".equals(value.get(1).toString())
//                            || "无偿借用".equals(value.get(1).toString())){
                        boolean hasErr = false;
                        if (value.size() > 1) {
                            int[] dateCols = {11, 12, 20, 23, 27, 33, 37};
                            for ( int col : dateCols ) {
                                int dateCheck = isDate(trim(value.get(col)));
                                if ( dateCheck != 0 ) {
                                    error = error + 1;
                                    Result.add(getDateErrMsg(dateCheck, lineNo));
                                    hasErr = true;
                                    break;
                                }
                            }
                            if ( hasErr ) {
                                continue;
                            }
                        }


                        // 輸出部門担当者, 現場担当者, 現場実施者, 現場担当者, 輸出部門担当者, 現場TL, 輸出部門TL
                        // todo 用户信息未导入，此段先注掉
                        int[] customerCols = {19, 22, 26, 31, 32, 35, 36};
                        for (int customerCol : customerCols) {
                            String val = trim(value.get(customerCol));
                            if (StringUtils.hasText(val)) {
                                CustomerInfo info = this.getCustomerInfo(val);
                                if (info == null) {
                                    error = error + 1;
                                    Result.add("模板第" + lineNo + "行的工号字段没有找到，请输入正确的工号，导入失败");
                                    hasErr = true;
                                    break;
                                } else {
                                    value.set(customerCol, info.getUserid());
                                }
                            }
                        }
                        if (hasErr) {
                            continue;
                        }

                        // 是否处理 否->0  是->1
                        int[] iTrueCols = {17, 18, 21, 28, 29, 30, 34};
                        for ( int col : iTrueCols ) {
                            value.set(col, convertToOne(value.get(col)));
                        }

                        /**
                         * （7列 - 16列）通関資料管理番号,型号, 价格， HSコード，輸入日付，延期返却期限，备注，客户，管理番号，機材名称，
                         * (17列 - 24列)： INVOICEと一致性, 設備写真あるか, 輸出部門担当者,実施日, 动作状况, 現場担当者, 実施日，備考
                         * ------- 以下列用for循环添加 ----------（outparams1..14）
                         * （25列 - 33列）動作状況	,現場実施者,実施日,INVOICEとの一致性，入荷写真との一致性，梱包状況，現場担当者，輸出部門担当者，実施日
                         * （34列 - 39列）最終確認，現場TL，輸出部門TL，実施日，備考，部门
                         *
                         */
                        String[] qitaCols = ("pcno,model,price,no,purchasetime,activitiondate,remarks,customer,controlno,machinename," +//(7-16)
                                "inparams1,inparams2,inparams3,inparams4,inparams5,inparams6,inparams7,inparams8") //17- 24
                                .split(",");
                        List<String> qitaList = new ArrayList<String>(Arrays.asList(qitaCols));
                        for ( int i = 1; i<=14; i++) {
                            qitaList.add("outparams" + i);
                        }
                        qitaList.add("usedepartment");
                        qitaCols = qitaList.toArray(new String[qitaList.size()]);
                        int start = 7;
                        setOrderedValues(start, assets, qitaCols, value);
//                    }
                    // end by zy
                }
                if (StrUtil.isNotBlank(assets.getAssets_id())) {
                    assets.preUpdate(tokenModel);
                    assetsMapper.updateByPrimaryKey(assets);
                } else {
                    if (!StringUtils.isEmpty(trim(value.get(3)))) {
                        assets.setBarcode(trim(value.get(3)));
                    } else {
                        assets.setBarcode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
                    }
                    assets.setRfidcd(DateUtil.format(new Date(), "yyyyMMddHHmmssSSSSSS"));
                    assets.preInsert(tokenModel);
                    assets.setAssets_id(UUID.randomUUID().toString());
                    assetsMapper.insert(assets);
                }

                listVo.add(assets);
                successCount++;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + successCount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    public List<String> getDepartment(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        return assetsMapper.getDepartment();
    }

    //    add_fjl_05/25  --添加资产编号重复check
    private void checkBarCod(String importBarcode) throws Exception {
        if (StrUtil.isNotBlank(importBarcode)) {
            Assets conditon = new Assets();
            conditon.setBarcode(importBarcode);
            List<Assets> rst = assetsMapper.select(conditon);
            if (rst.size() > 0) {
                throw new LogicalException("资产编号 " + importBarcode + " 重复！");
            }
        }
    }

    //    add_fjl_05/25  --添加资产编号重复check
    private int isDate(String str) {
        try{
            if ( StrUtil.isNotEmpty(str) ) {
                String month = str.substring(5, 7);
                String day = str.substring(8, 10);
                if ( Integer.parseInt(day) > 31 ) {
                    //"模板第" + lineNo + "行的日期格式错误，请输入正确的日子，导入失败"
                    return 3;
                } else if ( Integer.parseInt(month) > 12 ) {
                    //"模板第" + lineNo + "行的日期格式错误，请输入正确的月份，导入失败"
                    return 2;
                }
            }
        }catch (Exception e){
            return 2;
        }
        return 0;
    }

    private String getDateErrMsg(int errCode, int line) {
        if (errCode == 2) {
            return "模板第" + line  + "行的日期格式错误，请输入正确的月份，导入失败";
        } else if (errCode == 3) {
            return "模板第" + line + "行的日期格式错误，请输入正确的日子，导入失败";
        } else {
            return "模板第" + line + "行的日期格式错误，导入失败";
        }
    }

    private static final SimpleDateFormat _SF = new SimpleDateFormat("yyyy-MM-dd");
    private void setOrderedValues(int start, Object target, String[] cols, List<Object> value) throws Exception{
        for ( String col : cols ) {
            if (value.size() > start) {
                String val = trim(value.get(start));
                if (StringUtils.hasText(val)) {
                    Class c = PropertyUtils.getPropertyType(target, col);
                    if (c.equals(Date.class)) {
                        val = val.replace("/", "-");
                        Date d = _SF.parse(val);
                        PropertyUtils.setProperty(target, col, d);
                    } else {
                        PropertyUtils.setProperty(target, col, val);
                    }
                }
            }
            start++;
        }
    }


    private String trim(Object o) {
        if ( o==null ) {
            return null;
        }
        return o.toString().trim();
    }

    private CustomerInfo getCustomerInfoPer(String personalcodeIn) {
        Query query = new Query();
        String personalcode = personalcodeIn;
        query.addCriteria(Criteria.where("userinfo.personalcode").is(personalcode));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        return customerInfo;
    }

    private CustomerInfo getCustomerInfo(String jobnumberIn) {
        Query query = new Query();
        String jobnumber = jobnumberIn;
        query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        return customerInfo;
    }

    /**
     * 是否处理 否->0  是->1
     * @param o
     * @return
     *  o==是 || o==1 return 1
     */
    private String convertToOne(Object o) {
        String s = o!=null ? o.toString().trim() : "";
        if ("是".equals(s) || "1".equals(s)) {
            return "1";
        }
        return "0";
    }

    private Map<String, String> getDictionaryMap(String code) throws Exception {
        List<Dictionary> diclist = dictionaryService.getForSelect(code);
        Map<String, String> result = new HashMap<>();
        for (Dictionary d : diclist) {
            result.put(d.getValue1(), d.getCode());
        }
        return result;
    }


    @Override
    public List<Assets> getAssetsnameList(Assets assets, HttpServletRequest request) throws Exception {
        return assetsMapper.select(assets);
    }

}
