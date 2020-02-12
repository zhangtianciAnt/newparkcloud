
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
 *         &lt;element name="GetFmcVoltageResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="fmcVoltageObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice}FmcVoltageObject" minOccurs="0"/&gt;
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
    "getFmcVoltageResult",
    "fmcVoltageObject"
})
@XmlRootElement(name = "GetFmcVoltageResponse")
public class GetFmcVoltageResponse {

    @XmlElement(name = "GetFmcVoltageResult")
    protected Boolean getFmcVoltageResult;
    @XmlElementRef(name = "fmcVoltageObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<FmcVoltageObject> fmcVoltageObject;

    /**
     * 获取getFmcVoltageResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetFmcVoltageResult() {
        return getFmcVoltageResult;
    }

    /**
     * 设置getFmcVoltageResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetFmcVoltageResult(Boolean value) {
        this.getFmcVoltageResult = value;
    }

    /**
     * 获取fmcVoltageObject属性的值。
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}
     *
     */
    public JAXBElement<FmcVoltageObject> getFmcVoltageObject() {
        return fmcVoltageObject;
    }

    /**
     * 设置fmcVoltageObject属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}
     *
     */
    public void setFmcVoltageObject(JAXBElement<FmcVoltageObject> value) {
        this.fmcVoltageObject = value;
    }

}
