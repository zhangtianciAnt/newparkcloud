
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DeviceConnState complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="DeviceConnState"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ConnStatus" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeviceConnState", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "connStatus",
    "deviceId"
})
public class DeviceConnState {

    @XmlElement(name = "ConnStatus")
    protected Integer connStatus;
    @XmlElementRef(name = "deviceId", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceId;

    /**
     * ��ȡconnStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getConnStatus() {
        return connStatus;
    }

    /**
     * ����connStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setConnStatus(Integer value) {
        this.connStatus = value;
    }

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

}
