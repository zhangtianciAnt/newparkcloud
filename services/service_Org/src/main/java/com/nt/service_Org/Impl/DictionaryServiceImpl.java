package com.nt.service_Org.Impl;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_Org.mapper.TodoNoticeMapper;
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
            dictionary.setCode(code);
        }
        return dictionaryMapper.select(dictionary);
    }
}
