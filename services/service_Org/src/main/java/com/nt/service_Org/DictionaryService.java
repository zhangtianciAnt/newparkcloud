package com.nt.service_Org;

import com.nt.dao_Org.Dictionary;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DictionaryService {

    List<Dictionary> getForSelect(String code) throws Exception;

    List<Dictionary> getForvalue2(String value2) throws Exception;

    public void updateDictionary(Dictionary dictionary, TokenModel tokenModel)throws Exception;

    List<Dictionary> getDictionary(Dictionary dictionary) throws Exception;

    public void upDictionary(List<Dictionary> dictionarylist, TokenModel tokenModel)throws Exception;

}
