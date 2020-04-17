
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>PllRunMode�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="PllRunMode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="FreeRunMode"/&gt;
 *     &lt;enumeration value="StandardMode"/&gt;
 *     &lt;enumeration value="ZeroDelayMode"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "PllRunMode", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform")
@XmlEnum
public enum PllRunMode {

    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("FreeRunMode")
    FREE_RUN_MODE("FreeRunMode"),
    @XmlEnumValue("StandardMode")
    STANDARD_MODE("StandardMode"),
    @XmlEnumValue("ZeroDelayMode")
    ZERO_DELAY_MODE("ZeroDelayMode");
    private final String value;

    PllRunMode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PllRunMode fromValue(String v) {
        for (PllRunMode c: PllRunMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
