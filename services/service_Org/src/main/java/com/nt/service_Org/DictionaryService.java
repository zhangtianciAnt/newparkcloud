package com.nt.service_Org;

import com.nt.dao_Org.Dictionary;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DictionaryService {

    List<Dictionary> getForSelect(String code) throws Exception;

    //查询字典大分类（即PCODE为空的数据）
    List<Dictionary> bigList() throws Exception;

    //增加字典大分类中的小分类
    List<Dictionary> smallAtbig(String code) throws Exception;

    //删除字典小分类
    void deleteCodes(List<Dictionary> dictionaries, TokenModel tokenModel) throws Exception;

    //更新小分类
    void updataCodes(List<Dictionary> dictionaries, TokenModel tokenModel) throws Exception;

    //新增小分类
    void insertCodes(List<Dictionary> dictionaries, TokenModel tokenModel) throws Exception;
}
