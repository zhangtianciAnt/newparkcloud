package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.SampleService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.SampleMapper;
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
public class SampleServiceImpl implements SampleService {
    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private ContractNumber contractNumber;

    @Autowired
    private RoleService roleService;

    @Override
    public List<Sample> get(Sample sample) throws Exception {
        return sampleMapper.select(sample);
    }

    @Override
    public Sample getOne(String id) throws Exception {
        return sampleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sample> getForSupplier(String id) throws Exception {
        return sampleMapper.getForSupplier(id);
    }

    @Override
    public List<Sample> getForCustomer(String id) throws Exception {
        return sampleMapper.getForCustomer(id);
    }

    @Override
    public void update(Sample sample, TokenModel tokenModel) throws Exception {
        sample.preUpdate(tokenModel);
        if(sample.isNotice()){
            sample.setType(sample.getType() + 1);
            ToDoNotice(tokenModel,sample);
        }
        sampleMapper.updateByPrimaryKeySelective(sample);
    }

    @Override
    public void insert(Sample sample, TokenModel tokenModel) throws Exception {
        sample.setSample_id(UUID.randomUUID().toString());
        String number = contractNumber.getContractNumber("PT001009","sample");
        sample.setSampleorder(number);
        sample.preInsert(tokenModel);
        if(sample.isNotice()){
            sample.setType(1);
            ToDoNotice(tokenModel,sample);
        }
        sampleMapper.insert(sample);
    }

    @Override
    public void delete(String id) throws Exception {
        sampleMapper.deleteByPrimaryKey(id);
    }


    //生成代办
    @Async
    public void ToDoNotice(TokenModel tokenModel,Sample sample) throws Exception{
        // 创建代办
        if(sample.getType() == 1){
            List<MembersVo> membersVos =  roleService.getMembers("5eba6f09e52fa718db632696");
            for (MembersVo membersVo:
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【采购样品】：您有一条样品需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("样品单号【" +sample.getSampleorder()+"】");
                toDoNotice.setDataid(sample.getSample_id());
                toDoNotice.setUrl("/AOCHUAN3007FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }else if(sample.getType() == 2){
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【销售样品】：您有一条样品需要处理。");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("样品单号【" +sample.getSampleorder()+"】");
            toDoNotice.setDataid(sample.getSample_id());
            toDoNotice.setUrl("/AOCHUAN3007FormView");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(sample.getSaleresponsibility());
            toDoNoticeService.save(toDoNotice);
        }

    }
}
