
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
 *         &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FpgaId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="InputClockFrequency" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="InputClockId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="OutputClockEnables" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfboolean" minOccurs="0"/&gt;
 *         &lt;element name="OutputClockFrequencys" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOffloat" minOccurs="0"/&gt;
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
    "deviceId",
    "fpgaId",
    "inputClockFrequency",
    "inputClockId",
    "outputClockEnables",
    "outputClockFrequencys",
    "pllId"
})
public class PllClockObject {

    @XmlElementRef(name = "DeviceId", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceId;
    @XmlElement(name = "FpgaId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fpgaId;
    @XmlElement(name = "InputClockFrequency")
    protected Float inputClockFrequency;
    @XmlElement(name = "InputClockId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long inputClockId;
    @XmlElementRef(name = "OutputClockEnables", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfboolean> outputClockEnables;
    @XmlElementRef(name = "OutputClockFrequencys", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOffloat> outputClockFrequencys;
    @XmlElement(name = "PllId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long pllId;

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
     * ��ȡinputClockFrequency���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public Float getInputClockFrequency() {
        return inputClockFrequency;
    }

    /**
     * ����inputClockFrequency���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setInputClockFrequency(Float value) {
        this.inputClockFrequency = value;
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
     * ��ȡoutputClockEnables���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}
     *
     */
    public JAXBElement<ArrayOfboolean> getOutputClockEnables() {
        return outputClockEnables;
    }

    /**
     * ����outputClockEnables���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}
     *
     */
    public void setOutputClockEnables(JAXBElement<ArrayOfboolean> value) {
        this.outputClockEnables = value;
    }

    /**
     * ��ȡoutputClockFrequencys���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}
     *
     */
    public JAXBElement<ArrayOffloat> getOutputClockFrequencys() {
        return outputClockFrequencys;
    }

    /**
     * ����outputClockFrequencys���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}
     *
     */
    public void setOutputClockFrequencys(JAXBElement<ArrayOffloat> value) {
        this.outputClockFrequencys = value;
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
