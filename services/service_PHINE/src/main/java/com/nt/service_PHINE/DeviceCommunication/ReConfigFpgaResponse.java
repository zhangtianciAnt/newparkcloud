
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
 *         &lt;element name="ReConfigFpgaResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    "reConfigFpgaResult"
})
@XmlRootElement(name = "ReConfigFpgaResponse")
public class ReConfigFpgaResponse {

    @XmlElement(name = "ReConfigFpgaResult")
    protected Boolean reConfigFpgaResult;

    /**
     * 获取reConfigFpgaResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isReConfigFpgaResult() {
        return reConfigFpgaResult;
    }

    /**
     * 设置reConfigFpgaResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setReConfigFpgaResult(Boolean value) {
        this.reConfigFpgaResult = value;
    }

}
