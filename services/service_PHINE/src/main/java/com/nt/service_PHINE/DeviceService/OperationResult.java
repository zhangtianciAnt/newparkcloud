
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OperationResult�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="OperationResult"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Succeed"/&gt;
 *     &lt;enumeration value="Failed"/&gt;
 *     &lt;enumeration value="DeviceStatusAbnormal"/&gt;
 *     &lt;enumeration value="lastOperationIsInProgress"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "OperationResult", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform")
@XmlEnum
public enum OperationResult {

    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Succeed")
    SUCCEED("Succeed"),
    @XmlEnumValue("Failed")
    FAILED("Failed"),
    @XmlEnumValue("DeviceStatusAbnormal")
    DEVICE_STATUS_ABNORMAL("DeviceStatusAbnormal"),
    @XmlEnumValue("lastOperationIsInProgress")
    LAST_OPERATION_IS_IN_PROGRESS("lastOperationIsInProgress");
    private final String value;

    OperationResult(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperationResult fromValue(String v) {
        for (OperationResult c: OperationResult.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
