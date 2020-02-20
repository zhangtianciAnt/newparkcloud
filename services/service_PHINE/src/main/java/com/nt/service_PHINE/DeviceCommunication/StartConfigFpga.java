
package com.nt.service_PHINE.DeviceCommunication;

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
 *         &lt;element name="fpgaConfigObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}FpgaConfigObject" minOccurs="0"/&gt;
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
    "fpgaConfigObject"
})
@XmlRootElement(name = "StartConfigFpga")
public class StartConfigFpga {

    @XmlElementRef(name = "fpgaConfigObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<FpgaConfigObject> fpgaConfigObject;

    /**
     * ��ȡfpgaConfigObject���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FpgaConfigObject }{@code >}
     *     
     */
    public JAXBElement<FpgaConfigObject> getFpgaConfigObject() {
        return fpgaConfigObject;
    }

    /**
     * ����fpgaConfigObject���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FpgaConfigObject }{@code >}
     *     
     */
    public void setFpgaConfigObject(JAXBElement<FpgaConfigObject> value) {
        this.fpgaConfigObject = value;
    }

}
