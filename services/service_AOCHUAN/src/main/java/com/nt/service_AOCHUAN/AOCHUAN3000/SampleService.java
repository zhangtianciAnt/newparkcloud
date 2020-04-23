package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SampleService {
    List<Sample> get() throws Exception;

    Sample getOne(String id) throws Exception;

    List<Sample> getForSupplier(String id) throws Exception;

    List<Sample> getForCustomer(String id) throws Exception;

    void update(Sample sample, TokenModel tokenModel) throws Exception;

    void insert(Sample sample, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;
}
