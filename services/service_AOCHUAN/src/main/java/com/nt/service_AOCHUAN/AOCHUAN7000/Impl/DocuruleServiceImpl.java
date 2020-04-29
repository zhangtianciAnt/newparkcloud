package com.nt.service_AOCHUAN.AOCHUAN7000.Impl;


import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.*;
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

    @Autowired
    private VoucherRulesMapper voucherRuiesMapper;

    @Autowired
    private EntryRulesMapper entryRulesMapper;

    @Autowired
    private AuxiliaryAccountingItemsMapper auxiliaryAccountingItemsMapper;

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
        String[] crerulesid = new String[crerules.size()];
        if (crerules != null) {
            int rowundex = 0;
            for (int i =0 ; i< crerules.size() ; i++) {
                String id = UUID.randomUUID().toString();
                rowundex = rowundex + 1;
                crerules.get(i).preUpdate(tokenModel);
                crerules.get(i).setCrerule_id(id);
                crerules.get(i).setDocurule_fid(docuid);
                creruleMapper.insertSelective(crerules.get(i));
                helprules.get(i).preUpdate(tokenModel);
                helprules.get(i).setHelprule_id(UUID.randomUUID().toString());
                helprules.get(i).setCrerule_wid(docuid);
                helprules.get(i).setCrerule_fid(id);
                helpruleMapper.insertSelective(helprules.get(i));
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
                helpruleList.get(i).preInsert(tokenModel);
                helpruleList.get(i).setCrerule_fid(crulelist.get(i).getCrerule_id());
                helpruleList.get(i).setHelprule_id(UUID.randomUUID().toString());
                helpruleList.get(i).setStatus("0");
                helpruleList.get(i).setCrerule_wid(docuruleid);
                crulelist.get(i).setStatus("0");
                crulelist.get(i).setDocurule_fid(docuruleid);
                helpruleMapper.insertSelective(helpruleList.get(i));
                creruleMapper.insertSelective(crulelist.get(i));
            }
        }

    }

    @Override
    public void delCrerule(String helprule_id) throws Exception {

    }

    //删除
    @Override
    public Boolean delete(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception {

        Docurule docurule  = docuruleVo.getDocurule();

        List<Crerule> crerules = docuruleVo.getCrerules();

        List<Helprule> helprules = docuruleVo.getHelprules() ;

        //凭证规则
        voucherRuiesMapper.delDocuruleid(docurule.getModifyby(), docurule.getDocurule_id());

        if(crerules.isEmpty()){

        }else{

            //分录规则
            for (Crerule item : crerules) {
                if (existCheckAcc(item.getDocurule_fid())) {
                    item.preUpdate(tokenModel);
                    entryRulesMapper.delCreruleid(item.getModifyby(), item.getDocurule_fid());
                }
            }
        }

        if(helprules.isEmpty()){
        }else{
            //辅助核算项目
            for (Helprule item : helprules) {
                if (existCheckAch(item.getCrerule_wid())) {
                    item.preUpdate(tokenModel);
                    auxiliaryAccountingItemsMapper.delHelpruleid(item.getModifyby(), item.getCrerule_wid());
                }
            }
        }
        return true;
    }

    @Override
    public Docurule selectByDocutype(String docutype) {
        return docuruleMapper.selectByDocutype(docutype);
    }

    @Override
    public List<All> selectrule(String docurule_id) throws Exception {
        return allMapper.selectrule(docurule_id);
    }

    public Boolean existCheckAux(String id) throws Exception {
        int count = voucherRuiesMapper.existCheckAux(id);
        if (count == 0) {
            return false;
        }
        return true;
    }

    public Boolean existCheckAcc(String id) throws Exception {
        int count = entryRulesMapper.existCheckAcc(id);
        if (count == 0) {
            return false;
        }
        return true;
    }

    public Boolean existCheckAch(String id) throws Exception {
        int count = auxiliaryAccountingItemsMapper.existCheckAch(id);
        if (count == 0) {
            return false;
        }
        return true;
    }
}
