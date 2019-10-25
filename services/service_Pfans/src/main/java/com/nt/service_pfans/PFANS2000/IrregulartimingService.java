package com.nt.service_pfans.PFANS2000;


import com.nt.dao_Pfans.PFANS2000.Irregulartiming;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface IrregulartimingService {

    List<Irregulartiming> getAllIrregulartiming(Irregulartiming irregulartiming) throws Exception;

    Irregulartiming getIrregulartimingOne(String irregulartiming_id) throws Exception;

    void insertIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception;

    void updateIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception;

}
