
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ConnectionResult的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="ConnectionResult"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Result_Unknown"/&gt;
 *     &lt;enumeration value="Result_OK"/&gt;
 *     &lt;enumeration value="Result_Occupied"/&gt;
 *     &lt;enumeration value="Result_Error"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ConnectionResult", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_HardwareDevice")
@XmlEnum
public enum ConnectionResult {

    @XmlEnumValue("Result_Unknown")
    RESULT_UNKNOWN("Result_Unknown"),
    @XmlEnumValue("Result_OK")
    RESULT_OK("Result_OK"),
    @XmlEnumValue("Result_Occupied")
    RESULT_OCCUPIED("Result_Occupied"),
    @XmlEnumValue("Result_Error")
    RESULT_ERROR("Result_Error");
    private final String value;

    ConnectionResult(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConnectionResult fromValue(String v) {
        for (ConnectionResult c: ConnectionResult.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
