package com.nt.service_Org.mapper;

import com.nt.dao_Org.Dictionary;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictionaryMapper extends MyMapper<Dictionary> {
    //以下接口用于用户级数据字典页面

    //查询字典大分类（即PCODE为空的数据）
    List<Dictionary> bigList();

    //查询字典大分类中的小分类
    List<Dictionary> smallAtbig(@Param("code") String code);
}
