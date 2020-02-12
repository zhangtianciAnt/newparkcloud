
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OpenConnectionResult" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice}ConnectionResult" minOccurs="0"/&gt;
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
     * 获取openConnectionResult属性的值。
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
     * 设置openConnectionResult属性的值。
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
