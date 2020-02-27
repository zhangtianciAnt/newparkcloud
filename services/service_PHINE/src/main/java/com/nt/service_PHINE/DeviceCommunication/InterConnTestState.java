
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InterConnTestState complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="InterConnTestState"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Progress" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="TestId" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}InterConnTestItem" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterConnTestState", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "progress",
    "testId"
})
public class InterConnTestState {

    @XmlElement(name = "Progress")
    protected Integer progress;
    @XmlElement(name = "TestId")
    @XmlSchemaType(name = "string")
    protected InterConnTestItem testId;

    /**
     * ��ȡprogress���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * ����progress���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setProgress(Integer value) {
        this.progress = value;
    }

    /**
     * ��ȡtestId���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link InterConnTestItem }
     *
     */
    public InterConnTestItem getTestId() {
        return testId;
    }

    /**
     * ����testId���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link InterConnTestItem }
     *
     */
    public void setTestId(InterConnTestItem value) {
        this.testId = value;
    }

}
