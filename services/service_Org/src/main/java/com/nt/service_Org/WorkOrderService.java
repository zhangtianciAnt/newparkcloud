package com.nt.service_Org;



import com.nt.dao_Org.WorkOrder;
import com.nt.utils.dao.TokenModel;
import java.util.List;

public interface WorkOrderService {

    //保存
    void save(WorkOrder workorder, TokenModel tokenModel) throws Exception;

    //获取
    List<WorkOrder> get(WorkOrder workorder) throws Exception;

    //根据id获取工单信息
    WorkOrder getInfoById(String id) throws Exception;

}
