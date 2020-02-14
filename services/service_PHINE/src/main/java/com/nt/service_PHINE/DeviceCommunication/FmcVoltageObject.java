
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>FmcVoltageObject complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
@XmlType(name = "FmcVoltageObject", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
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
     * ��ȡfmcId���Ե�ֵ��
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
     * ����fmcId���Ե�ֵ��
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
     * ��ȡisWritetoEeprom���Ե�ֵ��
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
     * ����isWritetoEeprom���Ե�ֵ��
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
     * ��ȡvoltageValue���Ե�ֵ��
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
     * ����voltageValue���Ե�ֵ��
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
