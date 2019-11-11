package com.nt.service_Org;

import com.nt.dao_Org.Dictionary;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DictionaryService {

    List<Dictionary> getForSelect(String code) throws Exception;

    public void updateDictionary(Dictionary dictionary, TokenModel tokenModel)throws Exception;

}
