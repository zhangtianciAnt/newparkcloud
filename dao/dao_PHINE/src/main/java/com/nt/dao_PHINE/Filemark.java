package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "filemark")
public class Filemark extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String versiondescribtion;

    private String version;

    private String fileid;

    private String remarks;
}
