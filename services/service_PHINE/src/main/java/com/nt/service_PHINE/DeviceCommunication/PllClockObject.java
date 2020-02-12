
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * <p>PllClockObject complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="PllClockObject"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Enables_OutputClock" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfboolean" minOccurs="0"/&gt;
 *         &lt;element name="FpgaId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="Frequency_InputClock" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="Frequencys_OutputClock" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOffloat" minOccurs="0"/&gt;
 *         &lt;element name="InputClockId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="PllId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PllClockObject", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice", propOrder = {
    "enablesOutputClock",
    "fpgaId",
    "frequencyInputClock",
    "frequencysOutputClock",
    "inputClockId",
    "pllId"
})
public class PllClockObject {

    @XmlElementRef(name = "Enables_OutputClock", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfboolean> enablesOutputClock;
    @XmlElement(name = "FpgaId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fpgaId;
    @XmlElement(name = "Frequency_InputClock")
    protected Float frequencyInputClock;
    @XmlElementRef(name = "Frequencys_OutputClock", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOffloat> frequencysOutputClock;
    @XmlElement(name = "InputClockId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long inputClockId;
    @XmlElement(name = "PllId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long pllId;

    /**
     * 获取enablesOutputClock属性的值。
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}
     *
     */
    public JAXBElement<ArrayOfboolean> getEnablesOutputClock() {
        return enablesOutputClock;
    }

    /**
     * 设置enablesOutputClock属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}
     *
     */
    public void setEnablesOutputClock(JAXBElement<ArrayOfboolean> value) {
        this.enablesOutputClock = value;
    }

    /**
     * 获取fpgaId属性的值。
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFpgaId() {
        return fpgaId;
    }

    /**
     * 设置fpgaId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFpgaId(Long value) {
        this.fpgaId = value;
    }

    /**
     * 获取frequencyInputClock属性的值。
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public Float getFrequencyInputClock() {
        return frequencyInputClock;
    }

    /**
     * 设置frequencyInputClock属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setFrequencyInputClock(Float value) {
        this.frequencyInputClock = value;
    }

    /**
     * 获取frequencysOutputClock属性的值。
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}
     *
     */
    public JAXBElement<ArrayOffloat> getFrequencysOutputClock() {
        return frequencysOutputClock;
    }

    /**
     * 设置frequencysOutputClock属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}
     *
     */
    public void setFrequencysOutputClock(JAXBElement<ArrayOffloat> value) {
        this.frequencysOutputClock = value;
    }

    /**
     * 获取inputClockId属性的值。
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getInputClockId() {
        return inputClockId;
    }

    /**
     * 设置inputClockId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setInputClockId(Long value) {
        this.inputClockId = value;
    }

    /**
     * 获取pllId属性的值。
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getPllId() {
        return pllId;
    }

    /**
     * 设置pllId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setPllId(Long value) {
        this.pllId = value;
    }

}
