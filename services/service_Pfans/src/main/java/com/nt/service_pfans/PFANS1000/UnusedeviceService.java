package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UnusedeviceService {
    public List<Unusedevice> selectUnusedevice() throws Exception;

    List<Unusedevice> getUnusedevice(Unusedevice unusedevice) throws Exception;

    public Unusedevice One(String unusedeviceid) throws Exception;

    public void update(Unusedevice unusedevice, TokenModel tokenModel) throws Exception;

    public void insert(Unusedevice unusedevice, TokenModel tokenModel)throws Exception;

    public List<Unusedevice> getUnusedeviceList(Unusedevice unusedevice, HttpServletRequest request) throws Exception;

}
