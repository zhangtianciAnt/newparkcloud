package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.Punchcard;
import com.nt.utils.dao.TokenModel;

import java.text.ParseException;
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

    void insertHistoricalCard(String strStartDate,String strendDate,String strFlg,String strJobnumber) throws Exception;

    void getattendanceByuser(String userid) throws Exception;

    void selectattendance() throws Exception;

    void selectattendancebp() throws Exception;

    void insert() throws Exception;

    //add 导入人员计算年休 20201030
    void insertAnnualImport() throws Exception;
    //add 导入人员计算年休 20201030

    //获取打卡记录（参数）
    void getPunchcard(List<Punchcard> Punchcard) throws Exception;

    //获取打卡记录bp（参数）
    void getPunchcardbp(List<Punchcard> Punchcard) throws Exception;
    /**
     * 数据做成
     */
    void insertannualLeave(CustomerInfo customer) throws Exception;

     void insertNewAnnualRest(UserVo userVo,String id) throws Exception;
    //离职剩余年休
    public String remainingAnnual(String userid,String year) throws Exception;

    //每月最后一天计算实际工资
    void getrealwages() throws Exception;

    //ccm 1019 计算一个月出勤多少小时
    String workDayBymonth(String startDate,String endDate,String year) throws Exception;
    //ccm 1019 计算一个月出勤多少小时
}
