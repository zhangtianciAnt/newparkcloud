package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Citation;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
import com.nt.service_pfans.PFANS2000.mapper.CitationMapper;
import com.nt.service_pfans.PFANS2000.mapper.StaffexitprocedureMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class StaffexitprocedureServiceImpl implements StaffexitprocedureService {

    @Autowired
    private StaffexitprocedureMapper staffexitprocedureMapper;
    @Autowired
    private CitationMapper citationMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    //列表查询
    @Override
    public List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    //按id查询
    @Override
    public StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitprocedure_id(staffexitprocedureid);
        List<Citation> citationlist = citationMapper.select(citation);
        citationlist = citationlist.stream().sorted(Comparator.comparing(Citation::getRowindex)).collect(Collectors.toList());
        Staffexitprocedure Staff = staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedureid);
        staffVo.setStaffexitprocedure(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
    }

    //更新
    @Override
    public void update(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        updateRetireDate(staffexitprocedureVo);
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preUpdate(tokenModel);
        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
        String staffexitprocedureid = staffexitprocedure.getStaffexitprocedure_id();
        Citation cita = new Citation();
        cita.setStaffexitprocedure_id(staffexitprocedureid);
        citationMapper.delete(cita);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitprocedure_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }
    //新建
    @Override
    public void insert(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        String staffexitprocedureid = UUID.randomUUID().toString();
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preInsert(tokenModel);
        staffexitprocedure.setStaffexitprocedure_id(staffexitprocedureid);
        staffexitprocedureMapper.insertSelective(staffexitprocedure);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitprocedure_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    public void  updateRetireDate(StaffexitprocedureVo staffexitprocedureVo){
        if(staffexitprocedureVo.getStaffexitprocedure().getStage() == "3" && staffexitprocedureVo.getStaffexitprocedure().getStatus() == "4"){
            Query query = new Query(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitprocedure().getUser_id()));
            Update update = new Update();
            update.set("resignation_date",staffexitprocedureVo.getStaffexitprocedure().getEntry_time());
            mongoTemplate.updateFirst(query, update, CustomerInfo.class);
        }
    }
}
