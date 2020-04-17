
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
 *         &lt;element name="deviceSlotInfo" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ArrayOfDeviceSlotInfo" minOccurs="0"/&gt;
 *         &lt;element name="FilePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "deviceSlotInfo",
    "filePath"
})
@XmlRootElement(name = "InterconnTestStart")
public class InterconnTestStart {

    @XmlElementRef(name = "deviceSlotInfo", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfDeviceSlotInfo> deviceSlotInfo;
    @XmlElementRef(name = "FilePath", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> filePath;

    /**
     * ��ȡdeviceSlotInfo���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDeviceSlotInfo }{@code >}
     *
     */
    public JAXBElement<ArrayOfDeviceSlotInfo> getDeviceSlotInfo() {
        return deviceSlotInfo;
    }

    /**
     * ����deviceSlotInfo���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDeviceSlotInfo }{@code >}
     *
     */
    public void setDeviceSlotInfo(JAXBElement<ArrayOfDeviceSlotInfo> value) {
        this.deviceSlotInfo = value;
    }

    /**
     * ��ȡfilePath���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getFilePath() {
        return filePath;
    }

    /**
     * ����filePath���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setFilePath(JAXBElement<String> value) {
        this.filePath = value;
    }

}
