package com.nt.service_AOCHUAN.AOCHUAN2000;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.utils.dao.TokenModel;
import java.util.List;

public interface CustomerbaseinforService {
    List<Customerbaseinfor> get() throws Exception;

    Customerbaseinfor getOne(String id) throws Exception;

    void update(Customerbaseinfor customerbaseinfor, TokenModel tokenModel) throws Exception;

    String insert(Customerbaseinfor customerbaseinfor, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;
}
