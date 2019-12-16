package com.nt.service_Org.Impl;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public List<Dictionary> getForSelect(String code) throws Exception{
        Dictionary dictionary = new Dictionary();
        if(StrUtil.isNotBlank(code)){
            dictionary.setPcode(code);
        }
        return dictionaryMapper.select(dictionary);
    }

    //    以下接口用于用户级数据字典

    /**
     * @Method bigList
     * @Author 王哲
     * @Version 1.0
     * @Description 查询字典大分类（即PCODE为空,STATUS为不为1的数据）
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 10:06
     */
    @Override
    public List<Dictionary> bigList() throws Exception {
        return dictionaryMapper.bigList();
    }

    /**
     * @Method smallAtbig
     * @Author 王哲
     * @Version 1.0
     * @Description 查询字典大分类中的小分类
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 11:18
     */
    @Override
    public List<Dictionary> smallAtbig(String code) throws Exception {
        return dictionaryMapper.smallAtbig(code + "%");
    }

    /**
     * @Method deleteCodes
     * @Author 王哲
     * @Version 1.0
     * @Description 删除小分类字典
     * @Date 2019/12/13 11:18
     */
    @Override
    public void deleteCodes(List<Dictionary> dictionaries, TokenModel tokenModel) throws Exception {
        for (Dictionary d : dictionaries) {
            d.preUpdate(tokenModel);
            d.setStatus("1");
            dictionaryMapper.updateByPrimaryKeySelective(d);
        }
    }

    /**
     * @Method updataCodes
     * @Author 王哲
     * @Version 1.0
     * @Description 更新小分类
     * @Date 2019/12/13 11:44
     */
    @Override
    public void updataCodes(List<Dictionary> dictionaries, TokenModel tokenModel) throws Exception {
        for (Dictionary d : dictionaries) {
            d.preUpdate(tokenModel);
            dictionaryMapper.updateByPrimaryKey(d);
        }
    }

    /**
     * @Method insertCodes
     * @Author 王哲
     * @Version 1.0
     * @Description 新增小分类
     * @Date 2019/12/13 13:18
     */
    @Override
    public void insertCodes(List<Dictionary> dictionaries, TokenModel tokenModel) throws Exception {
        for (Dictionary d : dictionaries) {
            d.preInsert(tokenModel);
            dictionaryMapper.insert(d);
        }
    }
}
