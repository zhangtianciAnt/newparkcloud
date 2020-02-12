package com.nt.service_PHINE.Impl;

import com.nt.service_PHINE.DeviceCommunication.HardwareDeviceService;
import com.nt.service_PHINE.DeviceCommunication.IHardwareDeviceService;
import com.nt.service_PHINE.TestService;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: TestServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/12
 * @Version: 1.0
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public Boolean closeConnection(String id) {
        HardwareDeviceService ss = null;
        try {
            ss = new HardwareDeviceService(new URL("http://localhost:8734/WcfServiceLib_HardwareDevice/HardwareDeviceService/?wsdl"), new QName("http://tempuri.org/", "HardwareDeviceService"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert ss != null;
        IHardwareDeviceService port = ss.getBasicHttpBindingIHardwareDeviceService();
        return port.closeConnection("device_1");
    }
}
