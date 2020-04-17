
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>PllConfigStatus complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="PllConfigStatus"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="configStatus" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ConfigStatus" minOccurs="0"/&gt;
 *         &lt;element name="deviceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pllIds_ConfigFailed" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfunsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="progress" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PllConfigStatus", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "configStatus",
    "deviceID",
    "pllIdsConfigFailed",
    "progress"
})
public class PllConfigStatus {

    @XmlSchemaType(name = "string")
    protected ConfigStatus configStatus;
    @XmlElementRef(name = "deviceID", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceID;
    @XmlElementRef(name = "pllIds_ConfigFailed", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfunsignedInt> pllIdsConfigFailed;
    @XmlSchemaType(name = "unsignedInt")
    protected Long progress;

    /**
     * ��ȡconfigStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ConfigStatus }
     *
     */
    public ConfigStatus getConfigStatus() {
        return configStatus;
    }

    /**
     * ����configStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ConfigStatus }
     *
     */
    public void setConfigStatus(ConfigStatus value) {
        this.configStatus = value;
    }

    /**
     * ��ȡdeviceID���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getDeviceID() {
        return deviceID;
    }

    /**
     * ����deviceID���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setDeviceID(JAXBElement<String> value) {
        this.deviceID = value;
    }

    /**
     * ��ȡpllIdsConfigFailed���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfunsignedInt }{@code >}
     *
     */
    public JAXBElement<ArrayOfunsignedInt> getPllIdsConfigFailed() {
        return pllIdsConfigFailed;
    }

    /**
     * ����pllIdsConfigFailed���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfunsignedInt }{@code >}
     *
     */
    public void setPllIdsConfigFailed(JAXBElement<ArrayOfunsignedInt> value) {
        this.pllIdsConfigFailed = value;
    }

    /**
     * ��ȡprogress���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getProgress() {
        return progress;
    }

    /**
     * ����progress���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setProgress(Long value) {
        this.progress = value;
    }

}
