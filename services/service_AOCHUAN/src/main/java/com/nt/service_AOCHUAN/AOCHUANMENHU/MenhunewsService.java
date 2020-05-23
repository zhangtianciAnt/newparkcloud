package com.nt.service_AOCHUAN.AOCHUANMENHU;


import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Newsinformation;

import java.util.List;

public interface MenhunewsService {

    List<Newsinformation> get(Newsinformation newsinformation) throws Exception;


}
