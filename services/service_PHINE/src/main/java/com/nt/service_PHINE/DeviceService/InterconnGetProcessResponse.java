
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
 *         &lt;element name="InterconnGetProcessResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="CurrTestState" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}InterConnTestState" minOccurs="0"/&gt;
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
    "interconnGetProcessResult",
    "currTestState"
})
@XmlRootElement(name = "InterconnGetProcessResponse")
public class InterconnGetProcessResponse {

    @XmlElement(name = "InterconnGetProcessResult")
    protected Boolean interconnGetProcessResult;
    @XmlElementRef(name = "CurrTestState", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<InterConnTestState> currTestState;

    /**
     * ��ȡinterconnGetProcessResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isInterconnGetProcessResult() {
        return interconnGetProcessResult;
    }

    /**
     * ����interconnGetProcessResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInterconnGetProcessResult(Boolean value) {
        this.interconnGetProcessResult = value;
    }

    /**
     * ��ȡcurrTestState���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link InterConnTestState }{@code >}
     *
     */
    public JAXBElement<InterConnTestState> getCurrTestState() {
        return currTestState;
    }

    /**
     * ����currTestState���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link InterConnTestState }{@code >}
     *
     */
    public void setCurrTestState(JAXBElement<InterConnTestState> value) {
        this.currTestState = value;
    }

}
