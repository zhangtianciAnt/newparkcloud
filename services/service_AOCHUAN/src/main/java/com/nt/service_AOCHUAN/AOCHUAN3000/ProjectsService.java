package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ProAndFoll;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ProjectsService {

    //获取项目表
    List<Projects> getProjectList() throws Exception;
    //获取记录表数据
    List<FollowUpRecord> getFollowUpRecordList(Projects projects) throws Exception;

    //获取项目表
    Projects getForm(String id) throws Exception;

    List<ProAndFoll> getForSupplier(String id) throws Exception;

    List<ProAndFoll> getForCustomer(String id) throws Exception;

    //新建
    void insert(Object object, TokenModel tokenModel)throws Exception;
    //更新
    void update(Object object, TokenModel tokenModel) throws Exception;
    //删除
    void delete(Object object, TokenModel tokenModel) throws Exception;
    //存在Check
    Boolean existCheck(Object object) throws Exception;
    //唯一性Check
    Boolean uniqueCheck(Projects projects) throws Exception;
    //跟踪记录单独用删除
    void deleteFlw(FollowUpRecord followUpRecord , TokenModel tokenModel) throws Exception;
}
