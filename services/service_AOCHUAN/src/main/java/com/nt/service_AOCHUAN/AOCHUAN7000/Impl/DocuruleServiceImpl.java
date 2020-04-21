package com.nt.service_AOCHUAN.AOCHUAN7000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.CreruleMapper;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.DocuruleMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocuruleServiceImpl  implements DocuruleService {
    @Autowired
    private DocuruleMapper docuruleMapper;

    @Autowired
    private CreruleMapper creruleMapper;

    @Override
    public List<Docurule> get(Docurule docurule) throws Exception {
        return docuruleMapper.selectAll();
    }

    @Override
    public Docurule One(String id) throws Exception {
        return docuruleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception {
        Docurule docurule=new Docurule();
        BeanUtils.copyProperties(docuruleVo.getDocurule(),docurule);
        docurule.preUpdate(tokenModel);
       docuruleMapper.updateByPrimaryKey(docurule);
       String docuid=docurule.getDocurule_id();
       Crerule crerul=new Crerule();
       crerul.setDocurule_id(docuid);
       creruleMapper.delete(crerul);
       List<Crerule> crerules=docuruleVo.getCrerules();
        if (crerules != null) {
            int rowundex = 0;
            for (Crerule crerule : crerules) {
                rowundex = rowundex + 1;
                crerule.preInsert(tokenModel);
                crerule.setCrerule_id(UUID.randomUUID().toString());
                crerule.setDocurule_id(docuid);
                creruleMapper.insertSelective(crerule);
            }
        }

    }

    @Override
    public void insert(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception {
        String docuruleid= UUID.randomUUID().toString();
        Docurule docurule=new Docurule();
        BeanUtils.copyProperties(docuruleVo.getDocurule(),docurule);
        docurule.setDocurule_id(docuruleid);
        docurule.preInsert(tokenModel);
        docurule.setDocurule_id(docuruleid);
       docuruleMapper.insertSelective(docurule);
       List<Crerule> crulelist=docuruleVo.getCrerules();
        if (crulelist != null) {
            int rowundex = 0;
            for (Crerule crerule  : crulelist) {
                rowundex = rowundex + 1;
                crerule.preInsert(tokenModel);
                crerule.setCrerule_id(UUID.randomUUID().toString());
                crerule.setDocurule_id(docuruleid);
                creruleMapper.insertSelective(crerule);
            }
        }

    }

    @Override
    public void delete(String id) throws Exception {
   docuruleMapper.deleteByPrimaryKey(id);
    }
}
