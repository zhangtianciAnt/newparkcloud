package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Vo.LunarAllVo;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LunarbonusService {

    List<Lunarbonus> getList(TokenModel tokenModel) throws Exception;

    public void insert(LunardetailVo LunardetailVo, TokenModel tokenModel)throws Exception;

    public LunarAllVo getOne(String id,TokenModel tokenModel)throws Exception;

    public  void createTodonotice(Lunarbonus lunarbonus,TokenModel tokenModel) throws Exception;

    public  void overTodonotice(Lunarbonus lunarbonus,TokenModel tokenModel) throws Exception;

}
