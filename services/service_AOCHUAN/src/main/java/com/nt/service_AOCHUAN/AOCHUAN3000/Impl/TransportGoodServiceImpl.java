package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.TransportGoodService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.TransportGoodMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinPurchaseMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinSalesMapper;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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


    @Override
    public List<TransportGood> get() throws Exception {
        return transportGoodMapper.selectAll();
    }

    @Override
    public TransportGood getOne(String id) throws Exception {
        return transportGoodMapper.selectByPrimaryKey(id);
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
        if(transportGood.isNotice()){
            ToDoNotice(tokenModel,transportGood);
        }
    }

    @Override
    public void insert(TransportGood transportGood, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        transportGood.setTransportgood_id(id);
        transportGood.preInsert(tokenModel);
        transportGoodMapper.insert(transportGood);
    }

    @Override
    public void delete(String id) throws Exception {
        transportGoodMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertCW(FinPurchase finPurchase, TokenModel token) {
        finPurchase.setPurchase_id(UUID.randomUUID().toString());
        finPurchase.setCredential_status("PW001001");
        finPurchase.preInsert();
        finPurchaseMapper.insert(finPurchase);
    }

    @Override
    public void insertHK(FinSales finSales, TokenModel token) throws Exception {
        finSales.setSales_id(UUID.randomUUID().toString());
        finSales.preInsert();
        finSales.setArrival_status("0");
        finSales.setCredential_status("PW001001");
        String number = contractNumber.getContractNumber("PT001011","finSales");
        finSales.setCredential_sales(number);
        finSalesMapper.insert(finSales);
    }

    //生成代办
    @Async
    public void ToDoNotice(TokenModel tokenModel,TransportGood transportGood) throws Exception{
        // 创建代办
        if(transportGood.getType() == 1){
            List<MembersVo> membersVos =  roleService.getMembers("5eba6f09e52fa718db632696");
            for (MembersVo membersVo:
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【采购进货】：您有一条走货单需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("订单号【" +transportGood.getContractnumber()+"】");
                toDoNotice.setDataid(transportGood.getTransportgood_id());
                toDoNotice.setUrl("/AOCHUAN3002FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }else if(transportGood.getType() == 2){
            List<MembersVo> membersVos =  roleService.getMembers("5eba6f88e52fa718db632697");
            for (MembersVo membersVo:
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
}
