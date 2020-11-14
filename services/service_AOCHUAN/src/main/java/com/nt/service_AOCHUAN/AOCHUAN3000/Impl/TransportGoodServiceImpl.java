package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.aliyuncs.utils.IOUtils;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Applicationrecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Receivablesrecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Saledetails;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.DocumentExportVo;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.PurchaseExportVo;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.SalesExportVo;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.Impl.xls.MyXLSTransformer;
import com.nt.service_AOCHUAN.AOCHUAN3000.TransportGoodService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ApplicationrecordMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ReceivablesrecordMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.SaledetailsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.TransportGoodMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinPurchaseMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinSalesMapper;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class TransportGoodServiceImpl implements TransportGoodService {

    @Autowired
    private TransportGoodMapper transportGoodMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private FinPurchaseMapper finPurchaseMapper;

    @Autowired
    private FinSalesMapper finSalesMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ContractNumber contractNumber;
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SaledetailsMapper saledetailsMapper;

    @Autowired
    private ReceivablesrecordMapper receivablesrecordMapper;

    @Autowired
    private ApplicationrecordMapper applicationrecordMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    DecimalFormat df = new DecimalFormat("#.00");


    @Override
    public List<TransportGood> get(TransportGood transportGood) throws Exception {
//        return transportGoodMapper.select(transportGood);
        return transportGoodMapper.getTransportGoodList();
    }

    @Override
    public TransportGood getOne(String id) throws Exception {
        TransportGood transportGood = transportGoodMapper.selectByPrimaryKey(id);
        Saledetails saledetails = new Saledetails();
        saledetails.setTransportgood_id(id);
        List<Saledetails> saledetailsList = saledetailsMapper.getSailDetails(id);
        if (saledetailsList.size() > 0) {
            transportGood.setSaledetails(saledetailsList);
        }
        Receivablesrecord receivablesrecord = new Receivablesrecord();
        receivablesrecord.setTransportgood_id(id);
        List<Receivablesrecord> receivablesrecords = receivablesrecordMapper.getReceivablesRecord(id);
        if (receivablesrecords.size() > 0) {
            transportGood.setReceivablesrecord(receivablesrecords);
        }
        Applicationrecord applicationrecord = new Applicationrecord();
        applicationrecord.setTransportgood_id(id);
        List<Applicationrecord> applicationrecords = applicationrecordMapper.getApplicationRecord(id);
        if (applicationrecords.size() > 0) {
            transportGood.setApplicationrecord(applicationrecords);
        }
        return transportGood;
    }

    @Override
    public List<TransportGood> getForSupplier(String id) throws Exception {
        return transportGoodMapper.getForSupplier(id);
    }

    @Override
    public List<TransportGood> getForCustomer(String id) throws Exception {
        return transportGoodMapper.getForCustomer(id);
    }

    @Override
    public void update(TransportGood transportGood, TokenModel tokenModel) throws Exception {
        transportGood.preUpdate(tokenModel);
//        String[] arr =  getMergeField(transportGood.getSaledetails());
//        transportGood.setProducten(arr[0]);
//        transportGood.setSupplier(arr[1]);
//        transportGood.setCasnum(arr[2]);
        transportGoodMapper.updateByPrimaryKeySelective(transportGood);
        String id = transportGood.getTransportgood_id();
        if(StringUtils.isNoneEmpty(id)){
            DeleteSonTable(id);
            InsertSonTable(transportGood, id,tokenModel);
            if (transportGood.isNotice()) {
                ToDoNotice(tokenModel, transportGood);
            }
        }
        ToDoNoticeFinance(tokenModel, transportGood);
    }

    @Override
    public void insert(TransportGood transportGood, TokenModel tokenModel) throws Exception {
        // String number = contractNumber.getContractNumber("PT001010", "transportgood");
        String id = UUID.randomUUID().toString();
//        String[] arr =  getMergeField(transportGood.getSaledetails());
//        transportGood.setProducten(arr[0]);
//        transportGood.setSupplier(arr[1]);
//        transportGood.setCasnum(arr[2]);
        //transportGood.setContractnumber(number);
        transportGood.setTransportgood_id(id);
        transportGood.preInsert(tokenModel);
        transportGoodMapper.insert(transportGood);
        InsertSonTable(transportGood, id,tokenModel);
    }

    @Override
    public void delete(String id) throws Exception {
        transportGoodMapper.deleteByPrimaryKey(id);
        //DeleteSonTable(id);
    }

    private String[] getMergeField(List<Saledetails> saledetails) {
        String[] product = new String[saledetails.size()];
        String[] supplier = new String[saledetails.size()];
        String[] cas = new String[saledetails.size()];

        for (int i = 0; i < saledetails.size(); i++) {
            product[i] = StringUtils.isNotBlank(saledetails.get(i).getProductname()) ? saledetails.get(i).getProductname() : "无";
            supplier[i] = StringUtils.isNotBlank(saledetails.get(i).getSuppliername()) ? saledetails.get(i).getSuppliername() : "无";
            cas[i] = StringUtils.isNotBlank(saledetails.get(i).getCasnum()) ? saledetails.get(i).getCasnum() : "无";
        }

        return new String[]{StringUtils.join(product, "、"), StringUtils.join(supplier, "、"), StringUtils.join(cas, "、")};
    }


    private void InsertSonTable(TransportGood transportGood, String id, TokenModel tokenModel) throws Exception {
        if (transportGood.getSaledetails().size() > 0) {
            List<Saledetails> saledetailsList = transportGood.getSaledetails();
            for (Saledetails val : saledetailsList) {
                val.preInsert(tokenModel);
                val.setTransportgood_id(id);
                val.setSaledetails_id(UUID.randomUUID().toString());
            }
            saledetailsMapper.insertSaledetailsList(saledetailsList);
        }

        if (transportGood.getReceivablesrecord().size() > 0) {
            List<Receivablesrecord> receivablesrecords = transportGood.getReceivablesrecord();
            for (Receivablesrecord val : receivablesrecords) {
                val.preInsert(tokenModel);
                val.setTransportgood_id(id);
                val.setReceivablesrecord_id(UUID.randomUUID().toString());
            }
            if (transportGood.getFinance() == 1) {
                insertHK(receivablesrecords, transportGood,tokenModel);
            }
            receivablesrecordMapper.insertReceivablesrecordList(receivablesrecords);
        }

        if (transportGood.getApplicationrecord().size() > 0) {
            List<Applicationrecord> applicationrecords = transportGood.getApplicationrecord();
            for (Applicationrecord val : applicationrecords) {
                val.preInsert(tokenModel);
                val.setTransportgood_id(id);
                val.setApplicationrecord_id(UUID.randomUUID().toString());
            }
            if (transportGood.getFinance() == 2) {
                insertCW(applicationrecords, transportGood,tokenModel);
            }
            applicationrecordMapper.insertApplicationrecordList(applicationrecords);
        }
    }

    private void DeleteSonTable(String id) {
        Saledetails saledetails = new Saledetails();
        saledetails.setTransportgood_id(id);
        int all = saledetailsMapper.selectCount(saledetails);
        int delete = saledetailsMapper.delete(saledetails);
        log.info("-------------正在删除saledetails--------------");
        log.info("走货id：" + id);
        log.info("该走货相关saledetails数据共有：" + all + "条");
        log.info("删除"+ delete + "个");
        log.info("--------------删除结束-------------------------");
        if(delete > 100){
            log.info("----------该走货数据异常，走货id："+ id + "-----------");
        }
        Receivablesrecord receivablesrecord = new Receivablesrecord();
        receivablesrecord.setTransportgood_id(id);
        receivablesrecordMapper.delete(receivablesrecord);
        Applicationrecord applicationrecord = new Applicationrecord();
        applicationrecord.setTransportgood_id(id);
        applicationrecordMapper.delete(applicationrecord);
    }


    public void insertCW(List<Applicationrecord> applicationrecords, TransportGood transportGood, TokenModel tokenModel) {
        for (Applicationrecord val :
                applicationrecords) {
            FinPurchase finPurchase = new FinPurchase();
            finPurchase.setPurchase_id(UUID.randomUUID().toString());
            finPurchase.setContractnumber(transportGood.getContractnumber());
            finPurchase.setSupplier(val.getSupplierid());
            finPurchase.setUnitprice1(val.getUnitprice1());//单价
            finPurchase.setCurrency1(val.getCurrency1());//币种
            finPurchase.setPurchase_amount(val.getNumbers1());//数量
            finPurchase.setUnit1(val.getUnit());//单位
            finPurchase.setPaymenttime(val.getRealdate());//应付日期
            finPurchase.setInvoicenumber(val.getInvoiceno());//发票号码
            finPurchase.setCredential_status("PW001001");
            finPurchase.setRealpay(val.getRealpay() == null ? "0.00" : val.getRealpay().toString());//应付金额
            finPurchase.setRealamount(val.getRealamount());//实付金额
            finPurchase.setApplicationrecord_id(val.getApplicationrecord_id());
            finPurchase.setProductresponsibility(transportGood.getProductresponsibility());//采购负责人
            finPurchase.setProducten(val.getProductid());//产品id
            finPurchase.setSupplier(val.getSupplierid());//供应商id
            finPurchase.setTransportgood_id(val.getTransportgood_id());//走货id
            finPurchase.preInsert(tokenModel);
            finPurchaseMapper.insert(finPurchase);
        }
//        finPurchase.setPurchase_id(UUID.randomUUID().toString());
//        finPurchase.setCredential_status("PW001001");
//        finPurchase.preInsert();
//        finPurchaseMapper.insert(finPurchase);
    }


//    public void insertHK(List<Receivablesrecord> receivablesrecords, String contractNumber, String collectionAccount, String saleresponsibility, String country, TokenModel tokenModel) throws Exception {
    public void insertHK(List<Receivablesrecord> receivablesrecords,TransportGood transportGood, TokenModel tokenModel) throws Exception {
        for (Receivablesrecord val :
                receivablesrecords) {
            FinSales finSales = new FinSales();
            finSales.setSales_id(UUID.randomUUID().toString());
            finSales.setContractnumber(transportGood.getContractnumber());
            finSales.setUnitprice(val.getUnitprice());//单价
            finSales.setCurrency(val.getCurrency());//币种
            if(StringUtils.isNotEmpty(finSales.getCurrency())){
                com.nt.dao_Org.Dictionary dictionary = new com.nt.dao_Org.Dictionary();
                dictionary.setCode(finSales.getCurrency());
                List<Dictionary> dir = dictionaryService.getDictionary(dictionary);
                finSales.setEx_rate(dir.get(0).getValue1());//汇率
            }
            finSales.setAmount(val.getNumbers());//数量
            finSales.setUnit(val.getUnit());//单位
            finSales.setFreight(transportGood.getFreight());//运费
            finSales.setPremium(transportGood.getPremium());//保费
            finSales.setCountry(transportGood.getCountry());//国家
            finSales.setCustomer(val.getCustomername());
            finSales.setCollectionaccount(transportGood.getCollectionaccount());
            finSales.setReceamount(val.getReceamount() == null ? "0.00" : val.getReceamount().toString());
            finSales.setReceduedate(val.getReceduedate());
            finSales.setSalesamount(val.getRealamount());
            finSales.setArrivaltime(val.getPaybackdate());
            finSales.setReceivablesrecord_id(val.getReceivablesrecord_id());
            finSales.setCredential_status("PW001001");
            finSales.setArrival_status("0");
            finSales.setSaleresponsibility(transportGood.getSaleresponsibility());//销售负责人
            finSales.setTransportgood_id(val.getTransportgood_id());//走货id
            finSales.setProductus(val.getProductid());//产品id
            finSales.preInsert(tokenModel);
            finSalesMapper.insert(finSales);
        }
//        finSales.setSales_id(UUID.randomUUID().toString());
//        finSales.preInsert();
//        finSales.setArrival_status("0");
//        finSales.setCredential_status("PW001001");
//        String number = contractNumber.getContractNumber("PT001011", "fin_sales");
//        finSales.setCredential_sales(number);
//        finSalesMapper.insert(finSales);
    }

    @Override
    public void paymentCG(List<FinSales> finSales, TokenModel token) {
        for (FinSales finSale :
                finSales) {
            Receivablesrecord receivablesrecord = new Receivablesrecord();
            receivablesrecord.setReceivablesrecord_id(finSale.getReceivablesrecord_id());
            receivablesrecord.setRealamount(finSale.getReceamount());
            receivablesrecord.setPaybackdate(new Date());
            receivablesrecord.setPaybackstatus("PY011002");
            receivablesrecordMapper.updateByPrimaryKeySelective(receivablesrecord);
            finSale.setSalesamount(finSale.getReceamount());
            finSale.setArrivaltime(new Date());
            finSale.setArrival_status("1");
            finSalesMapper.updateByPrimaryKeySelective(finSale);

        }
    }

    @Override
    public void paymentXS(List<FinPurchase> finPurchases, TokenModel token) {
        for (FinPurchase finPurchase :
                finPurchases) {
            Applicationrecord applicationrecord = new Applicationrecord();
            applicationrecord.setApplicationrecord_id(finPurchase.getApplicationrecord_id());
            applicationrecord.setRealamount(finPurchase.getRealpay());
            applicationrecord.setPaiddate(new Date());
            applicationrecord.setPaymentstatus("PY011002");
            applicationrecordMapper.updateByPrimaryKeySelective(applicationrecord);
            finPurchase.setAp_date(new Date());
            finPurchase.setRealamount(finPurchase.getRealpay());
            finPurchase.setPaymentstatus("PY011002");
            finPurchaseMapper.updateByPrimaryKeySelective(finPurchase);
        }
    }

    //生成代办
    @Async
    public void ToDoNotice(TokenModel tokenModel, TransportGood transportGood) throws Exception {
        // 创建代办
        if (transportGood.getType() == 1) {
            List<MembersVo> membersVos = roleService.getMembers("5eba6f09e52fa718db632696");
            for (MembersVo membersVo :
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【采购进货】：您有一条走货单需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("订单号【" + transportGood.getContractnumber() + "】");
                toDoNotice.setDataid(transportGood.getTransportgood_id());
                toDoNotice.setUrl("/AOCHUAN3002FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        } else if (transportGood.getType() == 2) {
            List<MembersVo> membersVos = roleService.getMembers("5eba6f88e52fa718db632697");
            for (MembersVo membersVo :
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【单据填写】：您有一条走货单需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("订单号【" + transportGood.getContractnumber() + "】");
                toDoNotice.setDataid(transportGood.getTransportgood_id());
                toDoNotice.setUrl("/AOCHUAN3002FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }
    }

    //生成代办
    @Async
    public void ToDoNoticeFinance(TokenModel tokenModel, TransportGood transportGood) throws Exception {
        // 创建代办
        if (transportGood.getFinance() == 2) {
            List<MembersVo> membersVos = roleService.getMembers("5eba7094e52fa718db63269c");
            for (MembersVo membersVo :
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【发起请款】：您有一条请款单需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("订单号【" + transportGood.getContractnumber() + "】");
                toDoNotice.setDataid(transportGood.getTransportgood_id());
                toDoNotice.setUrl("/AOCHUAN3002FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        } else if (transportGood.getFinance() == 1) {
            List<MembersVo> membersVos = roleService.getMembers("5eba7094e52fa718db63269c");
            for (MembersVo membersVo :
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【发起回款确认】：您有一条汇款单需要确认。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("订单号【" + transportGood.getContractnumber() + "】");
                toDoNotice.setDataid(transportGood.getTransportgood_id());
                toDoNotice.setUrl("/AOCHUAN3002FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }
    }

    //系统服务
    public void othersToDoNotice() throws Exception {
        List<TransportGood> transportGoods = transportGoodMapper.deliveryTime();
        for (TransportGood transportGood :
                transportGoods) {
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【计划发货提醒】：您有一条走货单已到达发货时间。");
            toDoNotice.setInitiator("5eba6585e52fa718db63268d");
            toDoNotice.setContent("订单号【" + transportGood.getContractnumber() + "】");
            toDoNotice.setDataid(transportGood.getTransportgood_id());
            toDoNotice.setUrl("/AOCHUAN3002FormView");
            //toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(transportGood.getSaleresponsibility());
            toDoNoticeService.save(toDoNotice);
        }
    }

    /**
     * excel导出
     * 1.获取数据集List 插入到map集合中
     * 2.根据模板生成新的excel
     * 3.将新生成的excel文件从浏览器输出
     * 4.删除新生成的模板文件
	 * @return
	 */
    @Override
    public boolean setExport(HttpServletResponse response, List<TransportGood> exportVo) throws Exception {
    	boolean isSuccess = true;

        Map<String, Object> beans = new HashMap();

        //业务逻辑
        beans = logicExport(response, exportVo);
        boolean delete = false;

        //加载excel模板文件
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:excel/goodsdeliverytemplate.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                ClassPathResource cpr = new ClassPathResource("excel/goodsdeliverytemplate.xlsx");
                if (cpr.exists()) {
                    InputStream inputStream = cpr.getInputStream();
                    Date now = new Date();
                    file = File.createTempFile(now.getTime() + "", ".xlsx");
                    try {
                        byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
                        FileCopyUtils.copy(bdata, file);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }
                }
                delete = true;
            } catch (Exception e1) {
				isSuccess = false;
                e1.printStackTrace();
            }
        }

        //配置下载路径
//        String path = "/tmp/";
//        createDir(new File(path));

        //根据模板生成新的excel
		try {
			File excelFile = createNewFile(beans, file);
			//浏览器端下载文件
			try {
				downloadFile(response, excelFile);
			} catch (Exception e ) {}
			//删除服务器生成文件
			deleteFile(excelFile);
		} catch (Exception e ) {
			isSuccess = false;
		}

        if ( delete ) {
            deleteFile(file);
        }

        return isSuccess;
    }

    @Override
    public List<TransportGood> getTransportInfo(String id) throws Exception {
        List<TransportGood> transportGood = new ArrayList<>();
        List<TransportGood> transportGo = new ArrayList<>();
//        TransportGood trans= new TransportGood();
        Saledetails sds = new Saledetails();
        sds.setSupplierid(id);
        List<Saledetails> saledetailsList = saledetailsMapper.select(sds);
        if(saledetailsList.size() >0){
            for(Saledetails s : saledetailsList){
                if(StringUtils.isNotEmpty(s.getTransportgood_id())){
                    TransportGood good =  transportGoodMapper.selectByPrimaryKey(s.getTransportgood_id());
                    if(good != null){
                        transportGood.add(good);
                    }
                }
            }
        }
        if(transportGood.size()>0){
            for (TransportGood item : transportGood) {
                if (transportGo.stream().filter(item2 -> item2.getTransportgood_id().equals(item.getTransportgood_id())).count() == 0) {
                    transportGo.add(item);
                }
            }
        }
        return transportGo;
    }


    /**
     * 业务逻辑把数据存到Map中
     *
     * @param response
     * @param exportVo
     * @return beans
     */

    private Map<String, Object> logicExport(HttpServletResponse response, List<TransportGood> exportVo) {

        Map<String, Object> beans = new HashMap();
        List<SalesExportVo> salesexportList = new ArrayList<>();
        List<PurchaseExportVo> purchaseexportList = new ArrayList<>();
        List<DocumentExportVo> documentexportList = new ArrayList<>();
        //获取系统中所有人的姓名，放到map里
        Map<String, String> users = new HashMap<>();
        Query query = new Query();
        List<CustomerInfo> customerInfo = mongoTemplate.find(query, CustomerInfo.class);
        for(int i = 0; i < customerInfo.size(); i++) {
            String key = customerInfo.get(i).getUserid();
            String value = customerInfo.get(i).getUserinfo().getCustomername();
            users.put(key, value);
        }

        for (int i = 0; i < exportVo.size(); i++) {
            String id = exportVo.get(i).getTransportgood_id();
            List<SalesExportVo> salesexportListBase = applicationrecordMapper.selectExportList(id);//获取销售数据
            List<PurchaseExportVo> purchaseexportListBase = applicationrecordMapper.purchaseexportList(id);//获取采购数据
            List<DocumentExportVo> documentexportListBase = applicationrecordMapper.documentexportList(id);//获取单据数据
            for (int j = 0; j < salesexportListBase.size(); j++) {
                // 销售担当
                if(users.containsKey(salesexportListBase.get(j).getSaleresponsibility())){
                    salesexportListBase.get(j).setSaleresponsibility(users.get(salesexportListBase.get(j).getSaleresponsibility()));
                }
                // 销售合同回签
                if("0".equals(salesexportListBase.get(j).getSignback())){
                    salesexportListBase.get(j).setSignback("否");
                }else {
                    salesexportListBase.get(j).setSignback("是");
                }
            }
            for (int k = 0; k < purchaseexportListBase.size(); k++) {
                // 销售担当
                if(users.containsKey(purchaseexportListBase.get(k).getProductresponsibility())){
                    purchaseexportListBase.get(k).setProductresponsibility(users.get(purchaseexportListBase.get(k).getProductresponsibility()));
                }
                // 危险品
                if("0".equals(purchaseexportListBase.get(k).getDangerous())){
                    purchaseexportListBase.get(k).setDangerous("否");
                }else {
                    purchaseexportListBase.get(k).setDangerous("是");
                }
                // 采购合同回签
                if("0".equals(purchaseexportListBase.get(k).getSignback1())){
                    purchaseexportListBase.get(k).setSignback1("否");
                }else {
                    purchaseexportListBase.get(k).setSignback1("是");
                }
            }
            for (int l = 0; l < documentexportListBase.size(); l++) {
                // 单据担当
                if(users.containsKey(documentexportListBase.get(l).getBillresponsibility())){
                    documentexportListBase.get(l).setBillresponsibility(users.get(documentexportListBase.get(l).getBillresponsibility()));
                }
                // 确认到仓
                if("0".equals(documentexportListBase.get(l).getWarehouse())){
                    documentexportListBase.get(l).setWarehouse("否");
                }else {
                    documentexportListBase.get(l).setWarehouse("是");
                }
            }
            salesexportList.addAll(salesexportListBase);
            purchaseexportList.addAll(purchaseexportListBase);
            documentexportList.addAll(documentexportListBase);
        }
        beans.put("slist", salesexportList);
        beans.put("plist", purchaseexportList);
        beans.put("dlist", documentexportList);

        return beans;
    }

    /**
     * 根据excel模板生成新的excel
     *
     * @param beans
     * @param sourceFile
     * @return
     */
    private File createNewFile(Map<String, Object> beans, File sourceFile) throws Exception {
        XLSTransformer transformer = new MyXLSTransformer();
        File tempFile = File.createTempFile("D3002", ".xlsx");

        //命名
//        String name = "bbb.xlsx";
//        File newFile = new File(path + name);

        try (InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
             OutputStream out = new FileOutputStream(tempFile)) {
            //poi版本使用3.1.7要不然会报错
			// 对应poi4.x 扩展XLSTransformer
			// 需要初始化为com.nt.service_AOCHUAN.AOCHUAN3000.Impl.xlsMyXLSTransformer
            Workbook workbook = transformer.transformXLS(in, beans);
            workbook.write(out);
            out.flush();
            return tempFile;
        } catch (Exception e) {
        	log.error("error download 3002 excel file.", e);
            System.out.println(e.getMessage());
			//删除服务器生成文件
			deleteFile(tempFile);
            throw e;
        }
    }

    /**
     * 将服务器新生成的excel从浏览器下载
     *
     * @param response
     * @param excelFile
     */
    private void downloadFile(HttpServletResponse response, File excelFile) {
        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
        response.setContentType("multipart/form-data");
        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.setHeader("Content-Disposition", "attachment;filename=" + excelFile.getName());
        try (
                InputStream ins = new FileInputStream(excelFile);
                OutputStream os = response.getOutputStream()
        ) {
            byte[] b = new byte[1024];
            int len;
            while ((len = ins.read(b)) > 0) {
                os.write(b, 0, len);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 浏览器下载完成之后删除服务器生成的文件
     * 也可以设置定时任务去删除服务器文件
     *
     * @param excelFile
     */
    private void deleteFile(File excelFile) {
        excelFile.delete();
    }

    //如果目录不存在创建目录 存在则不创建
    private void createDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
