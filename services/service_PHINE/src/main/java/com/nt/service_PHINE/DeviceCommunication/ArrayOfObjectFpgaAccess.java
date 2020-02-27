
package com.nt.service_PHINE.DeviceCommunication;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfObject_FpgaAccess complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="ArrayOfObject_FpgaAccess"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Object_FpgaAccess" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}Object_FpgaAccess" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfObject_FpgaAccess", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "objectFpgaAccess"
})
public class ArrayOfObjectFpgaAccess {

    @XmlElement(name = "Object_FpgaAccess")
    protected List<ObjectFpgaAccess> objectFpgaAccess;

    /**
     * Gets the value of the objectFpgaAccess property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectFpgaAccess property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectFpgaAccess().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectFpgaAccess }
     *
     *
     */
    public List<ObjectFpgaAccess> getObjectFpgaAccess() {
        if (objectFpgaAccess == null) {
            objectFpgaAccess = new ArrayList<ObjectFpgaAccess>();
        }
        return this.objectFpgaAccess;
    }

}
