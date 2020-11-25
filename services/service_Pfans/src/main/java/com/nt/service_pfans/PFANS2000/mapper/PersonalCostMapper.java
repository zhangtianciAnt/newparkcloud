package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.utils.MyMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PersonalCostMapper extends MyMapper<PersonalCost>{

    List<PersonalCost> selectPersonalCostResult(@Param("list") List<CustomerInfo> customerInfoList,@Param("yearsantid") String yearsantid);

    void updatePersonalCost(@Param("uplist") List<PersonalCost> personalCostList, @Param("tokenModel") TokenModel tokenModel);
}
