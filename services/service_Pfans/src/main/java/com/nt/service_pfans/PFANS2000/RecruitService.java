package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecruitService {

    List<Recruit> getRecruit(Recruit recruit) throws Exception;

    public Recruit One(String recruitid) throws Exception;

    public void updateRecruit(Recruit recruit, TokenModel tokenModel) throws Exception;

    public void insert(Recruit recruit, TokenModel tokenModel)throws Exception;

    public List<Recruit> getRecruitList(Recruit recruit, HttpServletRequest request) throws Exception;

}
