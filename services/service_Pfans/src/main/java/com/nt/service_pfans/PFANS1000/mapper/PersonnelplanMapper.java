package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PersonnelplanMapper extends MyMapper<PersonnelPlan> {

  @Select("select supplierinfor_id,supchinese from supplierinfor where status = '0'")
  List<Supplierinfor> getSupplierinfor();

  @Select("select expname,cooperinterview.coopername,cooperinterview.suppliername,supplierinfor.supchinese\n" +
          "from expatriatesinfor\n" +
          "left join cooperinterview\n" +
          "on expatriatesinfor.expname = cooperinterview.cooperinterview_id\n" +
          "left join supplierinfor\n" +
          "on cooperinterview.suppliername = supplierinfor.supplierinfor_id\n" +
          "where expatriatesinfor.status = '0'")
  List<ExternalVo> getExternal();
}
