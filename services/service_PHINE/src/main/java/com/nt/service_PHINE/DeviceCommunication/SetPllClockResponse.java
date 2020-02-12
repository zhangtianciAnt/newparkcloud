
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
 *         &lt;element name="SetPllClockResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    "setPllClockResult"
})
@XmlRootElement(name = "SetPllClockResponse")
public class SetPllClockResponse {

    @XmlElement(name = "SetPllClockResult")
    protected Boolean setPllClockResult;

    /**
     * 获取setPllClockResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSetPllClockResult() {
        return setPllClockResult;
    }

    /**
     * 设置setPllClockResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSetPllClockResult(Boolean value) {
        this.setPllClockResult = value;
    }

}
