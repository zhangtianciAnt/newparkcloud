
package com.nt.service_PHINE.DeviceService;

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
 *         &lt;element name="GetCurrentConnStatusResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="deviceConnStates" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ArrayOfDeviceConnState" minOccurs="0"/&gt;
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
    "getCurrentConnStatusResult",
    "deviceConnStates"
})
@XmlRootElement(name = "GetCurrentConnStatusResponse")
public class GetCurrentConnStatusResponse {

    @XmlElement(name = "GetCurrentConnStatusResult")
    protected Boolean getCurrentConnStatusResult;
    @XmlElementRef(name = "deviceConnStates", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfDeviceConnState> deviceConnStates;

    /**
     * ��ȡgetCurrentConnStatusResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetCurrentConnStatusResult() {
        return getCurrentConnStatusResult;
    }

    /**
     * ����getCurrentConnStatusResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetCurrentConnStatusResult(Boolean value) {
        this.getCurrentConnStatusResult = value;
    }

    /**
     * ��ȡdeviceConnStates���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDeviceConnState }{@code >}
     *
     */
    public JAXBElement<ArrayOfDeviceConnState> getDeviceConnStates() {
        return deviceConnStates;
    }

    /**
     * ����deviceConnStates���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDeviceConnState }{@code >}
     *
     */
    public void setDeviceConnStates(JAXBElement<ArrayOfDeviceConnState> value) {
        this.deviceConnStates = value;
    }

}
