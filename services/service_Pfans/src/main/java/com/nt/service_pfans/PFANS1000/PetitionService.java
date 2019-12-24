package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PetitionService {

    List<Petition> get(Petition petition) throws Exception;

    public  Petition selectById(String petition_id) throws Exception;

    public void update(Petition petition,TokenModel tokenModel) throws Exception;

}
