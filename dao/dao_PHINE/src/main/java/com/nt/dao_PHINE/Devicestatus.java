package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "devicestatus")
public class Devicestatus extends BaseModel {
    private String deviceid;

    private Date checktime;

    private String devicestatus;

    private String exceptioninfo;

    private String remarks;

}
