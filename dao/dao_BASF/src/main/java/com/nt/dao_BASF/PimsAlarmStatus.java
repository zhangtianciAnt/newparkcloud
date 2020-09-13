package com.nt.dao_BASF;

/**
 * pims报警
 */
public enum PimsAlarmStatus {

    LL("低低", "0"), L("低", "1"), H("高", "2"), HH("高高", "3"), NORMAL("正常", "4");

    private String status;
    private String value;

    PimsAlarmStatus(String status, String value) {
        this.status = status;
        this.value = value;
    }

    public static String getStatus(String value) {
        for (PimsAlarmStatus p : PimsAlarmStatus.values()) {
            if (value.equals(p.getValue())) {
                return p.getStatus();
            }
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
