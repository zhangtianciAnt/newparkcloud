package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Leaveoffice;
import com.nt.service_pfans.PFANS2000.LeaveofficeService;
import com.nt.service_pfans.PFANS2000.mapper.LeaveofficeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class LeaveofficeServiceImpl  implements LeaveofficeService {

    @Autowired
    private LeaveofficeMapper leaveofficeMapper;

    @Override
        //列表查询
   public  List<Leaveoffice> get(Leaveoffice leaveoffice) throws Exception{
        return leaveofficeMapper.select(leaveoffice);
    }

    //新建
    @Override
    public void create(Leaveoffice leaveoffice, TokenModel tokenModel) throws Exception {
        if(!(leaveoffice.equals(null) || leaveoffice.equals(""))){
            leaveoffice.preInsert(tokenModel);
            leaveoffice.setLeaveoffice_id(UUID.randomUUID().toString());
            leaveofficeMapper.insertSelective(leaveoffice);
        }

    }


    //编辑
    @Override

    public void update(Leaveoffice leaveoffice,TokenModel tokenModel)throws Exception{
        leaveofficeMapper.updateByPrimaryKey(leaveoffice);
    }

    //按id查询
    @Override

  public   Leaveoffice   one(String leaveoffice_id) throws Exception{
       if(leaveoffice_id.equals("")){
           return null;
       }
       return leaveofficeMapper.selectByPrimaryKey(leaveoffice_id);
    }
}
