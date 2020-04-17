
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
 *         &lt;element name="GetDevicePllClockResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="devicePllClocks" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ArrayOfPllClockObject" minOccurs="0"/&gt;
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
    "getDevicePllClockResult",
    "devicePllClocks"
})
@XmlRootElement(name = "GetDevicePllClockResponse")
public class GetDevicePllClockResponse {

    @XmlElement(name = "GetDevicePllClockResult")
    protected Boolean getDevicePllClockResult;
    @XmlElementRef(name = "devicePllClocks", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPllClockObject> devicePllClocks;

    /**
     * ��ȡgetDevicePllClockResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetDevicePllClockResult() {
        return getDevicePllClockResult;
    }

    /**
     * ����getDevicePllClockResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetDevicePllClockResult(Boolean value) {
        this.getDevicePllClockResult = value;
    }

    /**
     * ��ȡdevicePllClocks���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPllClockObject }{@code >}
     *
     */
    public JAXBElement<ArrayOfPllClockObject> getDevicePllClocks() {
        return devicePllClocks;
    }

    /**
     * ����devicePllClocks���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPllClockObject }{@code >}
     *
     */
    public void setDevicePllClocks(JAXBElement<ArrayOfPllClockObject> value) {
        this.devicePllClocks = value;
    }

}
