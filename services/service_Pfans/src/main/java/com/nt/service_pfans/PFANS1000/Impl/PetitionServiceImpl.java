package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.service_pfans.PFANS1000.PetitionService;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PetitionServiceImpl implements PetitionService {

    @Autowired
    private PetitionMapper petitionMapper;

    @Override
    public List<Petition> get(Petition petition) throws Exception {
        return petitionMapper.select(petition);
    }

    @Override
    public Petition one(String petition_id) throws Exception {
       if(petition_id.equals("")){
           return null;
       }
       return petitionMapper.selectByPrimaryKey(petition_id);
    }

    @Override
    public void update(Petition petition, TokenModel tokenModel) throws Exception {
        petitionMapper.updateByPrimaryKeySelective(petition);

    }

}
