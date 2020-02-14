
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>PllClockObject complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
@XmlType(name = "PllClockObject", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "enablesOutputClock",
    "fpgaId",
    "frequencyInputClock",
    "frequencysOutputClock",
    "inputClockId",
    "pllId"
})
public class PllClockObject {

    @XmlElementRef(name = "Enables_OutputClock", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfboolean> enablesOutputClock;
    @XmlElement(name = "FpgaId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fpgaId;
    @XmlElement(name = "Frequency_InputClock")
    protected Float frequencyInputClock;
    @XmlElementRef(name = "Frequencys_OutputClock", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOffloat> frequencysOutputClock;
    @XmlElement(name = "InputClockId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long inputClockId;
    @XmlElement(name = "PllId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long pllId;

    /**
     * ��ȡenablesOutputClock���Ե�ֵ��
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
     * ����enablesOutputClock���Ե�ֵ��
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
     * ��ȡfpgaId���Ե�ֵ��
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
     * ����fpgaId���Ե�ֵ��
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
     * ��ȡfrequencyInputClock���Ե�ֵ��
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
     * ����frequencyInputClock���Ե�ֵ��
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
     * ��ȡfrequencysOutputClock���Ե�ֵ��
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
     * ����frequencysOutputClock���Ե�ֵ��
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
     * ��ȡinputClockId���Ե�ֵ��
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
     * ����inputClockId���Ե�ֵ��
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
     * ��ȡpllId���Ե�ֵ��
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
     * ����pllId���Ե�ֵ��
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
