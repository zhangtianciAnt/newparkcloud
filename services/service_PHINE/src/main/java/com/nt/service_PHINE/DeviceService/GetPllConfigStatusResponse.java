
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetPllConfigStatusResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="configStatus" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}PllConfigStatus" minOccurs="0"/&gt;
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
    "getPllConfigStatusResult",
    "configStatus"
})
@XmlRootElement(name = "GetPllConfigStatusResponse")
public class GetPllConfigStatusResponse {

    @XmlElement(name = "GetPllConfigStatusResult")
    protected Boolean getPllConfigStatusResult;
    @XmlElementRef(name = "configStatus", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<PllConfigStatus> configStatus;

    /**
     * ��ȡgetPllConfigStatusResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetPllConfigStatusResult() {
        return getPllConfigStatusResult;
    }

    /**
     * ����getPllConfigStatusResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetPllConfigStatusResult(Boolean value) {
        this.getPllConfigStatusResult = value;
    }

    /**
     * ��ȡconfigStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PllConfigStatus }{@code >}
     *
     */
    public JAXBElement<PllConfigStatus> getConfigStatus() {
        return configStatus;
    }

    /**
     * ����configStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PllConfigStatus }{@code >}
     *
     */
    public void setConfigStatus(JAXBElement<PllConfigStatus> value) {
        this.configStatus = value;
    }

}
