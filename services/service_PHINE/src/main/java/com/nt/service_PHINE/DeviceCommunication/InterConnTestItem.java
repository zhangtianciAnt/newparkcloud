
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InterConnTestItem�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="InterConnTestItem"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Initialize"/&gt;
 *     &lt;enumeration value="GT"/&gt;
 *     &lt;enumeration value="IO"/&gt;
 *     &lt;enumeration value="CARD"/&gt;
 *     &lt;enumeration value="END"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "InterConnTestItem", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform")
@XmlEnum
public enum InterConnTestItem {

    @XmlEnumValue("Initialize")
    INITIALIZE("Initialize"),
    GT("GT"),
    IO("IO"),
    CARD("CARD"),
    END("END");
    private final String value;

    InterConnTestItem(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InterConnTestItem fromValue(String v) {
        for (InterConnTestItem c: InterConnTestItem.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
