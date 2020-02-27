
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Object_FpgaBatchAccess complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="Object_FpgaBatchAccess"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccessObjects" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ArrayOfObject_FpgaAccess" minOccurs="0"/&gt;
 *         &lt;element name="Count" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Object_FpgaBatchAccess", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "accessObjects",
    "count"
})
public class ObjectFpgaBatchAccess {

    @XmlElementRef(name = "AccessObjects", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfObjectFpgaAccess> accessObjects;
    @XmlElement(name = "Count")
    @XmlSchemaType(name = "unsignedInt")
    protected Long count;

    /**
     * ��ȡaccessObjects���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfObjectFpgaAccess }{@code >}
     *
     */
    public JAXBElement<ArrayOfObjectFpgaAccess> getAccessObjects() {
        return accessObjects;
    }

    /**
     * ����accessObjects���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfObjectFpgaAccess }{@code >}
     *
     */
    public void setAccessObjects(JAXBElement<ArrayOfObjectFpgaAccess> value) {
        this.accessObjects = value;
    }

    /**
     * ��ȡcount���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getCount() {
        return count;
    }

    /**
     * ����count���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setCount(Long value) {
        this.count = value;
    }

}
