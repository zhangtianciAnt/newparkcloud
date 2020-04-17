
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
 *         &lt;element name="GetDeviceFmcVoltageResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="deviceFmcVoltages" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ArrayOfFmcVoltageObject" minOccurs="0"/&gt;
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
    "getDeviceFmcVoltageResult",
    "deviceFmcVoltages"
})
@XmlRootElement(name = "GetDeviceFmcVoltageResponse")
public class GetDeviceFmcVoltageResponse {

    @XmlElement(name = "GetDeviceFmcVoltageResult")
    protected Boolean getDeviceFmcVoltageResult;
    @XmlElementRef(name = "deviceFmcVoltages", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfFmcVoltageObject> deviceFmcVoltages;

    /**
     * ��ȡgetDeviceFmcVoltageResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetDeviceFmcVoltageResult() {
        return getDeviceFmcVoltageResult;
    }

    /**
     * ����getDeviceFmcVoltageResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetDeviceFmcVoltageResult(Boolean value) {
        this.getDeviceFmcVoltageResult = value;
    }

    /**
     * ��ȡdeviceFmcVoltages���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFmcVoltageObject }{@code >}
     *
     */
    public JAXBElement<ArrayOfFmcVoltageObject> getDeviceFmcVoltages() {
        return deviceFmcVoltages;
    }

    /**
     * ����deviceFmcVoltages���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFmcVoltageObject }{@code >}
     *
     */
    public void setDeviceFmcVoltages(JAXBElement<ArrayOfFmcVoltageObject> value) {
        this.deviceFmcVoltages = value;
    }

}
