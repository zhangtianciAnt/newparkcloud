package com.nt.dao_Pfans.PFANS8000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Information extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String Information_id;

    private String title;

    private String address;

    private String availablestate;

    private String createby;

    private Date createon;

    private String modifyby;

    private Date modifyon;

    private String owner;

    private String status;



}
