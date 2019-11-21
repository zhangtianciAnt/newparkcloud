package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Contrast;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContrastService {

    void insert(TokenModel tokenModel) throws Exception;

    List<Contrast> getList(Contrast contrast) throws Exception;
}
