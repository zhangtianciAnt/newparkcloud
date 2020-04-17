
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
 *         &lt;element name="OpenConnectionResult" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ConnectionResult" minOccurs="0"/&gt;
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
    "openConnectionResult"
})
@XmlRootElement(name = "OpenConnectionResponse")
public class OpenConnectionResponse {

    @XmlElement(name = "OpenConnectionResult")
    @XmlSchemaType(name = "string")
    protected ConnectionResult openConnectionResult;

    /**
     * ��ȡopenConnectionResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ConnectionResult }
     *
     */
    public ConnectionResult getOpenConnectionResult() {
        return openConnectionResult;
    }

    /**
     * ����openConnectionResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ConnectionResult }
     *
     */
    public void setOpenConnectionResult(ConnectionResult value) {
        this.openConnectionResult = value;
    }

}
