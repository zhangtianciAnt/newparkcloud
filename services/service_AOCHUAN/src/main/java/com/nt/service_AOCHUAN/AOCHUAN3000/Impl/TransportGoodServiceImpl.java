package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.alibaba.fastjson.JSON;
import com.nt.dao_AOCHUAN.AOCHUAN3000.*;
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
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
        TransportGood transportGood =  transportGoodMapper.selectByPrimaryKey(id);
        Saledetails saledetails = new Saledetails();
        saledetails.setTransportgood_id(id);
        List<Saledetails> saledetailsList = saledetailsMapper.select(saledetails);
        if(saledetailsList.size() > 0){
            transportGood.setSaledetails(saledetailsList);
        }
        Receivablesrecord receivablesrecord = new Receivablesrecord();
        receivablesrecord.setTransportgood_id(id);
        List<Receivablesrecord> receivablesrecords = receivablesrecordMapper.select(receivablesrecord);
        if(receivablesrecords.size() > 0){
            transportGood.setReceivablesrecord(receivablesrecords);
        }
        Applicationrecord applicationrecord = new Applicationrecord();
        applicationrecord.setTransportgood_id(id);
        List<Applicationrecord> applicationrecords = applicationrecordMapper.select(applicationrecord);
        if(applicationrecords.size() > 0){
            transportGood.setApplicationrecord(applicationrecords);
        }
        return  transportGood;
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
        transportGoodMapper.updateByPrimaryKeySelective(transportGood);
        String id = transportGood.getTransportgood_id();
        if(transportGood.getFinance() == 0){
            DeleteSonTable(id);
            InsertSonTable(transportGood,id);
        }
        if (transportGood.isNotice()) {
            ToDoNotice(tokenModel, transportGood);
        }
        ToDoNoticeFinance(tokenModel, transportGood);
    }

    @Override
    public void insert(TransportGood transportGood, TokenModel tokenModel) throws Exception {
       // String number = contractNumber.getContractNumber("PT001010", "transportgood");
        String id = UUID.randomUUID().toString();
        //transportGood.setContractnumber(number);
        transportGood.setTransportgood_id(id);
        transportGood.preInsert(tokenModel);
        transportGoodMapper.insert(transportGood);
        InsertSonTable(transportGood,id);
    }

    @Override
    public void delete(String id) throws Exception {
        transportGoodMapper.deleteByPrimaryKey(id);
        DeleteSonTable(id);
    }

    private void InsertSonTable(TransportGood transportGood,String id){
        if(transportGood.getSaledetails().size() > 0){
            List<Saledetails> saledetailsList = transportGood.getSaledetails();
            for (Saledetails val:saledetailsList) {
                val.setTransportgood_id(id);
                val.setSaledetails_id(UUID.randomUUID().toString());
            }
            saledetailsMapper.insertSaledetailsList(saledetailsList);
        }

        if(transportGood.getReceivablesrecord().size() > 0){
            List<Receivablesrecord> receivablesrecords = transportGood.getReceivablesrecord();
            for (Receivablesrecord val:receivablesrecords) {
                val.setTransportgood_id(id);
                val.setReceivablesrecord_id(UUID.randomUUID().toString());
            }
            receivablesrecordMapper.insertReceivablesrecordList(receivablesrecords);
        }

        if(transportGood.getApplicationrecord().size() > 0){
            List<Applicationrecord> applicationrecords = transportGood.getApplicationrecord();
            for (Applicationrecord val:applicationrecords) {
                val.setTransportgood_id(id);
                val.setApplicationrecord_id(UUID.randomUUID().toString());
            }
            applicationrecordMapper.insertApplicationrecordList(applicationrecords);
        }
    }

    private void DeleteSonTable(String id){
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
    @Override
    public void insertCW(TransportGood transportGood, TokenModel token) {
        List<Applicationrecord> applicationrecords = transportGood.getApplicationrecord();
        for (Applicationrecord val:
        applicationrecords) {
            FinPurchase finPurchase = new FinPurchase();
            finPurchase.setPurchase_id(UUID.randomUUID().toString());
            finPurchase.setContractnumber(transportGood.getContractnumber());
            finPurchase.setSupplier(val.getSuppliername());
            finPurchase.setPaymenttime(val.getRealdate());
            finPurchase.setInvoicenumber(val.getInvoiceno());
            finPurchase.setCredential_status("PW001001");
            finPurchase.setPaymentaccount("");
            finPurchase.setRealpay(val.getRealpay() == null ?"0.00":val.getRealpay().toString());
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

    @Override
    public void insertHK(TransportGood transportGood, TokenModel token) throws Exception {
        List<Receivablesrecord> receivablesrecords = transportGood.getReceivablesrecord();
        for (Receivablesrecord val:
        receivablesrecords) {
            FinSales finSales = new FinSales();
            finSales.setSales_id(UUID.randomUUID().toString());
            finSales.setContractnumber(transportGood.getContractnumber());
            finSales.setCustomer(val.getCustomername());
            finSales.setCollectionaccount(transportGood.getCollectionaccount());
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
        for (FinSales finSale:
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
        for (FinPurchase finPurchase:
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
}
