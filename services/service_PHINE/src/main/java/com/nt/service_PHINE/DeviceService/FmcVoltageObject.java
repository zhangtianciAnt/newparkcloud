
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FmcId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="FpgaId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="IsWritetoEeprom" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="VoltageValueIndex" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
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
    "deviceId",
    "fmcId",
    "fpgaId",
    "isWritetoEeprom",
    "voltageValueIndex"
})
public class FmcVoltageObject {

    @XmlElementRef(name = "DeviceId", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceId;
    @XmlElement(name = "FmcId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fmcId;
    @XmlElement(name = "FpgaId")
    @XmlSchemaType(name = "unsignedInt")
    protected Long fpgaId;
    @XmlElement(name = "IsWritetoEeprom")
    protected Boolean isWritetoEeprom;
    @XmlElement(name = "VoltageValueIndex")
    @XmlSchemaType(name = "unsignedInt")
    protected Long voltageValueIndex;

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
     * ��ȡvoltageValueIndex���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getVoltageValueIndex() {
        return voltageValueIndex;
    }

    /**
     * ����voltageValueIndex���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setVoltageValueIndex(Long value) {
        this.voltageValueIndex = value;
    }

}
