
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
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boardIndex" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
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
    "deviceId",
    "boardIndex",
    "fpgaBatchAccessObject"
})
@XmlRootElement(name = "RegBatWrite")
public class RegBatWrite {

    @XmlElementRef(name = "deviceId", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceId;
    protected Integer boardIndex;
    @XmlElementRef(name = "fpgaBatchAccessObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ObjectFpgaBatchAccess> fpgaBatchAccessObject;

    /**
     * ��ȡdeviceId���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getDeviceId() {
        return deviceId;
    }

    /**
     * ����deviceId���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setDeviceId(JAXBElement<String> value) {
        this.deviceId = value;
    }

    /**
     * ��ȡboardIndex���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getBoardIndex() {
        return boardIndex;
    }

    /**
     * ����boardIndex���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setBoardIndex(Integer value) {
        this.boardIndex = value;
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