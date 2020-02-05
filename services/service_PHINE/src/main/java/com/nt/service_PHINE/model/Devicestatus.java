package com.nt.service_PHINE.model;

import com.nt.utils.dao.BaseModel;
import java.util.Date;
import javax.persistence.*;

public class Devicestatus extends BaseModel {
    private String deviceid;

    private Date checktime;

    private String devicestatus;

    private String exceptioninfo;

    private String remarks;

    /**
     * @return deviceid
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * @param deviceid
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    /**
     * @return checktime
     */
    public Date getChecktime() {
        return checktime;
    }

    /**
     * @param checktime
     */
    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    /**
     * @return devicestatus
     */
    public String getDevicestatus() {
        return devicestatus;
    }

    /**
     * @param devicestatus
     */
    public void setDevicestatus(String devicestatus) {
        this.devicestatus = devicestatus;
    }

    /**
     * @return exceptioninfo
     */
    public String getExceptioninfo() {
        return exceptioninfo;
    }

    /**
     * @param exceptioninfo
     */
    public void setExceptioninfo(String exceptioninfo) {
        this.exceptioninfo = exceptioninfo;
    }

    /**
     * @return remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}