package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.LeaveDaysVo;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.StatisticsVo;
import com.nt.dao_Org.Earlyvacation;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.service_AOCHUAN.AOCHUAN6000.VacationService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.VacationMapper;
import com.nt.service_Org.mapper.EarlyvacationMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class VacationServiceImpl implements VacationService {

    @Autowired
    private VacationMapper vacationMapper;
    @Autowired
    private EarlyvacationMapper earlyvacationMapper;



    @Override
    public List<Vacation> get(Vacation vacation) throws Exception {
        return vacationMapper.select(vacation);
    }

    @Override
    public void insert(Vacation vacation, TokenModel tokenModel) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Vacation selectVac = new Vacation();
        selectVac.setApplicanterid(vacation.getApplicanterid());
        List<Vacation> lists = vacationMapper.select(selectVac);
        if(lists.size() > 0){
            for(Vacation v:lists){

                String vacstart = simpleDateFormat.format(v.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(v.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间

                int compareToKS = vacation.getStartdate().compareTo(datestart); //开始 和 数据库开始 对比
                int compareToKS1 = vacation.getEnddate().compareTo(datestart); //结束 和 开始 对比

                int compareToJS = vacation.getEnddate().compareTo(datestaend); //结束 和 结束 对比
                int compareToJS1 = vacation.getStartdate().compareTo(datestaend); //开始 和 结束 对比



                if((compareToKS == -1 || compareToKS == 0) && (compareToKS1 == 1 || compareToKS1 == 0) ){
                    throw new LogicalException("期间内日期已经申请过，请重新选择日期！");
                }
                else if((compareToKS == 1 || compareToKS == 0)  && (compareToJS == -1 || compareToJS == 0)){
                    throw new LogicalException("期间内日期已经申请过，请重新选择日期！");
                }
                else if((compareToJS1 == -1 || compareToJS1 == 0) && (compareToJS == 1 || compareToJS == 0)){
                    throw new LogicalException("期间内日期已经申请过，请重新选择日期！");
                }
                else if((compareToKS == -1 || compareToKS == 0) && (compareToJS == 1 || compareToJS == 0)){
                    throw new LogicalException("期间内日期已经申请过，请重新选择日期！");
                }
                else {
                    vacation.preInsert(tokenModel);
                    vacation.setVacation_id(UUID.randomUUID().toString());
                    vacationMapper.insert(vacation);
                    break;
                }
            }
        }
        else{
            vacation.preInsert(tokenModel);
            vacation.setVacation_id(UUID.randomUUID().toString());
            vacationMapper.insert(vacation);
        }



    }

    @Override
    public Vacation One(String ids) throws Exception {

        return vacationMapper.selectByPrimaryKey(ids);
    }
//获取年假结余
    @Override
    public Earlyvacation getannualyear(String ids) throws Exception {
        Earlyvacation earlyvacation = new Earlyvacation();
        earlyvacation.setUsernames(ids);
        Earlyvacation earlyvacation1 = earlyvacationMapper.selectOne(earlyvacation);
        if(earlyvacation1 == null){
            return null;
        }
        else {
            return earlyvacation1;
        }

    }

    @Override
    public void update(Vacation vacation, TokenModel tokenModel) throws Exception {
        vacation.preUpdate(tokenModel);
        vacationMapper.updateByPrimaryKey(vacation);
    }

    @Override
    public void delete(String id) throws Exception {
//        Vacation vacation = new Vacation();
//        vacation.setVacation_id(id);
//        vacation.setStatus("1");
        vacationMapper.deleteByPrimaryKey(id);


    }

    @Override
    public List<StatisticsVo> getVo() throws Exception {

        List<StatisticsVo> statistics = vacationMapper.getVo();
        for(StatisticsVo statisticsVo : statistics){
            Earlyvacation earlyvacation = new Earlyvacation();
            earlyvacation.setUsernames(statisticsVo.getNames());

            Earlyvacation earlyvacation1 = earlyvacationMapper.selectOne(earlyvacation);
            statisticsVo.setStartannual(earlyvacation1.getAnnualleave());
            statisticsVo.setStartwedding(earlyvacation1.getMarriageleave());
            statisticsVo.setStartmaternity(earlyvacation1.getMaternityleave());
            statisticsVo.setStartfuneral(earlyvacation1.getFuneralleave());

        }


        return vacationMapper.getVo();
    }

    @Override
    public LeaveDaysVo getVacation(String id) throws Exception {
        return vacationMapper.getVacation(id);
    }
}
