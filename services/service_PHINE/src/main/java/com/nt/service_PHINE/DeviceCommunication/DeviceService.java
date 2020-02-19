package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.1.18
 * 2020-02-19T14:02:26.096+08:00
 * Generated source version: 3.1.18
 *
 */
@WebServiceClient(name = "DeviceService",
                  wsdlLocation = "http://localhost:8734/WcfServiceLib_VerityPlatform/DeviceService/?wsdl",
                  targetNamespace = "http://tempuri.org/")
public class DeviceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "DeviceService");
    public final static QName BasicHttpBindingIDeviceService = new QName("http://tempuri.org/", "BasicHttpBinding_IDeviceService");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8734/WcfServiceLib_VerityPlatform/DeviceService/?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(DeviceService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "http://localhost:8734/WcfServiceLib_VerityPlatform/DeviceService/?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public DeviceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public DeviceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DeviceService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public DeviceService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public DeviceService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public DeviceService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns IDeviceService
     */
    @WebEndpoint(name = "BasicHttpBinding_IDeviceService")
    public IDeviceService getBasicHttpBindingIDeviceService() {
        return super.getPort(BasicHttpBindingIDeviceService, IDeviceService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IDeviceService
     */
    @WebEndpoint(name = "BasicHttpBinding_IDeviceService")
    public IDeviceService getBasicHttpBindingIDeviceService(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingIDeviceService, IDeviceService.class, features);
    }

}
