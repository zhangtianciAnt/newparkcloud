
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
 *         &lt;element name="OnlineDeviceAllResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="OnlineInfo" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}OnLineDeviceInfo" minOccurs="0"/&gt;
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
    "onlineDeviceAllResult",
    "onlineInfo"
})
@XmlRootElement(name = "OnlineDeviceAllResponse")
public class OnlineDeviceAllResponse {

    @XmlElement(name = "OnlineDeviceAllResult")
    protected Boolean onlineDeviceAllResult;
    @XmlElementRef(name = "OnlineInfo", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<OnLineDeviceInfo> onlineInfo;

    /**
     * ��ȡonlineDeviceAllResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOnlineDeviceAllResult() {
        return onlineDeviceAllResult;
    }

    /**
     * ����onlineDeviceAllResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setOnlineDeviceAllResult(Boolean value) {
        this.onlineDeviceAllResult = value;
    }

    /**
     * ��ȡonlineInfo���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OnLineDeviceInfo }{@code >}
     *
     */
    public JAXBElement<OnLineDeviceInfo> getOnlineInfo() {
        return onlineInfo;
    }

    /**
     * ����onlineInfo���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OnLineDeviceInfo }{@code >}
     *
     */
    public void setOnlineInfo(JAXBElement<OnLineDeviceInfo> value) {
        this.onlineInfo = value;
    }

}
