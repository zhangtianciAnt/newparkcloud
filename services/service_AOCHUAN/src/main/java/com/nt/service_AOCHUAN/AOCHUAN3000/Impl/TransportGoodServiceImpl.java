package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.alibaba.fastjson.JSON;
import com.nt.dao_AOCHUAN.AOCHUAN3000.*;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ExportVo;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.TransportGoodService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.*;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinPurchaseMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinSalesMapper;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
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
    private SaledetailsMapper saledetailsMapper;

    @Autowired
    private ReceivablesrecordMapper receivablesrecordMapper;

    @Autowired
    private ApplicationrecordMapper applicationrecordMapper;

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
        DeleteSonTable(id);
        InsertSonTable(transportGood, id);
        if (transportGood.isNotice()) {
            ToDoNotice(tokenModel, transportGood);
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
        InsertSonTable(transportGood, id);
    }

    @Override
    public void delete(String id) throws Exception {
        transportGoodMapper.deleteByPrimaryKey(id);
        DeleteSonTable(id);
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


    private void InsertSonTable(TransportGood transportGood, String id) throws Exception {
        if (transportGood.getSaledetails().size() > 0) {
            List<Saledetails> saledetailsList = transportGood.getSaledetails();
            for (Saledetails val : saledetailsList) {
                val.setTransportgood_id(id);
                val.setSaledetails_id(UUID.randomUUID().toString());
            }
            saledetailsMapper.insertSaledetailsList(saledetailsList);
        }

        if (transportGood.getReceivablesrecord().size() > 0) {
            List<Receivablesrecord> receivablesrecords = transportGood.getReceivablesrecord();
            for (Receivablesrecord val : receivablesrecords) {
                val.setTransportgood_id(id);
                val.setReceivablesrecord_id(UUID.randomUUID().toString());
            }
            if (transportGood.getFinance() == 2) {
                insertHK(receivablesrecords, transportGood.getContractnumber(), transportGood.getCollectionaccount());
            }
            receivablesrecordMapper.insertReceivablesrecordList(receivablesrecords);
        }

        if (transportGood.getApplicationrecord().size() > 0) {
            List<Applicationrecord> applicationrecords = transportGood.getApplicationrecord();
            for (Applicationrecord val : applicationrecords) {
                val.setTransportgood_id(id);
                val.setApplicationrecord_id(UUID.randomUUID().toString());
            }
            if (transportGood.getFinance() == 1) {
                insertCW(applicationrecords, transportGood.getContractnumber());
            }
            applicationrecordMapper.insertApplicationrecordList(applicationrecords);
        }
    }

    private void DeleteSonTable(String id) {
        Saledetails saledetails = new Saledetails();
        saledetails.setTransportgood_id(id);
        saledetailsMapper.delete(saledetails);
        Receivablesrecord receivablesrecord = new Receivablesrecord();
        receivablesrecord.setTransportgood_id(id);
        receivablesrecordMapper.delete(receivablesrecord);
        Applicationrecord applicationrecord = new Applicationrecord();
        applicationrecord.setTransportgood_id(id);
        applicationrecordMapper.delete(applicationrecord);
    }


    public void insertCW(List<Applicationrecord> applicationrecords, String contractNumber) {
        for (Applicationrecord val :
                applicationrecords) {
            FinPurchase finPurchase = new FinPurchase();
            finPurchase.setPurchase_id(UUID.randomUUID().toString());
            finPurchase.setContractnumber(contractNumber);
            finPurchase.setSupplier(val.getSuppliername());
            finPurchase.setPaymenttime(val.getRealdate());
            finPurchase.setInvoicenumber(val.getInvoiceno());
            finPurchase.setCredential_status("PW001001");
            finPurchase.setPaymentaccount("");
            finPurchase.setRealpay(val.getRealpay() == null ? "0.00" : val.getRealpay().toString());
            finPurchase.setRealamount(val.getRealamount());
            finPurchase.setApplicationrecord_id(val.getApplicationrecord_id());
            finPurchase.preInsert();
            finPurchaseMapper.insert(finPurchase);
        }
//        finPurchase.setPurchase_id(UUID.randomUUID().toString());
//        finPurchase.setCredential_status("PW001001");
//        finPurchase.preInsert();
//        finPurchaseMapper.insert(finPurchase);
    }


    public void insertHK(List<Receivablesrecord> receivablesrecords, String contractNumber, String collectionAccount) throws Exception {
        for (Receivablesrecord val :
                receivablesrecords) {
            FinSales finSales = new FinSales();
            finSales.setSales_id(UUID.randomUUID().toString());
            finSales.setContractnumber(contractNumber);
            finSales.setCustomer(val.getCustomername());
            finSales.setCollectionaccount(collectionAccount);
            finSales.setReceamount(val.getReceamount() == null ? "0.00" : val.getReceamount().toString());
            finSales.setReceduedate(val.getReceduedate());
            finSales.setSalesamount(val.getRealamount());
            finSales.setArrivaltime(val.getPaybackdate());
            finSales.setReceivablesrecord_id(val.getReceivablesrecord_id());
            finSales.setCredential_status("PW001001");
            finSales.setArrival_status("0");
            finSales.preInsert();
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
            finPurchase.setPaymentstatus("1");
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

    @Override
    public void setExport(String a) throws Exception {
        try {
            //List<MyTest> exportList = myTestService.findAll(); //获取列表数据
            ExportVo exprot = new ExportVo();
            List<ExportVo> exportList = applicationrecordMapper.selectExportList("TRANSPORTGOODXXXXXX2XXXXXXXXX3000001");//获取列表数据
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("excel/deliverytemplate.xls");   //得到文档的路径
            //列表数据将存储到指定的excel文件路径，这个路径是在项目编译之后的target目录下
            FileOutputStream out = new FileOutputStream("target/classes/excel/qqq.xls");
            //这里的context是jxls框架上的context内容
            org.jxls.common.Context context = new org.jxls.common.Context();
            //将列表参数放入context中
//            context.putVar("exportList", exportList);
            //将List<Exam>列表数据按照模板文件中的格式生成到scoreOutput.xls文件中
            JxlsHelper.getInstance().processTemplate(in, out, context);
//            //下面步骤为浏览器下载部分
//            //指定数据生成后的文件输入流（将上述out的路径作为文件的输入流）
//            FileInputStream fileInputStream = new FileInputStream("target/classes/excel/aaaa.xls");
//            //导出excel文件，设置文件名
//            String filename = URLEncoder.encode("test信息.xls", "UTF-8");
//            //设置下载头
//            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
//            ServletOutputStream outputStream = response.getOutputStream();
//            //将文件写入浏览器
//            byte[] bys = new byte[fileInputStream.available()];
//            fileInputStream.read(bys);
//            outputStream.write(bys);
//            outputStream.flush();
//            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
