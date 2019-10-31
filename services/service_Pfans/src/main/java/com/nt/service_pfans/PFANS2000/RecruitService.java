package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecruitService {

    //查看
    List<Recruit> getRecruit(Recruit recruit) throws Exception;

    public Recruit One(String recruitid) throws Exception;

    //修改
    public void updateRecruit(Recruit recruit, TokenModel tokenModel) throws Exception;

    //创建
    public void insert(Recruit recruit, TokenModel tokenModel)throws Exception;

    //计算金额
    public List<Recruit> getRecruitList(Recruit recruit, HttpServletRequest request) throws Exception;

}
