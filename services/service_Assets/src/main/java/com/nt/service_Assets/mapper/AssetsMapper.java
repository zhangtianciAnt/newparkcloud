package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.Assets;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssetsMapper extends MyMapper<Assets> {

    String getMaxCode(@Param("Asstype") String Asstype,@Param("Bartype") String Bartype);

    List<Assets> selectList4Show(Assets assets);

    List<String> getDepartment();

    //region add scc 21/8/2 人员部门变更时，名下资产也要进行变更 from
    void updateByOwner(@Param("owner") String owner,@Param("usedepartment") String usedepartment);
    //endregion add scc 21/8/2 人员部门变更时，名下资产也要进行变更 from
}
