package com.nt.service_AOCHUAN.AOCHUANMENHU;


import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface MenhuproductsService {

    List<Menhuproducts> get(Menhuproducts menhuproducts) throws Exception;


}
