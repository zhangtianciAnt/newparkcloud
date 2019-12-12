package com.nt.dao_BASFSQL;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;


public class AccessLevel  {


    private String RecNum;


    private String ALNum;

    public String getRecNum() {
        return RecNum;
    }

    public void setRecNum(String recNum) {
        RecNum = recNum;
    }

    public String getALNum() {
        return ALNum;
    }

    public void setALNum(String ALNum) {
        this.ALNum = ALNum;
    }
}
