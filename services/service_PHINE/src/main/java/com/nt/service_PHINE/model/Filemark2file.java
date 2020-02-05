package com.nt.service_PHINE.model;

import com.nt.utils.dao.BaseModel;
import javax.persistence.*;

public class Filemark2file extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String fileid;

    private String fileversionid;

    private String remarks;

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

    /**
     * @return fileid
     */
    public String getFileid() {
        return fileid;
    }

    /**
     * @param fileid
     */
    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    /**
     * @return fileversionid
     */
    public String getFileversionid() {
        return fileversionid;
    }

    /**
     * @param fileversionid
     */
    public void setFileversionid(String fileversionid) {
        this.fileversionid = fileversionid;
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