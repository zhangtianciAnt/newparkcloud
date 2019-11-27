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
import org.springframework.util.StringUtils;

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

    @Override
    public List<Dictionary> getForvalue2(String value2) throws Exception {
        Dictionary dictionary = new Dictionary();
        if(StrUtil.isNotBlank(value2)){
            dictionary.setValue2(value2);
        }
        return dictionaryMapper.select(dictionary);
    }

    @Override
    public void updateDictionary(Dictionary dictionary, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(dictionary)){
            dictionary.preUpdate(tokenModel);
            dictionaryMapper.updateByPrimaryKeySelective(dictionary);
        }
    }
}

