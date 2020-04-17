
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BoardInfo complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="BoardInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="belongDeviceSlotId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boardId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boardType" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}BoardType" minOccurs="0"/&gt;
 *         &lt;element name="chipType" type="{http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform}ChipType" minOccurs="0"/&gt;
 *         &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ipPortNumber" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&gt;
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
@XmlType(name = "BoardInfo", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", propOrder = {
    "belongDeviceSlotId",
    "boardId",
    "boardType",
    "chipType",
    "ipAddress",
    "ipPortNumber",
    "runningStatus"
})
public class BoardInfo {

    @XmlElementRef(name = "belongDeviceSlotId", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> belongDeviceSlotId;
    @XmlElementRef(name = "boardId", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> boardId;
    @XmlSchemaType(name = "string")
    protected BoardType boardType;
    @XmlSchemaType(name = "string")
    protected ChipType chipType;
    @XmlElementRef(name = "ipAddress", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", type = JAXBElement.class, required = false)
    protected JAXBElement<String> ipAddress;
    @XmlSchemaType(name = "unsignedInt")
    protected Long ipPortNumber;
    @XmlSchemaType(name = "string")
    protected RunningStatus runningStatus;

    /**
     * ��ȡbelongDeviceSlotId���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getBelongDeviceSlotId() {
        return belongDeviceSlotId;
    }

    /**
     * ����belongDeviceSlotId���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setBelongDeviceSlotId(JAXBElement<String> value) {
        this.belongDeviceSlotId = value;
    }

    /**
     * ��ȡboardId���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getBoardId() {
        return boardId;
    }

    /**
     * ����boardId���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setBoardId(JAXBElement<String> value) {
        this.boardId = value;
    }

    /**
     * ��ȡboardType���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link BoardType }
     *
     */
    public BoardType getBoardType() {
        return boardType;
    }

    /**
     * ����boardType���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link BoardType }
     *
     */
    public void setBoardType(BoardType value) {
        this.boardType = value;
    }

    /**
     * ��ȡchipType���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ChipType }
     *
     */
    public ChipType getChipType() {
        return chipType;
    }

    /**
     * ����chipType���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ChipType }
     *
     */
    public void setChipType(ChipType value) {
        this.chipType = value;
    }

    /**
     * ��ȡipAddress���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public JAXBElement<String> getIpAddress() {
        return ipAddress;
    }

    /**
     * ����ipAddress���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     */
    public void setIpAddress(JAXBElement<String> value) {
        this.ipAddress = value;
    }

    /**
     * ��ȡipPortNumber���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getIpPortNumber() {
        return ipPortNumber;
    }

    /**
     * ����ipPortNumber���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setIpPortNumber(Long value) {
        this.ipPortNumber = value;
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
