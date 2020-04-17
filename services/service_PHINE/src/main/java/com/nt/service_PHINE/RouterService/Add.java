
package com.nt.service_PHINE.RouterService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="n1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="n2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
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
    "n1",
    "n2"
})
@XmlRootElement(name = "Add")
public class Add {

    protected Double n1;
    protected Double n2;

    /**
     * ��ȡn1���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getN1() {
        return n1;
    }

    /**
     * ����n1���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setN1(Double value) {
        this.n1 = value;
    }

    /**
     * ��ȡn2���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getN2() {
        return n2;
    }

    /**
     * ����n2���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setN2(Double value) {
        this.n2 = value;
    }

}
