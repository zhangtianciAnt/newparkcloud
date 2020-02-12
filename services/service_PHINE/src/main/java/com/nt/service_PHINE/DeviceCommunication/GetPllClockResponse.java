
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
 *         &lt;element name="GetPllClockResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="pllClockObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice}PllClockObject" minOccurs="0"/&gt;
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
    "getPllClockResult",
    "pllClockObject"
})
@XmlRootElement(name = "GetPllClockResponse")
public class GetPllClockResponse {

    @XmlElement(name = "GetPllClockResult")
    protected Boolean getPllClockResult;
    @XmlElementRef(name = "pllClockObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<PllClockObject> pllClockObject;

    /**
     * 获取getPllClockResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetPllClockResult() {
        return getPllClockResult;
    }

    /**
     * 设置getPllClockResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetPllClockResult(Boolean value) {
        this.getPllClockResult = value;
    }

    /**
     * 获取pllClockObject属性的值。
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PllClockObject }{@code >}
     *
     */
    public JAXBElement<PllClockObject> getPllClockObject() {
        return pllClockObject;
    }

    /**
     * 设置pllClockObject属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PllClockObject }{@code >}
     *
     */
    public void setPllClockObject(JAXBElement<PllClockObject> value) {
        this.pllClockObject = value;
    }

}
