package com.nt.service_Org;


import com.nt.dao_Org.PersonScale;
import com.nt.dao_Org.PersonScaleMee;
import com.nt.dao_Org.Vo.PersonScaleVo;

import java.util.List;

public interface PersonScaleService {

    void calMonthScaleInfo()throws Exception;

    public List<PersonScaleMee> getList(PersonScaleMee personScaleMee) throws Exception;

    public PersonScaleVo getPeopleInfo(String personScaleMee_id, String yearMonth) throws Exception;
}
