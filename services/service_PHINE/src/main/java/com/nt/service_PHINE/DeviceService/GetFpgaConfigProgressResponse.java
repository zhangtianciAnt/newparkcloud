
package com.nt.service_PHINE.DeviceService;

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
 *         &lt;element name="GetFpgaConfigProgressResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="progress" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
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
    "getFpgaConfigProgressResult",
    "progress"
})
@XmlRootElement(name = "GetFpgaConfigProgressResponse")
public class GetFpgaConfigProgressResponse {

    @XmlElement(name = "GetFpgaConfigProgressResult")
    protected Boolean getFpgaConfigProgressResult;
    @XmlSchemaType(name = "unsignedInt")
    protected Long progress;

    /**
     * ��ȡgetFpgaConfigProgressResult���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGetFpgaConfigProgressResult() {
        return getFpgaConfigProgressResult;
    }

    /**
     * ����getFpgaConfigProgressResult���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGetFpgaConfigProgressResult(Boolean value) {
        this.getFpgaConfigProgressResult = value;
    }

    /**
     * ��ȡprogress���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getProgress() {
        return progress;
    }

    /**
     * ����progress���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setProgress(Long value) {
        this.progress = value;
    }

}
