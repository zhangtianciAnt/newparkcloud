package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.service_AOCHUAN.AOCHUAN6000.VacationService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.VacationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class VacationServiceImpl implements VacationService {

    @Autowired
    private VacationMapper vacationMapper;


    @Override
    public List<Vacation> get(Vacation vacation) throws Exception {
        return vacationMapper.select(vacation);
    }

    @Override
    public void insert(Vacation vacation, TokenModel tokenModel) throws Exception {
        vacation.preInsert(tokenModel);
        vacation.setVacation_id(UUID.randomUUID().toString());
        vacationMapper.insert(vacation);
    }

    @Override
    public Vacation One(String ids) throws Exception {
        return vacationMapper.selectByPrimaryKey(ids);
    }

    @Override
    public void update(Vacation vacation, TokenModel tokenModel) throws Exception {
        vacation.preUpdate(tokenModel);
        vacationMapper.updateByPrimaryKey(vacation);
    }

    @Override
    public void delete(String id) throws Exception {
        Vacation vacation = new Vacation();
        vacation.setVacation_id(id);
        vacation.setStatus("1");
        vacationMapper.updateByPrimaryKey(vacation);


    }
}
