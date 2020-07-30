package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Pfans.PFANS1000.Fixedassets;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_pfans.PFANS1000.FixedassetsService;
import com.nt.service_pfans.PFANS1000.mapper.FixedassetsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class FixedassetsServiceImpl  implements FixedassetsService {

    @Autowired
    private FixedassetsMapper fixedassetsMapper;

    @Autowired
    private AssetsMapper assetsMapper;

    @Override
    public List<Fixedassets> getFixedassets(Fixedassets fixedassets) {
        return fixedassetsMapper.select(fixedassets);
    }

    @Override
    public Fixedassets One(String fixedassets_id) throws Exception {
        return fixedassetsMapper.selectByPrimaryKey(fixedassets_id);
    }

    @Override
    public void updateFixedassets(Fixedassets fixedassets, TokenModel tokenModel) throws Exception {
        fixedassets.preUpdate(tokenModel);
        if(fixedassets.getStatus().equals("4")){
            Assets assets = new Assets();
            assets.setBarcode(fixedassets.getRfid());
            List<Assets> assetsList = assetsMapper.select(assets);
            for(Assets ast : assetsList){
//                2020-07-13 ~ 2020-07-25
                ast.setPsdcdbringoutreason(fixedassets.getObjective());
                ast.setPsdcdperiod(fixedassets.getRepair().substring(0,10));
                ast.setPsdcdreturndate(fixedassets.getRepair().substring(fixedassets.getRepair().length() - 10));
                assetsMapper.updateByPrimaryKeySelective(ast);
            }
        }
        fixedassetsMapper.updateByPrimaryKey(fixedassets);
    }

    @Override
    public void insert(Fixedassets fixedassets, TokenModel tokenModel) throws Exception {
        fixedassets.preInsert(tokenModel);
        fixedassets.setFixedassets_id(UUID.randomUUID().toString()) ;
        fixedassetsMapper.insert(fixedassets);
    }

}
