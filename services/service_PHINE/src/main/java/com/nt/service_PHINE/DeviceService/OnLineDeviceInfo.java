
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OnLineDeviceInfo complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="OnLineDeviceInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OnlineDeviceCounter" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="OnlineResult" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnLineDeviceInfo", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "onlineDeviceCounter",
    "onlineResult"
})
public class OnLineDeviceInfo {

    @XmlElement(name = "OnlineDeviceCounter")
    protected Integer onlineDeviceCounter;
    @XmlElementRef(name = "OnlineResult", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfint> onlineResult;

    /**
     * ��ȡonlineDeviceCounter���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getOnlineDeviceCounter() {
        return onlineDeviceCounter;
    }

    /**
     * ����onlineDeviceCounter���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setOnlineDeviceCounter(Integer value) {
        this.onlineDeviceCounter = value;
    }

    /**
     * ��ȡonlineResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *
     */
    public JAXBElement<ArrayOfint> getOnlineResult() {
        return onlineResult;
    }

    /**
     * ����onlineResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *
     */
    public void setOnlineResult(JAXBElement<ArrayOfint> value) {
        this.onlineResult = value;
    }

}
