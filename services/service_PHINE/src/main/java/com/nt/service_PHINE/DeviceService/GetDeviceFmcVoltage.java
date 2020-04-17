
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "deviceId",
    "deviceFmcVoltages"
})
@XmlRootElement(name = "GetDeviceFmcVoltage")
public class GetDeviceFmcVoltage {

    @XmlElementRef(name = "deviceId", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceId;
    @XmlElementRef(name = "deviceFmcVoltages", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfFmcVoltageObject> deviceFmcVoltages;

    /**
     * ��ȡdeviceId���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getDeviceId() {
        return deviceId;
    }

    /**
     * ����deviceId���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setDeviceId(JAXBElement<String> value) {
        this.deviceId = value;
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
