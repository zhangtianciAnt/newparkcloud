package com.nt.service_AOCHUAN.AOCHUAN7000.Impl;


import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.AllMapper;
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

    @Autowired
    private AllMapper allMapper;

    @Override
    public List<Docurule> get(Docurule docurule) throws Exception {
        return docuruleMapper.select(docurule);
    }


    @Override
    public DocuruleVo One(String docurule_id) throws Exception {
        DocuruleVo docuruleVo=new DocuruleVo();
        Crerule crerul=new Crerule();
        crerul.setDocurule_fid(docurule_id);
        List<Crerule> creruleList=creruleMapper.select(crerul);
        creruleList=creruleList.stream().sorted(Comparator.comparing(Crerule::getCrerule_id)).collect(Collectors.toList());
        Docurule docurule=docuruleMapper.selectByPrimaryKey(docurule_id);
        docuruleVo.setDocurule(docurule);
        docuruleVo.setCrerules(creruleList);
        return docuruleVo;
    }

//    @Override
//    public void updateDo(Docurule docurule, TokenModel tokenModel) throws Exception {
//        docurule.preUpdate(tokenModel);
//        docuruleMapper.updateByPrimaryKey(docurule);
//    }
//
//    @Override
//    public void updateCr(Crerule crerule, TokenModel tokenModel) throws Exception {
//   crerule.preUpdate(tokenModel);
//   creruleMapper.updateByPrimaryKey(crerule);
//    }
//
//    @Override
//    public void updateHe(Helprule helprule, TokenModel tokenModel) throws Exception {
//helprule.preUpdate(tokenModel);
//helpruleMapper.updateByPrimaryKey(helprule);
//    }



    @Override
    public void update(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception {
        Docurule docurule=new Docurule();
        BeanUtils.copyProperties(docurule,docuruleVo.getDocurule());
        docurule.preUpdate(tokenModel);
       docuruleMapper.updateByPrimaryKey(docurule);

       String docuid=docurule.getDocurule_id();
       Crerule crerul=new Crerule();
       crerul.setDocurule_fid(docuid);


      Helprule help1=new Helprule();
      help1.setCrerule_wid(docuid);

       creruleMapper.delete(crerul);
       List<Crerule> crerules=docuruleVo.getCrerules();
       helpruleMapper.delete(help1);
      List<Helprule> helprules=docuruleVo.getHelprules();
        if (crerules != null) {
            int rowundex = 0;
            for (Crerule crerule : crerules) {
                String id = UUID.randomUUID().toString();
                rowundex = rowundex + 1;
                crerule.preInsert(tokenModel);
                crerule.setCrerule_id(id);
                crerule.setDocurule_fid(docuid);
                creruleMapper.insertSelective(crerule);
        if (helprules != null) {
            for (Helprule helprule : helprules) {
                rowundex = rowundex + 1;
                helprule.preInsert(tokenModel);
                helprule.setHelprule_id(UUID.randomUUID().toString());
                helprule.setCrerule_wid(docuid);
                helprule.setCrerule_fid(id);
                helpruleMapper.insertSelective(helprule);
            }
        }
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
                helpruleList.get(i).setCrerule_fid(crulelist.get(i).getCrerule_id());
                helpruleList.get(i).setHelprule_id(UUID.randomUUID().toString());
                crulelist.get(i).setDocurule_fid(docuruleid);
                helpruleMapper.insertSelective(helpruleList.get(i));
                creruleMapper.insertSelective(crulelist.get(i));
            }
        }

    }

    @Override
    public void delCrerule(String helprule_id) throws Exception {

        helpruleMapper.delCrerule(helprule_id);
    }

    @Override
    public Docurule selectByDocutype(String docutype) {
        return docuruleMapper.selectByDocutype(docutype);
    }

    @Override
    public List<All> selectrule(String docurule_id) throws Exception {
        return allMapper.selectrule(docurule_id);
    }








}
