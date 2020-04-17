
package com.nt.service_PHINE.DeviceService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfPllClockObject complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="ArrayOfPllClockObject"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PllClockObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}PllClockObject" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPllClockObject", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "pllClockObject"
})
public class ArrayOfPllClockObject {

    @XmlElement(name = "PllClockObject", nillable = true)
    protected List<PllClockObject> pllClockObject;

    /**
     * Gets the value of the pllClockObject property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pllClockObject property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPllClockObject().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PllClockObject }
     *
     *
     */
    public List<PllClockObject> getPllClockObject() {
        if (pllClockObject == null) {
            pllClockObject = new ArrayList<PllClockObject>();
        }
        return this.pllClockObject;
    }

}
