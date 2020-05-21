package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.Punchcard;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 *
 */
public interface AnnualLeaveService {

    /**
     * 获取休假列表
     *
     * @return
     * @throws Exception
     */
    List<AnnualLeave> getDataList(TokenModel tokenModel) throws Exception;

    void insertattendance(int diffday,String staffId,String staffNo) throws Exception;

    void insertattendancebp(int diffday,String staffId,String staffNo) throws Exception;

    void insertpunchcard(int diffday) throws Exception;

    void selectattendance() throws Exception;

    void selectattendancebp() throws Exception;

    void insert() throws Exception;

    //获取打卡记录（参数）
    void getPunchcard(List<Punchcard> Punchcard) throws Exception;

    //获取打卡记录bp（参数）
    void getPunchcardbp(List<Punchcard> Punchcard) throws Exception;
    /**
     * 数据做成
     */
    void insertannualLeave(CustomerInfo customer) throws Exception;

     void insertNewAnnualRest(UserVo userVo,String id) throws Exception;
}
