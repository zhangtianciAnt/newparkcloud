package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import cn.hutool.core.lang.Console;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinPurchaseSerivce;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinPurchaseMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class FinPurchaseServiceImpl implements FinPurchaseSerivce {

    @Autowired
    FinPurchaseMapper finPurchaseMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    //获取财务-采购数据
    @Override
    public List<FinPurchase> getFinPurchaseList(FinPurchase finPurchase) throws Exception {
        return finPurchaseMapper.select(finPurchase);
    }

    @Override
    public FinPurchase getForm(String id) throws Exception {
        return finPurchaseMapper.selectByPrimaryKey(id);
    }

    //更新
    @Override
    public void update(FinPurchase finPurchase, TokenModel tokenModel) throws Exception {

        finPurchase.preUpdate(tokenModel);
        System.out.println(finPurchase);
        finPurchaseMapper.updateByPrimaryKeySelective(finPurchase);
    }

    //存在Check
    @Override
    public Boolean existCheck(FinPurchase finPurchase) throws Exception {

        List<FinPurchase> resultLst =  finPurchaseMapper.existCheck(finPurchase.getPurchase_id(),"0");
        if (resultLst.isEmpty()){
            return false;
        }
        return true;
    }

    //唯一性Check
    @Override
    public Boolean uniqueCheck(FinPurchase finPurchase) throws Exception {
        List<FinPurchase> resultLst = finPurchaseMapper.uniqueCheck(finPurchase.getPurchase_id(), finPurchase.getContractnumber());

        if (resultLst.isEmpty()){
            return false;
        }
        return true;
    }

    //更新走货
    @Override
    public void updateTransportGood(FinPurchase finPurchase, TokenModel tokenModel) throws Exception {

        finPurchase.preUpdate(tokenModel);
        finPurchaseMapper.updateTransportGood(finPurchase.getInvoicenumber(), finPurchase.getAp_date(),finPurchase.getPaymentstatus(), finPurchase.getModifyby(),finPurchase.getTransportgood_id());
    }

    //生成代办
    @Async
    public void ToDoNotice(FinPurchase finPurchase) throws Exception{
        // 创建代办
        if("PY011001".equals(finPurchase.getPaymentstatus())){
            List<MembersVo> membersVos =  roleService.getMembers("5eba6f09e52fa718db632696");
            for (MembersVo membersVo:
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【用款申请】：您有一条付款确认需要处理。");
                toDoNotice.setInitiator(finPurchase.getCreateby());
                toDoNotice.setContent("合同编号编号【" +finPurchase.getContractnumber()+"】");
                toDoNotice.setDataid(finPurchase.getPurchase_id());
                toDoNotice.setUrl("/AOCHUAN5002FormView");
                toDoNotice.setCreateby(finPurchase.getCreateby());
                Date d = new Date();
                toDoNotice.setCreateon(d);
                toDoNotice.setStatus("2");
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }
    }

    //系统服务(每天：0：00：00)
    //@Scheduled( cron="0 0 24 * * ?")
    @Scheduled( cron="3 * * * * ?")
    public void createNotice() throws Exception {
        FinPurchase finPurchase = new FinPurchase();
        finPurchase.setPaymentstatus("PY011001");//未付款
        finPurchase.setStatus("2");//状态可用
        List<FinPurchase> finPurchaseList = finPurchaseMapper.select(finPurchase);
        for (FinPurchase finPurchase1 : finPurchaseList) {
            ToDoNotice(finPurchase1);
        }
    }
}
