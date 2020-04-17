package com.nt.service_PHINE;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Operationdetail;
import com.nt.utils.dao.TokenModel;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface AsyncService {
    Future<List<Operationdetail>> doLogicFileLoad(List<Fileinfo> fileinfoList, TokenModel tokenModel, String operationId, QName SERVICE_NAME, Map<String, List<Fileinfo>> configProgressMap) throws Exception;

//    List<Fileinfo> getConfigProgressMap(TokenModel tokenModel);
//
//    void clearConfigProgressByToken(TokenModel tokenModel);
}
