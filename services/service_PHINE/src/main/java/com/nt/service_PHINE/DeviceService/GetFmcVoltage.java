
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
 *         &lt;element name="fmcVoltageObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}FmcVoltageObject" minOccurs="0"/&gt;
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
    "fmcVoltageObject"
})
@XmlRootElement(name = "GetFmcVoltage")
public class GetFmcVoltage {

    @XmlElementRef(name = "fmcVoltageObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<FmcVoltageObject> fmcVoltageObject;

    /**
     * ��ȡfmcVoltageObject���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}
     *
     */
    public JAXBElement<FmcVoltageObject> getFmcVoltageObject() {
        return fmcVoltageObject;
    }

    /**
     * ����fmcVoltageObject���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}
     *
     */
    public void setFmcVoltageObject(JAXBElement<FmcVoltageObject> value) {
        this.fmcVoltageObject = value;
    }

}
