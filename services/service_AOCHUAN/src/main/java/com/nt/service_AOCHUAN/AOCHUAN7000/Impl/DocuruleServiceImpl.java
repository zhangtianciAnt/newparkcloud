package com.nt.service_AOCHUAN.AOCHUAN7000.Impl;


import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.CreruleMapper;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.DocuruleMapper;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.HelpruleMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocuruleServiceImpl  implements DocuruleService {
    @Autowired
    private DocuruleMapper docuruleMapper;

    @Autowired
    private CreruleMapper creruleMapper;

    @Autowired
    private HelpruleMapper helpruleMapper;

    @Override
    public List<Docurule> get(Docurule docurule) throws Exception {
        return docuruleMapper.selectAll();
    }

    @Override
    public DocuruleVo One(String docurule_id) throws Exception {
        DocuruleVo docuruleVo=new DocuruleVo();
        Crerule crerul=new Crerule();
        crerul.setDocurule_id(docurule_id);
        List<Crerule> creruleList=creruleMapper.select(crerul);
        creruleList=creruleList.stream().sorted(Comparator.comparing(Crerule::getCrerule_id)).collect(Collectors.toList());
        Docurule docurule=docuruleMapper.selectByPrimaryKey(docurule_id);
        docuruleVo.setDocurule(docurule);
        docuruleVo.setCrerules(creruleList);
        return docuruleVo;
    }
    @Override
    public List<Helprule> helpOne(String docurule_id) throws Exception {

        return helpruleMapper.helpOne(docurule_id);
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
        docurule = docuruleVo.getDocurule();
        BeanUtils.copyProperties(docuruleVo.getDocurule(),docurule);
        docurule.preInsert(tokenModel);
        docurule.setDocurule_id(docuruleid);
      docuruleMapper.insertSelective(docurule);
       List<Crerule> crulelist=docuruleVo.getCrerules();
        Helprule helprule=new Helprule();
        List<Helprule> helpruleList=docuruleVo.getHelprules();
        if (crulelist != null) {
            for ( int i = 0;i< crulelist.size();i++) {
                crulelist.get(i).preInsert(tokenModel);
                crulelist.get(i).setCrerule_id(UUID.randomUUID().toString());
                helpruleList.get(i).setCrerule_id(crulelist.get(i).getCrerule_id());
                helpruleList.get(i).setHelprule_id(UUID.randomUUID().toString());
                crulelist.get(i).setDocurule_id(docuruleid);
                helpruleMapper.insertSelective(helpruleList.get(i));
                creruleMapper.insertSelective(crulelist.get(i));
            }
        }

    }

    @Override
    public void delete(String id) throws Exception {
   docuruleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Docurule selectByDocutype(String docutype) {
        return docuruleMapper.selectByDocutype(docutype);
    }

}
