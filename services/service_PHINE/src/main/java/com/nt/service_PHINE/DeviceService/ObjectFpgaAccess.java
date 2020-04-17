
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Object_FpgaAccess complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="Object_FpgaAccess"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FpgaId" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
 *         &lt;element name="regAddress" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/&gt;
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
@XmlType(name = "Object_FpgaAccess", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "fpgaId",
    "status",
    "regAddress",
    "regData"
})
public class ObjectFpgaAccess {

    @XmlElement(name = "FpgaId")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer fpgaId;
    @XmlElement(name = "Status")
    @XmlSchemaType(name = "unsignedInt")
    protected Long status;
    @XmlSchemaType(name = "unsignedShort")
    protected Integer regAddress;
    @XmlSchemaType(name = "unsignedInt")
    protected Long regData;

    /**
     * ��ȡfpgaId���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getFpgaId() {
        return fpgaId;
    }

    /**
     * ����fpgaId���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setFpgaId(Integer value) {
        this.fpgaId = value;
    }

    /**
     * ��ȡstatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getStatus() {
        return status;
    }

    /**
     * ����status���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setStatus(Long value) {
        this.status = value;
    }

    /**
     * ��ȡregAddress���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getRegAddress() {
        return regAddress;
    }

    /**
     * ����regAddress���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setRegAddress(Integer value) {
        this.regAddress = value;
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
