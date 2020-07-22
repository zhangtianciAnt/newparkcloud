package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.BaseVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface WagesService {

    List<Wages> select(TokenModel tokenModel);

    List<Wages> wagesList(Wages wages) throws Exception;

    List<Bonussend> bonusList(Bonussend bonussend) throws Exception;

    List<BaseVo> selectBase(String dates) throws Exception;

    /**
     * 根据givingid获取工资
     *
     * @param givingId
     * @return
     * @throws Exception
     */
    List<Wages> getWagesByGivingId(String givingId) throws Exception;

    int insertWages(List<Wages> wages, TokenModel tokenModel) throws Exception;

    List<Wages> getWagesdepartment(String dates) throws Exception;

    List<Wages> getWagecompany() throws Exception;

    //获取离职人员工资
    List<Wages> getWagesByResign(String user_id,TokenModel tokenModel) throws Exception;

    Wages getwages(String strFlg,TokenModel tokenModel)throws Exception;

    void insertBase(String  givingid,TokenModel tokenModel)throws Exception;

    void insertOtherOne(String  givingid,TokenModel tokenModel)throws Exception;

    void insertOtherTwo(String  givingid,TokenModel tokenModel)throws Exception;

    void insertLackattendance(String strFlg,String  givingid,TokenModel tokenModel)throws Exception;

    void insertResidual(String  strFlg,String  givingid,TokenModel tokenModel)throws Exception;

    void insertContrast(String  givingid,TokenModel tokenModel)throws Exception;

    void deletewages(String strTemp,List<Giving> givinglist)throws Exception;

}


