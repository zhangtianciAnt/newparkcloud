package com.nt.service_AOCHUAN.AOCHUAN6000;


import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.StatisticsVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface VacationService {

    List<Vacation> get(Vacation vacation) throws Exception;

    public void insert(Vacation vacation, TokenModel tokenModel)throws  Exception;

    public Vacation One(String ids)throws  Exception;

    public void update(Vacation vacation, TokenModel tokenModel)throws  Exception;

    void delete(String id) throws Exception;

//获取年休一览
    List<StatisticsVo> getVo() throws Exception;



}
