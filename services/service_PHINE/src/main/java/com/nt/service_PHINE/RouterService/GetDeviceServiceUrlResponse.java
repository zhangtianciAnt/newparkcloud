
package com.nt.service_PHINE.RouterService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetDeviceServiceUrlResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="serviceUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getDeviceServiceUrlResult",
    "serviceUrl"
})
@XmlRootElement(name = "GetDeviceServiceUrlResponse")
public class GetDeviceServiceUrlResponse {

    @XmlElement(name = "GetDeviceServiceUrlResult")
    protected Boolean getDeviceServiceUrlResult;
    @XmlElementRef(name = "serviceUrl", namespace = "http://Microsoft.ServiceModel.Samples", type = JAXBElement.class, required = false)
    protected JAXBElement<String> serviceUrl;

    /**
     * ��ȡgetDeviceServiceUrlResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetDeviceServiceUrlResult() {
        return getDeviceServiceUrlResult;
    }

    /**
     * ����getDeviceServiceUrlResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetDeviceServiceUrlResult(Boolean value) {
        this.getDeviceServiceUrlResult = value;
    }

    /**
     * ��ȡserviceUrl���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getServiceUrl() {
        return serviceUrl;
    }

    /**
     * ����serviceUrl���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setServiceUrl(JAXBElement<String> value) {
        this.serviceUrl = value;
    }

}
