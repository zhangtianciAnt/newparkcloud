package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.GivingVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface GivingListService {
    GivingVo List(String giving_id) throws Exception;
}


