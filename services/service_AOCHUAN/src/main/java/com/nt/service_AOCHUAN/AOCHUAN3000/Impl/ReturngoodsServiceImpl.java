package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Returngoods;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.ReturngoodsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ReturngoodsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.TransportGoodMapper;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ReturngoodsServiceImpl implements ReturngoodsService {

    @Autowired
    private ReturngoodsMapper returngoodsMapper;

    @Autowired
    private TransportGoodMapper transportGoodMapper;


    @Autowired
    private RoleService roleService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;


    @Override
    public List<Returngoods> get(Returngoods returngoods) throws Exception {
        return returngoodsMapper.select(returngoods);
    }

    @Override
    public void insert(Returngoods returngoods, TokenModel tokenModel) throws Exception {
        returngoods.preInsert(tokenModel);
        returngoods.setReturngoods_id(UUID.randomUUID().toString());
        if(returngoods.getType() == null){
            returngoods.setType(0);
        }
        if(returngoods.isNotice()){
            ToDoNotice(tokenModel,returngoods);
        }
        returngoodsMapper.insert(returngoods);
    }

    @Override
    public Returngoods One(String ids) throws Exception {
        return returngoodsMapper.selectByPrimaryKey(ids);
    }

    @Override
    public void update(Returngoods returngoods, TokenModel tokenModel) throws Exception {
        returngoods.preUpdate(tokenModel);
        if(returngoods.isNotice()){
            ToDoNotice(tokenModel,returngoods);
//            returngoods.setType(returngoods.getType() + 1);
        }
        returngoodsMapper.updateByPrimaryKey(returngoods);

    }

    @Override
    public void delete(String id) throws Exception {
        Returngoods returngoods = new Returngoods();
        returngoods.setReturngoods_id(id);
        returngoods.setStatus("1");
        returngoodsMapper.updateByPrimaryKey(returngoods);

    }

    //生成代办
    @Async
    public void ToDoNotice(TokenModel tokenModel, Returngoods returngoods) throws Exception{
        // 创建代办
        if(returngoods.getType() == 0){
            List<MembersVo> membersVos =  roleService.getMembers("5e96adfa96c5744860b31a00");
            for (MembersVo membersVo:
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【退货】：您有一条退货消息处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("合同号【" +returngoods.getContractno()+"】");
                toDoNotice.setDataid(returngoods.getReturngoods_id());
                toDoNotice.setUrl("/AOCHUAN3005FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }else if(returngoods.getType() == 1){
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【退货】：您有一条采购消息处理。");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("合同号【" +returngoods.getContractno()+"】");
            toDoNotice.setDataid(returngoods.getReturngoods_id());
            toDoNotice.setUrl("/AOCHUAN3005FormView");
            toDoNotice.preInsert(tokenModel);

            TransportGood transportGood = new TransportGood();
            transportGood.setContractnumber(returngoods.getContractno());
            List<TransportGood> list = transportGoodMapper.select(transportGood);
            for(TransportGood tr : list){
                toDoNotice.setOwner(tr.getSaleresponsibility());//发送给采购
            }

//            toDoNotice.setOwner("5e956171e52fa71970c1a097");//发送给采购
            toDoNoticeService.save(toDoNotice);
        }
        else if(returngoods.getType() == 2){
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【退货】：您有一条单据消息处理。");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("合同号【" +returngoods.getContractno()+"】");
            toDoNotice.setDataid(returngoods.getReturngoods_id());
            toDoNotice.setUrl("/AOCHUAN3005FormView");
            toDoNotice.preInsert(tokenModel);
            TransportGood transportGood = new TransportGood();
            transportGood.setContractnumber(returngoods.getContractno());
            List<TransportGood> list = transportGoodMapper.select(transportGood);
            for(TransportGood tr : list){
                toDoNotice.setOwner(tr.getBillresponsibility());//发送给单据
            }

//            toDoNotice.setOwner("5e956171e52fa71970c1a097");//发送给单据
            toDoNoticeService.save(toDoNotice);
        }

    }
}
