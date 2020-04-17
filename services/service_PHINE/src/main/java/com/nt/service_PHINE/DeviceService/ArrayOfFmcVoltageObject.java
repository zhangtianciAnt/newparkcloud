
package com.nt.service_PHINE.DeviceService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfFmcVoltageObject complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="ArrayOfFmcVoltageObject"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FmcVoltageObject" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}FmcVoltageObject" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfFmcVoltageObject", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "fmcVoltageObject"
})
public class ArrayOfFmcVoltageObject {

    @XmlElement(name = "FmcVoltageObject", nillable = true)
    protected List<FmcVoltageObject> fmcVoltageObject;

    /**
     * Gets the value of the fmcVoltageObject property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fmcVoltageObject property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFmcVoltageObject().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FmcVoltageObject }
     *
     *
     */
    public List<FmcVoltageObject> getFmcVoltageObject() {
        if (fmcVoltageObject == null) {
            fmcVoltageObject = new ArrayList<FmcVoltageObject>();
        }
        return this.fmcVoltageObject;
    }

}
