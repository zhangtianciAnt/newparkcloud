
package com.nt.service_PHINE.DeviceCommunication;

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
 *         &lt;element name="RegBatReadResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="fpgaBatchAccessObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}Object_FpgaBatchAccess" minOccurs="0"/&gt;
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
    "regBatReadResult",
    "fpgaBatchAccessObject"
})
@XmlRootElement(name = "RegBatReadResponse")
public class RegBatReadResponse {

    @XmlElement(name = "RegBatReadResult")
    protected Boolean regBatReadResult;
    @XmlElementRef(name = "fpgaBatchAccessObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ObjectFpgaBatchAccess> fpgaBatchAccessObject;

    /**
     * ��ȡregBatReadResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRegBatReadResult() {
        return regBatReadResult;
    }

    /**
     * ����regBatReadResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRegBatReadResult(Boolean value) {
        this.regBatReadResult = value;
    }

    /**
     * ��ȡfpgaBatchAccessObject���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}
     *
     */
    public JAXBElement<ObjectFpgaBatchAccess> getFpgaBatchAccessObject() {
        return fpgaBatchAccessObject;
    }

    /**
     * ����fpgaBatchAccessObject���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}
     *
     */
    public void setFpgaBatchAccessObject(JAXBElement<ObjectFpgaBatchAccess> value) {
        this.fpgaBatchAccessObject = value;
    }

}
