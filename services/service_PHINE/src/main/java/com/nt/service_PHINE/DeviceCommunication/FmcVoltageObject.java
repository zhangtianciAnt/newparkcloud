
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.*;


/**
 * <p>FmcVoltageObject complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="FmcVoltageObject"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FmcId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="FpgaId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="IsWritetoEeprom" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="VoltageValue" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FmcVoltageObject", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice", propOrder = {
    "fmcId",
    "fpgaId",
    "isWritetoEeprom",
    "voltageValue"
})
public class FmcVoltageObject {

    @XmlElement(name = "FmcId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fmcId;
    @XmlElement(name = "FpgaId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fpgaId;
    @XmlElement(name = "IsWritetoEeprom")
    protected Boolean isWritetoEeprom;
    @XmlElement(name = "VoltageValue")
    protected Float voltageValue;

    /**
     * 获取fmcId属性的值。
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFmcId() {
        return fmcId;
    }

    /**
     * 设置fmcId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFmcId(Long value) {
        this.fmcId = value;
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
     * 获取isWritetoEeprom属性的值。
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIsWritetoEeprom() {
        return isWritetoEeprom;
    }

    /**
     * 设置isWritetoEeprom属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIsWritetoEeprom(Boolean value) {
        this.isWritetoEeprom = value;
    }

    /**
     * 获取voltageValue属性的值。
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public Float getVoltageValue() {
        return voltageValue;
    }

    /**
     * 设置voltageValue属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setVoltageValue(Float value) {
        this.voltageValue = value;
    }

}
