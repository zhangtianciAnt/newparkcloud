package com.nt.service_pfans.PFANS2000;


import com.nt.dao_Pfans.PFANS2000.Irregulartiming;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface IrregulartimingService {

  List<Irregulartiming> getAllIrregulartiming() throws Exception;

  Irregulartiming getIrregulartimingOne(String irregulartimingid) throws  Exception;

 void insertIrregulartiming(Irregulartiming irregulartiming,TokenModel tokenModel) throws Exception;

 void updateIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception;

}
