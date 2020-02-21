package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Operationdetail extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String operationid;

    private String devicename;

    private String configurationtype;

    private String filename;

    private String operationresult;

    private String remarks;

    private String fileid;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    /**
     * @return operationid
     */
    public String getOperationid() {
        return operationid;
    }

    /**
     * @param operationid
     */
    public void setOperationid(String operationid) {
        this.operationid = operationid;
    }

    /**
     * @return devicename
     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * @param devicename
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    /**
     * @return configurationtype
     */
    public String getConfigurationtype() {
        return configurationtype;
    }

    /**
     * @param configurationtype
     */
    public void setConfigurationtype(String configurationtype) {
        this.configurationtype = configurationtype;
    }

    /**
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return operationresult
     */
    public String getOperationresult() {
        return operationresult;
    }

    /**
     * @param operationresult
     */
    public void setOperationresult(String operationresult) {
        this.operationresult = operationresult;
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
