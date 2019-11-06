package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Citation;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
import com.nt.service_pfans.PFANS2000.mapper.StaffexitprocedureMapper;
import com.nt.service_pfans.PFANS2000.mapper.CitationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class StaffexitprocedureServiceImpl implements StaffexitprocedureService {

    @Autowired
    private StaffexitprocedureMapper staffexitprocedureMapper;
    @Autowired
    private CitationMapper citationMapper;

    //列表查询
    @Override
    public List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    //新建
    @Override
    public void create(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception {
        if (!(staffexitprocedure.equals(null) || staffexitprocedure.equals(""))) {
            staffexitprocedure.preInsert(tokenModel);
            staffexitprocedure.setStaffexitprocedure_id(UUID.randomUUID().toString());
            staffexitprocedureMapper.insertSelective(staffexitprocedure);
        }
    }

    //编辑
//    @Override
//    public void update(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception {
//        staffexitprocedure.preUpdate(tokenModel);
//        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
//    }

    //按id查询
    @Override
    public Staffexitprocedure one(String staffexitprocedure_id) throws Exception {
        if (staffexitprocedure_id.equals("")) {
            return null;
        }
        return staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedure_id);
    }

    //按id查询
    @Override
    public StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitprocedure_id(staffexitprocedureid);
        List<Citation> citationlist = citationMapper.select(citation);
        Staffexitprocedure Staff = staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedureid);
        staffVo.setStaffexitprocedure(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
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
            for (Citation citation : citationlist) {
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitprocedure_id(staffexitprocedureid);
                citationMapper.insertSelective(citation);
            }
        }
    }
    //新建
    @Override
    public void update(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preUpdate(tokenModel);
        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            for (Citation citation : citationlist) {
                citation.preUpdate(tokenModel);
                citationMapper.updateByPrimaryKey(citation);
            }
        }
    }
}
