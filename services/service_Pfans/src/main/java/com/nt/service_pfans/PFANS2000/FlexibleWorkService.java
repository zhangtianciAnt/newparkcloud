package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FlexibleWorkService {
    //获取
    List<FlexibleWork> getFlexiblework(FlexibleWork Flexiblework) throws Exception;
}
