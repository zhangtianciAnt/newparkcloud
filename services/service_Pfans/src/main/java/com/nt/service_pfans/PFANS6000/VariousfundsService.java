package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface VariousfundsService {

    List<Variousfunds> getvariousfunds(Variousfunds variousfunds) throws Exception;

    public Variousfunds getvariousfundsApplyOne(String variousfunds_id) throws Exception;

    public void updatevariousfundsApply(Variousfunds variousfunds, TokenModel tokenModel) throws Exception;

    public void createvariousfundsApply(Variousfunds variousfunds, TokenModel tokenModel) throws Exception;
}
