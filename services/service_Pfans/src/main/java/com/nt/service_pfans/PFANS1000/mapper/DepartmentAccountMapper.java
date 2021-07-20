package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccountTotal;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentAccountVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentTotalVo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum3Vo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DepartmentAccountMapper extends MyMapper<DepartmentAccount>{

    //查询各个表集计数据，部门别表
    List<DepartmentAccountVo> selectByYearAndDep(@Param("years") String years, @Param("department") String department);
    //部门别表批量插入数据
    void insertDepAll(@Param("list") List<DepartmentAccount> departmentAccountListInsert);
    //部门别表批量更新数据
    void updateDepAll(@Param("list") List<DepartmentAccount> departmentAccountListUpdate);
    //查询当前年度，当前部门，当前theme数据是否存在
    List<DepartmentAccountTotal> selectTotal(@Param("years") String years, @Param("department") String department,@Param("themeid") String themeid);
    //查询各个表集计数据，部门别合计表
    List<DepartmentTotalVo> selectTotalBytheme(@Param("years") String years, @Param("department") String department, @Param("themeid") String themeid);
    //获取当前theme，当前年度，当前部门，产生的外注费用
    List<DepartmentAccountTotal> selectPJMount(@Param("years") String years, @Param("department") String department, @Param("themeid") String themeid);
    //部门别合计表批量插入数据
    void insertDepTotalAll(@Param("list") List<DepartmentAccountTotal> departmentAccountTotalInsert);
    //部门别合计表批量更新数据
    void updateDepTotalAll(@Param("list") List<DepartmentAccountTotal> departmentAccountTotalUpdate);
    //画面检索数据
    List<DepartmentAccount> selectALL(@Param("years") String years, @Param("department") String department);

}
