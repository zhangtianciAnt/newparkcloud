
package com.nt.service_PHINE.DeviceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>FpgaControlBit�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="FpgaControlBit"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="ConfigFpga"/&gt;
 *     &lt;enumeration value="SaveToSdCard"/&gt;
 *     &lt;enumeration value="ConfigAndSave"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "FpgaControlBit", namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform")
@XmlEnum
public enum FpgaControlBit {

    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("ConfigFpga")
    CONFIG_FPGA("ConfigFpga"),
    @XmlEnumValue("SaveToSdCard")
    SAVE_TO_SD_CARD("SaveToSdCard"),
    @XmlEnumValue("ConfigAndSave")
    CONFIG_AND_SAVE("ConfigAndSave");
    private final String value;

    FpgaControlBit(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FpgaControlBit fromValue(String v) {
        for (FpgaControlBit c: FpgaControlBit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
