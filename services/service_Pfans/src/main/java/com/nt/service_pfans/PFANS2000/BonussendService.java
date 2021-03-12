package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BonussendService {

    List<Bonussend> inserttodo(Bonussend bonussend) throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    List<Bonussend> List(Bonussend bonussend,TokenModel tokenModel) throws Exception;

    public void update(List<Bonussend> bonussend, TokenModel tokenModel) throws Exception;

    // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 start
    //public void updateSend(String id) throws Exception;
    public void updateSend(List<Bonussend> bonussend, TokenModel tokenModel) throws Exception;
    // update gbb 20210312 NT_PFANS_20210305_BUG_130 点击送信发送代办 end
}
