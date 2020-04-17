
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ChipType�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="ChipType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Ku115"/&gt;
 *     &lt;enumeration value="Vu440"/&gt;
 *     &lt;enumeration value="S10"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ChipType", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform")
@XmlEnum
public enum ChipType {

    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Ku115")
    KU_115("Ku115"),
    @XmlEnumValue("Vu440")
    VU_440("Vu440"),
    @XmlEnumValue("S10")
    S_10("S10");
    private final String value;

    ChipType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ChipType fromValue(String v) {
        for (ChipType c: ChipType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
