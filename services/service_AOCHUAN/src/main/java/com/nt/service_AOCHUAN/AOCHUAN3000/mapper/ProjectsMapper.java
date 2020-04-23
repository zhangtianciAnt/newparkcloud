package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProjectsMapper extends MyMapper<Projects> {
    //获取-项目表
    public List<Projects> getProjectList();
    //删除-项目表
    void deleteFromProjectsByDoubleKey(@Param("modifyby") String modifyby,@Param("products_id") String products_id,@Param("supplier_id") String supplier_id,@Param("status") String status);
    //存在Check
    public List<Projects> existCheck(@Param("id") String id,@Param("status") String status);
    //唯一性Check
    public List<Projects> uniqueCheck(@Param("id") String id,@Param("products_id") String products_id,@Param("supplier_id") String supplier_id);

}

