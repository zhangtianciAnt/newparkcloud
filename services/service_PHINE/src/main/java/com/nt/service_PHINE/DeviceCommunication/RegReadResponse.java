
package com.nt.service_PHINE.DeviceCommunication;

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
 *         &lt;element name="RegReadResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="regData" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
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
    "regReadResult",
    "regData"
})
@XmlRootElement(name = "RegReadResponse")
public class RegReadResponse {

    @XmlElement(name = "RegReadResult")
    protected Boolean regReadResult;
    @XmlSchemaType(name = "unsignedInt")
    protected Long regData;

    /**
     * ��ȡregReadResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRegReadResult() {
        return regReadResult;
    }

    /**
     * ����regReadResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRegReadResult(Boolean value) {
        this.regReadResult = value;
    }

    /**
     * ��ȡregData���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getRegData() {
        return regData;
    }

    /**
     * ����regData���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setRegData(Long value) {
        this.regData = value;
    }

}
