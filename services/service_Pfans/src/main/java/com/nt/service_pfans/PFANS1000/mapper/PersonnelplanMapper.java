package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PersonnelplanMapper extends MyMapper<PersonnelPlan> {
//  zy-7/7-禅道213任务
  @Select("select supplierinfor_id,supchinese,supjapanese,supenglish,abbreviation from supplierinfor where status = '0' ")
  List<Supplierinfor> getSupplierinfor();

  @Select("select expatriatesinfor_id,expname as name,suppliername,supplierinfor_id as suppliernameid,rn as thisyear,jobclassification as entermouth from expatriatesinfor where status = '0' and group_id = #{groupid} ")
  List<ExternalVo> getExternal(@Param("groupid") String groupid);

  // add-lyt-21/1/29-禅道任务648-start
  @Select("SELECT yearsantid,username,aptoju,jutoma,ltrank,exrank FROM personalcost WHERE yearsantid IN(SELECT yearsantid FROM personalcostyears WHERE YEARS= #{years}) AND userid = #{userid}")
  List<PersonalCost> getPersonalCost(@Param("years") String years, @Param("userid") String userid );
  // add-lyt-21/1/29-禅道任务648-end
}
