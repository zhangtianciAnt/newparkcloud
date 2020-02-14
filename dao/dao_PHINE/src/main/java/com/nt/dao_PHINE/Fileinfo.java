package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fileinfo")
public class Fileinfo extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String fileid;

    private String projectid;

    private String filetype;

    private String deviceid;

    private String fpgaid;

    private String filename;

    private String url;

    private String remarks;
}
