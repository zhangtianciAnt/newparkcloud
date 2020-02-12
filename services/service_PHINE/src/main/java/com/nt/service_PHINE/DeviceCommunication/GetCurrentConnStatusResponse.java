
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetCurrentConnStatusResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="deviceConnStates" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice}ArrayOfDeviceConnState" minOccurs="0"/&gt;
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
     * 获取getCurrentConnStatusResult属性的值。
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
     * 设置getCurrentConnStatusResult属性的值。
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
     * 获取deviceConnStates属性的值。
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
     * 设置deviceConnStates属性的值。
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
