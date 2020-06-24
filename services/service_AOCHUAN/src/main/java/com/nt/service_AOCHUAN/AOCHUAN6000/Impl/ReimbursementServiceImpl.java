package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Dailyfee;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.ReimAndReimDetail;
import com.nt.service_AOCHUAN.AOCHUAN6000.ReimbursementService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.DailyfreeMapper;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.ReimbursementDetailMapper;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.ReimbursementMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementMapper reimbursementMapper;
    @Autowired
    private ReimbursementDetailMapper reimbursementDetailMapper;

    @Autowired
    private DailyfreeMapper dailyfreeMapper;

    //获取费用主表
    @Override
    public List<Reimbursement> getReimbursementList(Reimbursement reimbursement) throws Exception {
        return reimbursementMapper.select(reimbursement);
    }

    @Override
    public Reimbursement getForm(String id) throws Exception {
        Reimbursement reimbursement = new Reimbursement();
        Dailyfee dailyfee = new Dailyfee();
        reimbursement = reimbursementMapper.selectByPrimaryKey(id);
        dailyfee.setReimbursement_id(id);
        List<Dailyfee> list = dailyfreeMapper.select(dailyfee);


        reimbursement.setTablercList(list);

        return reimbursement;
    }

    //获取费用明细表
    @Override
    public List<ReimbursementDetail> getReimbursementDetailList(ReimbursementDetail reimbursementDetail) throws Exception {
        String reimNO = reimbursementDetail.getReimbursement_no();
        return reimbursementDetailMapper.getReimbursementDetailList(reimNO,"0");
    }

    //新建
    @Override
    public void insert(Object object, TokenModel tokenModel) throws Exception {

        if (object instanceof Reimbursement){
            //PROJECTS
            Reimbursement reimbursement = new Reimbursement();
            BeanUtils.copyProperties(reimbursement,object);

            reimbursement.preInsert(tokenModel);
            reimbursementMapper.insert(reimbursement);

        } else if(object instanceof ReimbursementDetail){
            //FOLLOWUPRECORD
            ReimbursementDetail reimbursementDetail = new ReimbursementDetail();
            BeanUtils.copyProperties(reimbursementDetail,object);

            reimbursementDetail.preInsert(tokenModel);
            reimbursementDetailMapper.insert(reimbursementDetail);
        }
    }

    //更新
    @Override
    public void update(Object object, TokenModel tokenModel) throws Exception {

        if (object instanceof Reimbursement){

            Reimbursement reimbursement = new Reimbursement();
            BeanUtils.copyProperties(reimbursement,object);
            reimbursement.preUpdate(tokenModel);
            reimbursementMapper.updateByPrimaryKeySelective(reimbursement);

        } else if(object instanceof ReimbursementDetail){

            ReimbursementDetail reimbursementDetail = new ReimbursementDetail();
            BeanUtils.copyProperties(reimbursementDetail,object);
            reimbursementDetail.preUpdate(tokenModel);
            reimbursementDetailMapper.updateByPrimaryKeySelective(reimbursementDetail);
        }
    }

    //删除
    @Override
    public void delete(Object object, TokenModel tokenModel) throws Exception {

        if (object instanceof Reimbursement){

            Reimbursement reimbursement = new Reimbursement();
            BeanUtils.copyProperties(reimbursement,object);
            reimbursement.preUpdate(tokenModel);
            reimbursementMapper.deleteFromReimbursementByDoubleKey(reimbursement.getModifyby(),reimbursement.getReimbursement_id(),"1");

        } else if(object instanceof ReimbursementDetail){

            ReimbursementDetail reimbursementDetail = new ReimbursementDetail();
            BeanUtils.copyProperties(reimbursementDetail,object);
            reimbursementDetail.preUpdate(tokenModel);
            reimbursementDetailMapper.deleteFromReimbursementDetailByPrikey(reimbursementDetail.getModifyby(),reimbursementDetail.getReimbursement_no(),"1");
        }
    }

    //存在Check
    @Override
    public Boolean existCheck(Object object) throws Exception {

        if (object instanceof Reimbursement){
            Reimbursement reimbursement = new Reimbursement();
            BeanUtils.copyProperties(reimbursement,object);
            List<Reimbursement> resultLst =  reimbursementMapper.existCheck(reimbursement.getReimbursement_id(),"0");
            if (resultLst.isEmpty()){
                return false;
            }

        } else if(object instanceof ReimbursementDetail){
            ReimbursementDetail reimbursementDetail = new ReimbursementDetail();
            BeanUtils.copyProperties(reimbursementDetail,object);
            List<ReimbursementDetail> resultLst =  reimbursementDetailMapper.existCheck(reimbursementDetail.getReimbursement_detail_id(),"0");
            if (resultLst.isEmpty()){
                return false;
            }
        }

        return true;
    }

    //唯一性Check
    @Override
    public Boolean uniqueCheck(Reimbursement reimbursement) throws Exception {
        List<Reimbursement> resultLst = reimbursementMapper.uniqueCheck(reimbursement.getReimbursement_id(), reimbursement.getReimbursement_no());

        if (resultLst.isEmpty()){
            return false;
        }

        return true;
    }

    @Override
    public void insertDailyfree(ReimAndReimDetail reimAndReimDetail, TokenModel tokenModel) throws Exception {
        List<Dailyfee> list = reimAndReimDetail.getReimrcFormList();
        for(Dailyfee d : list){
            d.preInsert(tokenModel);
            d.setReimbursement_id(reimAndReimDetail.getReimForm().getReimbursement_id());
            d.setDailyfeeid(UUID.randomUUID().toString());
            dailyfreeMapper.insert(d);
        }
    }

    @Override
    public void deleteDailyfree(Dailyfee dailyfee, TokenModel tokenModel) throws Exception {
        dailyfreeMapper.delete(dailyfee);
    }


}
