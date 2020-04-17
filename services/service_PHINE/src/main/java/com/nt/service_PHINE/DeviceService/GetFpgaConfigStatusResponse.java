
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="GetFpgaConfigStatusResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="configStatus" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ConfigStatus" minOccurs="0"/&gt;
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
    "getFpgaConfigStatusResult",
    "configStatus"
})
@XmlRootElement(name = "GetFpgaConfigStatusResponse")
public class GetFpgaConfigStatusResponse {

    @XmlElement(name = "GetFpgaConfigStatusResult")
    protected Boolean getFpgaConfigStatusResult;
    @XmlSchemaType(name = "string")
    protected ConfigStatus configStatus;

    /**
     * ��ȡgetFpgaConfigStatusResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetFpgaConfigStatusResult() {
        return getFpgaConfigStatusResult;
    }

    /**
     * ����getFpgaConfigStatusResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetFpgaConfigStatusResult(Boolean value) {
        this.getFpgaConfigStatusResult = value;
    }

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

}
