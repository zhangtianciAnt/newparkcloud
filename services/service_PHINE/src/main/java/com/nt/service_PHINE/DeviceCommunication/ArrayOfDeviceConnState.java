
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>ArrayOfDeviceConnState complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ArrayOfDeviceConnState"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DeviceConnState" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice}DeviceConnState" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDeviceConnState", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice", propOrder = {
    "deviceConnState"
})
public class ArrayOfDeviceConnState {

    @XmlElement(name = "DeviceConnState", nillable = true)
    protected List<DeviceConnState> deviceConnState;

    /**
     * Gets the value of the deviceConnState property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deviceConnState property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeviceConnState().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeviceConnState }
     *
     *
     */
    public List<DeviceConnState> getDeviceConnState() {
        if (deviceConnState == null) {
            deviceConnState = new ArrayList<DeviceConnState>();
        }
        return this.deviceConnState;
    }

}
