package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LunarbonusService {

    List<Lunarbonus> getList(Lunarbonus lunarbonus) throws Exception;

}
