
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DeviceInfo complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="DeviceInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="connectionStatus" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ConnectionStatus" minOccurs="0"/&gt;
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="deviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="deviceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="onlineStatus" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}OnlineStatus" minOccurs="0"/&gt;
 *         &lt;element name="ownedBoardIds" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/&gt;
 *         &lt;element name="runningStatus" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}RunningStatus" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeviceInfo", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "connectionStatus",
    "deviceId",
    "deviceType",
    "deviceUUID",
    "onlineStatus",
    "ownedBoardIds",
    "runningStatus"
})
public class DeviceInfo {

    @XmlSchemaType(name = "string")
    protected ConnectionStatus connectionStatus;
    @XmlElementRef(name = "deviceId", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceId;
    @XmlElementRef(name = "deviceType", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceType;
    @XmlElementRef(name = "deviceUUID", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceUUID;
    @XmlSchemaType(name = "string")
    protected OnlineStatus onlineStatus;
    @XmlElementRef(name = "ownedBoardIds", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfstring> ownedBoardIds;
    @XmlSchemaType(name = "string")
    protected RunningStatus runningStatus;

    /**
     * ��ȡconnectionStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ConnectionStatus }
     *
     */
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * ����connectionStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ConnectionStatus }
     *
     */
    public void setConnectionStatus(ConnectionStatus value) {
        this.connectionStatus = value;
    }

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
     * ��ȡdeviceType���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getDeviceType() {
        return deviceType;
    }

    /**
     * ����deviceType���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setDeviceType(JAXBElement<String> value) {
        this.deviceType = value;
    }

    /**
     * ��ȡdeviceUUID���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getDeviceUUID() {
        return deviceUUID;
    }

    /**
     * ����deviceUUID���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setDeviceUUID(JAXBElement<String> value) {
        this.deviceUUID = value;
    }

    /**
     * ��ȡonlineStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link OnlineStatus }
     *
     */
    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * ����onlineStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link OnlineStatus }
     *
     */
    public void setOnlineStatus(OnlineStatus value) {
        this.onlineStatus = value;
    }

    /**
     * ��ȡownedBoardIds���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *
     */
    public JAXBElement<ArrayOfstring> getOwnedBoardIds() {
        return ownedBoardIds;
    }

    /**
     * ����ownedBoardIds���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *
     */
    public void setOwnedBoardIds(JAXBElement<ArrayOfstring> value) {
        this.ownedBoardIds = value;
    }

    /**
     * ��ȡrunningStatus���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link RunningStatus }
     *
     */
    public RunningStatus getRunningStatus() {
        return runningStatus;
    }

    /**
     * ����runningStatus���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link RunningStatus }
     *
     */
    public void setRunningStatus(RunningStatus value) {
        this.runningStatus = value;
    }

}
