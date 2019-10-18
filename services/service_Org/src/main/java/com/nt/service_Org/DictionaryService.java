package com.nt.service_Org;

import com.nt.dao_Org.Dictionary;

import java.util.List;

public interface DictionaryService {

    List<Dictionary> getForSelect(String code) throws Exception;
}
