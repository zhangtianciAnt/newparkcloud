package com.nt.service_AOCHUAN.AOCHUAN2000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CustomerbaseinforMapper extends MyMapper<Customerbaseinfor> {

    @Select("select * from customerbaseinfor where NOT(`STATUS` = '1')  order by createon desc")
    List<Customerbaseinfor> selectALLcust();

    int allselectCount();

    @Select("select * from customerbaseinfor order by createon desc LIMIT 1 ")
    Customerbaseinfor selectCustomernum();
//添加客户编码
    @Select("select * from customerbaseinfor order by createon asc")
    List<Customerbaseinfor> selectCustomerlist();
}
