
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="InterConnStatus" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/&gt;
 *         &lt;element name="ResultFilePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "interConnStatus",
    "resultFilePath"
})
@XmlRootElement(name = "InterconnGetResult")
public class InterconnGetResult {

    @XmlElementRef(name = "InterConnStatus", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfint> interConnStatus;
    @XmlElementRef(name = "ResultFilePath", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> resultFilePath;

    /**
     * ��ȡinterConnStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *
     */
    public JAXBElement<ArrayOfint> getInterConnStatus() {
        return interConnStatus;
    }

    /**
     * ����interConnStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *
     */
    public void setInterConnStatus(JAXBElement<ArrayOfint> value) {
        this.interConnStatus = value;
    }

    /**
     * ��ȡresultFilePath���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getResultFilePath() {
        return resultFilePath;
    }

    /**
     * ����resultFilePath���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setResultFilePath(JAXBElement<String> value) {
        this.resultFilePath = value;
    }

}
