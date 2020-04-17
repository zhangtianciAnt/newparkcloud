
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ConfigStatus�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="ConfigStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Configing"/&gt;
 *     &lt;enumeration value="Succeed"/&gt;
 *     &lt;enumeration value="Failed"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ConfigStatus", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform")
@XmlEnum
public enum ConfigStatus {

    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Configing")
    CONFIGING("Configing"),
    @XmlEnumValue("Succeed")
    SUCCEED("Succeed"),
    @XmlEnumValue("Failed")
    FAILED("Failed");
    private final String value;

    ConfigStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConfigStatus fromValue(String v) {
        for (ConfigStatus c: ConfigStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
